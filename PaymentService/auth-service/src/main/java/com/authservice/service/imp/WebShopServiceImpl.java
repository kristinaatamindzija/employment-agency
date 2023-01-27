package com.authservice.service.imp;

import com.authservice.dto.LoginResponseDTO;
import com.authservice.dto.MerchantDataResponse;
import com.authservice.dto.PaymentMethodDTO;
import com.authservice.dto.WebShopDTO;
import com.authservice.model.PaymentMethod;
import com.authservice.model.WebShop;
import com.authservice.repository.PaymentMethodRepository;
import com.authservice.repository.WebShopRepository;
import com.authservice.service.PaymentMethodService;
import com.authservice.service.WebShopService;
import com.authservice.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Set;
import java.util.UUID;

@Service
public class WebShopServiceImpl implements WebShopService {

    private final int PASSWORD_STRENGTH = 10;

    @Autowired
    private WebShopRepository webShopRepository;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private PaymentMethodService paymentMethodService;


    protected static SecureRandom random = new SecureRandom();
    @Override
    public WebShop register(String username, String password, String currency) {
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(PASSWORD_STRENGTH, random);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        WebShop webShop = new WebShop(username, encodedPassword, currency);
        webShop.setMerchantUuid(UUID.randomUUID().toString().replace("-", ""));
        return webShopRepository.save(webShop);
    }

    @Override
    public LoginResponseDTO login(WebShopDTO webShopDTO) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(PASSWORD_STRENGTH, new SecureRandom());
        WebShop webShop = webShopRepository.findByUsername(webShopDTO.getUsername());
        if(webShop==null) return null;

        if(bCryptPasswordEncoder.matches(webShopDTO.getPassword(), webShop.getPassword()) ||
                webShopDTO.getPassword().equals(webShop.getPassword())) {
            String jwt = tokenUtils.generateToken(webShop.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();
            return new LoginResponseDTO(jwt, expiresIn, webShop);
        }
        return null;
    }

    @Override
    public void addPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        WebShop webShop = webShopRepository.findByMerchantUuid(paymentMethodDTO.getMerchantUuid());
        if(webShop == null) throw new UsernameNotFoundException("WebShop not found");
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodDTO.getPaymentMethodId());
        if(paymentMethod == null) throw new UsernameNotFoundException("Payment method not found");
        Set<PaymentMethod> paymentMethods = webShop.getPaymentMethods();
        paymentMethods.stream().filter(method -> method.getId()
                        .equals(paymentMethod.getId()))
                        .findFirst().ifPresent(method -> {
            throw new RuntimeException("Payment method already exists");
        });
        paymentMethods.add(paymentMethod);
        switch (Math.toIntExact(paymentMethodDTO.getPaymentMethodId())) {
            case 1:
            case 2: {
                webShop.setMerchantId(paymentMethodDTO.getMerchantId());
                webShop.setMerchantPassword(paymentMethodDTO.getMerchantPassword());
                break;
            }
//            case 3: {
//                webShop.setPaypalUsername(paymentMethodDTO.getPaypalUsername());
//                break;
//            }
//            case 4: {
//                webShop.setBitcoinWalletId(paymentMethodDTO.getBitcoinWalletId());
//                break;
//            }

        }
        webShopRepository.save(webShop);
    }

    @Override
    public void deletePaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        WebShop webShop = webShopRepository.findByMerchantUuid(paymentMethodDTO.getMerchantUuid());
        if(webShop == null) throw new UsernameNotFoundException("WebShop not found");
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodDTO.getPaymentMethodId());
        if(paymentMethod == null) throw new UsernameNotFoundException("Payment method not found");
        Set<PaymentMethod> merchantPaymentMethods = webShop.getPaymentMethods();
        if(merchantPaymentMethods.stream().noneMatch(method -> method.getId().equals(paymentMethod.getId())))
            throw new RuntimeException("Payment method not found");
        merchantPaymentMethods.removeIf(method -> method.getId().equals(paymentMethod.getId()));
        switch (Math.toIntExact(paymentMethodDTO.getPaymentMethodId())) {
            case 1:
            case 2: {
                webShop.setMerchantId(null);
                webShop.setMerchantPassword(null);
                break;
            }
//            case 3: {
//                webShop.setPaypalUsername(null);
//                break;
//            }
//            case 4: {
//                webShop.setBitcoinWalletId(null);
//                break;
//            }

        }
        webShopRepository.save(webShop);

    }

    @Override
    public MerchantDataResponse getMerchantData(String merchantUuid) {
        WebShop webShop = webShopRepository.findByMerchantUuid(merchantUuid);
        if(webShop == null) throw new UsernameNotFoundException("WebShop not found");
        return new MerchantDataResponse(webShop.getMerchantId(), webShop.getMerchantPassword(),
                webShop.getSuccessUrl(), webShop.getFailUrl(), webShop.getErrorUrl());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webShopRepository.loadByUsername(username);
    }
}
