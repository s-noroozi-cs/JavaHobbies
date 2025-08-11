package com.example.demo;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServletServer extends HttpServlet {
    @PostMapping("/proxy")
    public ResponseEntity proxy(HttpServletRequest request) {
        System.out.println("uri: " + request.getRequestURL());
        System.out.println("method: " + request.getMethod());
        System.out.println("header x: " + request.getHeader("x"));
        System.out.println("header y: " + request.getHeader("y"));

        return ResponseEntity.ok("OK");
    }
}
