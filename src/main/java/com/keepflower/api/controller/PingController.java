package com.keepflower.api.controller;

import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.response.SuccessResponseBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {
	@GetMapping
	public ResponseEntity<ResponseBody> ping() {
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseBody<>("Pong! The server is running."));
	}
}
