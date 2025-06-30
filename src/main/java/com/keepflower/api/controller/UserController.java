package com.keepflower.api.controller;

import com.keepflower.api.common.response.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
public class UserController {
    @GetMapping("/me")
    public ResponseEntity<ResponseBody> getCurrentUser() {
    }
}
