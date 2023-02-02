package com.authservice.service.imp;

import com.authservice.dto.LoginResponseDTO;
import com.authservice.dto.MerchantDataResponse;
import com.authservice.dto.PaymentMethodDTO;
import com.authservice.dto.WebShopDTO;
import com.authservice.model.PaymentMethod;
import com.authservice.model.WebShop;
import com.authservice.repository.WebShopRepository;
import com.authservice.service.PaymentMethodService;
import com.authservice.service.WebShopService;
import com.authservice.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class WebShopServiceImpl implements WebShopService {

    private final int PASSWORD_STRENGTH = 10;

    private final WebShopRepository webShopRepository;

    private final TokenUtils tokenUtils;

    private final PaymentMethodService paymentMethodService;

    protected static SecureRandom random = new SecureRandom();

    public WebShopServiceImpl(WebShopRepository webShopRepository, TokenUtils tokenUtils, PaymentMethodService paymentMethodService) {
        this.webShopRepository = webShopRepository;
        this.tokenUtils = tokenUtils;
        this.paymentMethodService = paymentMethodService;
    }

    @Override
    public WebShop register(String username, String password, String currency) {
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(PASSWORD_STRENGTH, random);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        WebShop webShop = new WebShop(username, encodedPassword, currency);
        webShop.setMerchantUuid(UUID.randomUUID().toString().replace("-", ""));
        WebShop newWebshop =  webShopRepository.save(webShop);
        log.info("Webshop with username: " + username + " registered");
        return newWebshop;
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
            log.info("Webshop with username: " + webShopDTO.getUsername() + " logged in");
            return new LoginResponseDTO(jwt, expiresIn, webShop);
        }
        return null;
    }

    @Override
    public void addPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        WebShop webShop = webShopRepository.findByMerchantUuid(paymentMethodDTO.getMerchantUuid());
        if(webShop == null) {
            log.error("Webshop with merchant uuid: " + paymentMethodDTO.getMerchantUuid() + " not found");
            throw new UsernameNotFoundException("WebShop not found");
        }
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodDTO.getPaymentMethodId());
        if(paymentMethod == null) {
            log.error("Payment method with id: " + paymentMethodDTO.getPaymentMethodId() + " not found");
            throw new UsernameNotFoundException("Payment method not found");
        }
        Set<PaymentMethod> paymentMethods = webShop.getPaymentMethods();
        paymentMethods.stream().filter(method -> method.getId()
                        .equals(paymentMethod.getId()))
                        .findFirst().ifPresent(method -> {
            log.error("Payment method with id: " + paymentMethodDTO.getPaymentMethodId() + " already exists");
            throw new RuntimeException("Payment method already exists");
        });
        paymentMethods.add(paymentMethod);
        log.info("Payment method with id: " + paymentMethodDTO.getPaymentMethodId() +
                " added to webshop with merchant uuid: " + paymentMethodDTO.getMerchantUuid());
        switch (Math.toIntExact(paymentMethodDTO.getPaymentMethodId())) {
            case 1: {
                webShop.setMerchantId(paymentMethodDTO.getMerchantId());
                webShop.setMerchantPassword(paymentMethodDTO.getMerchantPassword());
                break;
            }
            case 4: {
                webShop.setBitcoinApiToken(paymentMethodDTO.getBitcoinApiToken());
                break;
            }

        }
        webShopRepository.save(webShop);
        log.info("Webshop with merchant uuid: " + paymentMethodDTO.getMerchantUuid() + " saved");
    }

    @Override
    public void deletePaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        WebShop webShop = webShopRepository.findByMerchantUuid(paymentMethodDTO.getMerchantUuid());
        if(webShop == null) {
            log.error("Webshop with merchant uuid: " + paymentMethodDTO.getMerchantUuid() + " not found");
            throw new UsernameNotFoundException("WebShop not found");
        }
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodDTO.getPaymentMethodId());
        if(paymentMethod == null) {
            log.error("Payment method with id: " + paymentMethodDTO.getPaymentMethodId() + " not found");
            throw new UsernameNotFoundException("Payment method not found");
        }
        Set<PaymentMethod> merchantPaymentMethods = webShop.getPaymentMethods();
        if(merchantPaymentMethods.stream().noneMatch(method -> method.getId().equals(paymentMethod.getId()))) {
            log.error("Payment method with id: " + paymentMethodDTO.getPaymentMethodId() + " not found");
            throw new RuntimeException("Payment method not found");
        }
        merchantPaymentMethods.removeIf(method -> method.getId().equals(paymentMethod.getId()));
        switch (Math.toIntExact(paymentMethodDTO.getPaymentMethodId())) {
            case 1: {
                webShop.setMerchantId(null);
                webShop.setMerchantPassword(null);
                break;
            }
            case 4: {
                webShop.setBitcoinApiToken(null);
                break;
            }

        }
        webShopRepository.save(webShop);
        log.info("Webshop with merchant uuid: " + paymentMethodDTO.getMerchantUuid() + " saved");

    }

    @Override
    public MerchantDataResponse getMerchantData(String merchantUuid) {
        WebShop webShop = webShopRepository.findByMerchantUuid(merchantUuid);
        if(webShop == null) {
            log.error("Webshop with merchant uuid: " + merchantUuid + " not found");
            throw new UsernameNotFoundException("WebShop not found");
        }
        return new MerchantDataResponse(webShop.getMerchantId(), webShop.getMerchantPassword(),
                webShop.getSuccessUrl(), webShop.getFailUrl(), webShop.getErrorUrl(), webShop.getBankUrl(),
                webShop.getPaymentMethods(), webShop.getBitcoinApiToken());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webShopRepository.loadByUsername(username);
    }
}
