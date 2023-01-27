package com.bankservice.feign;

import com.bankservice.dto.AuthServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "auth-service", url = "http://localhost:7702/auth")
public interface AuthServiceFeignClient {
    @GetMapping("/merchantData/{merchantUuid}")
    AuthServiceResponse getMerchantData(@PathVariable String merchantUuid);
}
