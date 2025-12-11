package com.hobi.webauthn.yubico.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebAuthnServlet extends HttpServlet {
  private final WebAuthnWebHandler handler = new WebAuthnWebHandler();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String pathInfo = request.getPathInfo();

    try {
      if (pathInfo == null) {
        handler.sendError(response, "Invalid endpoint", 400);

      } else {

        switch (pathInfo) {
          case "/register/start":
            handler.handleRegistrationStart(request, response);
            break;
          case "/register/finish":
            handler.handleRegistrationFinish(request, response);
            break;
          case "/login/start":
            handler.handleLoginStart(request, response);
            break;
          case "/login/finish":
            handler.handleLoginFinish(request, response);
            break;
          default:
            handler.sendError(response, "Endpoint not found", 404);
        }
      }
    } catch (Throwable e) {
      handler.sendError(response, "Server error: " + e.getMessage(), 500);
    }finally{
        response.getWriter().flush();
        response.getWriter().close();
    }
  }
}
