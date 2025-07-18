package com.keepflower.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.response.SuccessResponseBody;
import com.keepflower.api.common.util.CookieUtil;
import com.keepflower.api.dto.request.LogInRequest;
import com.keepflower.api.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final CookieUtil cookieUtil;

	@PostMapping("/login")
	public ResponseEntity<ResponseBody> login(@RequestBody @Valid LogInRequest body) {
		ResponseCookie[] cookies = authService.login(body.username(), body.password());
		return ResponseEntity.status(HttpStatus.OK).headers(cookieUtil.cookiesToHeadersConsumer(cookies))
				.body(new SuccessResponseBody<>("Logged in successfully!"));
	}
}
