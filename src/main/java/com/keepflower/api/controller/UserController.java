package com.keepflower.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.response.SuccessResponseBody;
import com.keepflower.api.dto.request.SignUpRequest;
import com.keepflower.api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseBody> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        userService.createUser(signUpRequest.username(), signUpRequest.password());
        return ResponseEntity.ok(new SuccessResponseBody<>("User created successfully"));
    }
}
