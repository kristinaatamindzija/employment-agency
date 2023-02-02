package com.bankservice.config;

import feign.Client;
import feign.Feign;
import feign.Retryer;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.util.ResourceUtils;


import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class CustomFeignConfiguration {

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .retryer(Retryer.NEVER_RETRY)
                .client(new Client.Default(getSSLSocketFactory(), null));
    }

    SSLSocketFactory getSSLSocketFactory() {
        char[] allPassword = "jabuka123".toCharArray();
        SSLContext sslContext = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resourceUrl = classLoader.getResource("keystores/springboot.jks");
            assert resourceUrl != null;
            sslContext = SSLContextBuilder
                    .create()
                    .setKeyStoreType("JKS")
                    .loadKeyMaterial(new File(resourceUrl.getFile()), allPassword, allPassword)
                    .loadTrustMaterial((x509Certificates, s) -> true)
                    .build();
        } catch (Exception e) { /* *** */ }
        return sslContext.getSocketFactory();
    }
}