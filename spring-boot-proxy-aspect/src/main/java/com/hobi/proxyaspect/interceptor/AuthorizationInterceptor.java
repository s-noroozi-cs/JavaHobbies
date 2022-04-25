package com.hobi.proxyaspect.interceptor;

import com.hobi.proxyaspect.annotation.Authorization;
import com.hobi.proxyaspect.util.LogUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod methodHandle = (HandlerMethod) handler;
        if (methodHandle.getMethod().isAnnotationPresent(Authorization.class)) {
            LogUtil.log(request, "AuthorizationInterceptor");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
