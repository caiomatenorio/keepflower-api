package com.keepflower.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {
    @PostMapping("/signup")
    public ResponseEntity<ResponseBody> signup() {
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBody> login() {
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseBody> logout() {
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseBody> refreshSession() {
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseBody> getAuthStatus() {
    }
}
