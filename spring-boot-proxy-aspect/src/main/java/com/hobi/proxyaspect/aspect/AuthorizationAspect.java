package com.hobi.proxyaspect.aspect;

import com.hobi.proxyaspect.annotation.Authorization;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AuthorizationAspect {

    @Before("@annotation(authorization)")
    public void handle_authorization(Authorization authorization){
        System.out.println("before method");
    }

}
