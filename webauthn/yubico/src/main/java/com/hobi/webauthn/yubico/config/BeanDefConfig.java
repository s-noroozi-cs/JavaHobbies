package com.hobi.webauthn.yubico.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.hobi.webauthn.yubico.servlet.WebAuthnServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanDefConfig {

  @Bean
  public ServletRegistrationBean servletRegistrationBean() {
    ServletRegistrationBean bean =
        new ServletRegistrationBean(new WebAuthnServlet(), "/api/webauthn/*");
    bean.setLoadOnStartup(1);
    return bean;
  }


}
