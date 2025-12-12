package com.hobi.webauthn.yubico.config;

import com.hobi.webauthn.yubico.servlet.AuthenticateServlet;
import com.hobi.webauthn.yubico.servlet.RegisterServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanDefConfig {

  @Bean
  public ServletRegistrationBean fidoRegisterServlet() {
    ServletRegistrationBean bean = new ServletRegistrationBean(new RegisterServlet(), "/register");
    bean.setLoadOnStartup(1);
    return bean;
  }

  @Bean
  public ServletRegistrationBean fidoAuthenticateServlet() {
    ServletRegistrationBean bean =
        new ServletRegistrationBean(new AuthenticateServlet(), "/authenticate");
    bean.setLoadOnStartup(1);
    return bean;
  }
}
