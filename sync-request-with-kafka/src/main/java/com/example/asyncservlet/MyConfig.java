package com.example.asyncservlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;

@Configuration
public class MyConfig {


    private StreamBridge streamBridge;

    @Autowired
    public MyConfig(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Bean
    public ServletRegistrationBean myAsyncServlet() {
        HttpServlet servlet = new MyAsyncServlet(streamBridge);
        String urlMapping = "/async-servlet";
        ServletRegistrationBean registration = new ServletRegistrationBean(servlet, urlMapping);
        registration.setAsyncSupported(true);
        return registration;
    }
}
