package com.pcc.config;

import com.pcc.security.Encryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RequestLoggingInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String ipAddress = Encryptor.encrypt(request.getRemoteAddr());
        String userAgent = request.getHeader("User-Agent");
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.info("Incoming request from IP: {}, User-Agent: {}, URI: {}, Method: {}",
                ipAddress, userAgent, requestURI, method);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        String ipAddress = Encryptor.encrypt(request.getRemoteAddr());
        String userAgent = request.getHeader("User-Agent");
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        int statusCode = response.getStatus();
        log.info("Outgoing response from IP: {}, User-Agent: {}, URI: {}, Method: {}, Status Code: {}",
                ipAddress, userAgent, requestURI, method, statusCode);
    }
}
