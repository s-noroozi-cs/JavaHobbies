package com.hobi.proxyaspect.aspect;

import com.hobi.proxyaspect.annotation.Authorization;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuthorizationAspect {

    @Before("@annotation(authorization)")
    public void handle_authorization(Authorization authorization){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String msg = String.format("time:%s: The request method: %s, url: %s", LocalDateTime.now(),request.getMethod(),request.getRequestURI());
        System.out.println("aspect impl. --> " + msg);
    }

}
