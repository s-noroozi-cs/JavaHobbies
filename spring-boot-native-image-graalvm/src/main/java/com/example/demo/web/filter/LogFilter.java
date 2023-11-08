package com.example.demo.web.filter;

import com.example.demo.web.servlet.ValidateServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Order(0)
@Component
public class LogFilter implements Filter {
  private ValidateServlet validateServlet;

  public LogFilter() {
    this.validateServlet = new ValidateServlet();
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    long startTime = System.currentTimeMillis();
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    try {
      validateServlet.validate(httpServletRequest, httpServletResponse);
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      System.out.println("Duration: %d".formatted((System.currentTimeMillis() - startTime)));
    }
  }
}
