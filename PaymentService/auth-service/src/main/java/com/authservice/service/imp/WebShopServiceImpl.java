package com.authservice.service.imp;

import com.authservice.model.WebShop;
import com.authservice.repository.WebShopRepository;
import com.authservice.service.WebShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class WebShopServiceImpl implements WebShopService {
    @Autowired
    private WebShopRepository webShopRepository;

    protected static SecureRandom random = new SecureRandom();
    @Override
    public WebShop registerUser(String username, String password, String currency) {
        WebShop webShop = new WebShop(username, password, currency);
        webShop.setApiToken(generateToken(webShop.getUsername()));
        return webShopRepository.save(webShop);
    }


    public synchronized String generateToken( String username ) {
        long longToken = Math.abs( random.nextLong() );
        String random = Long.toString( longToken, 16 );
        return ( username + ":" + random );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webShopRepository.findByUsername(username);
    }
}
