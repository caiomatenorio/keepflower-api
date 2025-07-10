package com.keepflower.api.controller;

import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.response.SuccessResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {
    /**
     * Simple ping/pong endpoint to verify that the server is up and running.
     *
     * @return a successful response containing the message "Pong! The server is
     *         running."
     */
    @GetMapping
    public ResponseEntity<ResponseBody> ping() {
        return ResponseEntity.ok(new SuccessResponseBody<>("Pong! The server is running."));
    }
}
