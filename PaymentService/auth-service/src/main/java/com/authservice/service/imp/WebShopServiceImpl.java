package com.authservice.service.imp;

import com.authservice.dto.LoginResponseDTO;
import com.authservice.dto.WebShopDTO;
import com.authservice.model.WebShop;
import com.authservice.repository.WebShopRepository;
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

@Service
public class WebShopServiceImpl implements WebShopService {

    private final int PASSWORD_STRENGTH = 10;
    @Autowired
    private WebShopRepository webShopRepository;
    @Autowired
    private TokenUtils tokenUtils;

    protected static SecureRandom random = new SecureRandom();
    @Override
    public WebShop register(String username, String password, String currency) {
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(PASSWORD_STRENGTH, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        WebShop webShop = new WebShop(username, encodedPassword, currency);
        webShop.setApiToken(generateToken(webShop.getUsername()));
        return webShopRepository.save(webShop);
    }

    @Override
    public LoginResponseDTO login(WebShopDTO webShopDTO) {

//        Authentication authentication =
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(webShopDTO.getUsername(), webShopDTO.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        WebShop webShop = (WebShop) authentication.getPrincipal();
//        String jwt = tokenUtils.generateToken(webShop.getUsername());
//        int expiresIn = tokenUtils.getExpiredIn();
//
//        return new LoginResponseDTO(jwt, expiresIn, webShop);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(PASSWORD_STRENGTH, new SecureRandom());
        WebShop webShop = webShopRepository.findByUsername(webShopDTO.getUsername());
        if(webShop==null) return null;

        if(bCryptPasswordEncoder.matches(webShopDTO.getPassword(), webShop.getPassword())) {
            String jwt = tokenUtils.generateToken(webShop.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();
            return new LoginResponseDTO(jwt, expiresIn, webShop);
        }
        return null;
    }


    public synchronized String generateToken( String username ) {
        long longToken = Math.abs( random.nextLong() );
        String random = Long.toString( longToken, 16 );
        return ( username + ":" + random );
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webShopRepository.loadByUsername(username);
    }
}
