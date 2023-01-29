package com.example.paypalservice.feign;

import com.example.paypalservice.dto.AuthServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "auth-service", url = "http://localhost:7702/auth")
public interface AuthServiceFeignClient {
    @GetMapping("/merchantData/{merchantUuid}")
    AuthServiceResponse getMerchantData(@PathVariable String merchantUuid);
}