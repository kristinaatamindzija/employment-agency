package com.authservice.service.imp;

import com.authservice.dto.LoginResponseDTO;
import com.authservice.dto.PaymentMethodDTO;
import com.authservice.dto.WebShopDTO;
import com.authservice.model.MerchantPaymentMethod;
import com.authservice.model.PaymentMethod;
import com.authservice.model.WebShop;
import com.authservice.repository.PaymentMethodRepository;
import com.authservice.repository.WebShopRepository;
import com.authservice.service.MerchantPaymentMethodService;
import com.authservice.service.PaymentMethodService;
import com.authservice.service.WebShopService;
import com.authservice.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class WebShopServiceImpl implements WebShopService {

    private final int PASSWORD_STRENGTH = 10;
    private final int TOKEN_LENGTH = 8;

    @Autowired
    private WebShopRepository webShopRepository;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private MerchantPaymentMethodService merchantPaymentMethodService;

    protected static SecureRandom random = new SecureRandom();
    @Override
    public WebShop register(String username, String password, String currency) {
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(PASSWORD_STRENGTH, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        WebShop webShop = new WebShop(username, encodedPassword, currency);
        webShop.setApiToken(generateRandomString(TOKEN_LENGTH));
        return webShopRepository.save(webShop);
    }

    @Override
    public LoginResponseDTO login(WebShopDTO webShopDTO) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(PASSWORD_STRENGTH, new SecureRandom());
        WebShop webShop = webShopRepository.findByUsername(webShopDTO.getUsername());
        if(webShop==null) return null;

        if(bCryptPasswordEncoder.matches(webShopDTO.getPassword(), webShop.getPassword()) || webShopDTO.getPassword().equals(webShop.getPassword())) {
            String jwt = tokenUtils.generateToken(webShop.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();
            return new LoginResponseDTO(jwt, expiresIn, webShop);
        }
        return null;
    }

    @Override
    public void addPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        WebShop webShop = webShopRepository.findByApiToken(paymentMethodDTO.getApiToken());
        if(webShop == null) throw new UsernameNotFoundException("WebShop not found");
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodDTO.getPaymentMethodId());
        if(paymentMethod == null) throw new UsernameNotFoundException("Payment method not found");
        Set<MerchantPaymentMethod> merchantPaymentMethods = webShop.getMerchantPaymentMethods();
        merchantPaymentMethods.stream().filter(merchantPaymentMethod -> merchantPaymentMethod.getPaymentMethod().getId()
                        .equals(paymentMethod.getId()))
                        .findFirst().ifPresent(merchantPaymentMethod -> {
            throw new RuntimeException("Payment method already exists");
        });
        MerchantPaymentMethod merchantPaymentMethod = new MerchantPaymentMethod(paymentMethod, generateRandomString(30), generateRandomString(100));
        merchantPaymentMethod = merchantPaymentMethodService.save(merchantPaymentMethod);
        merchantPaymentMethods.add(merchantPaymentMethod);
        webShopRepository.save(webShop);
    }

    @Override
    public void deletePaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        WebShop webShop = webShopRepository.findByApiToken(paymentMethodDTO.getApiToken());
        if(webShop == null) throw new UsernameNotFoundException("WebShop not found");
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodDTO.getPaymentMethodId());
        if(paymentMethod == null) throw new UsernameNotFoundException("Payment method not found");
        Set<MerchantPaymentMethod> merchantPaymentMethods = webShop.getMerchantPaymentMethods();
        merchantPaymentMethods.stream().filter(merchantPaymentMethod -> merchantPaymentMethod.getPaymentMethod().getId()
                        .equals(paymentMethod.getId()))
                        .findFirst().ifPresent(merchantPaymentMethod -> {
            merchantPaymentMethodService.delete(merchantPaymentMethod);
            merchantPaymentMethods.remove(merchantPaymentMethod);
            webShopRepository.save(webShop);
        });
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        while (length > 0) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            length--;
        }
        return result.toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webShopRepository.loadByUsername(username);
    }
}
