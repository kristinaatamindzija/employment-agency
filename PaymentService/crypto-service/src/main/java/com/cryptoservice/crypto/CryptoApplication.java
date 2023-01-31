package com.cryptoservice.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CryptoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoApplication.class, args);
	}

}
