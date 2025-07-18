package com.keepflower.api.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keepflower.api.common.util.JwtUtil;
import com.keepflower.api.exception.InvalidCredentialsException;
import com.keepflower.api.exception.UserNotFoundException;
import com.keepflower.api.model.Session;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserService userService;
	private final SessionService sessionService;
	private final JwtUtil jwtUtil;

	@Transactional
	public ResponseCookie[] login(String username, String password) throws InvalidCredentialsException {
		Session session;

		if (!userService.validateCredentials(username, password)) {
			throw new InvalidCredentialsException();
		}

		try {
			session = sessionService.createSession(username);
		} catch (UserNotFoundException e) {
			throw new InvalidCredentialsException();
		}

		String jwt = jwtUtil.generate(session.getId(), session.getUser().getId(), session.getUser().getUsername());
		return sessionService.createSessionCookies(jwt, session.getRefreshToken());
	}
}
