package com.keepflower.api.common.util;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtExtractUtil implements TokenExtractUtil {
	@Override
	@Nullable
	public String extractFromServlet(HttpServletRequest request) {
		return Optional.ofNullable(request.getCookies())
				.flatMap(cookies -> Arrays.stream(cookies)
						.filter(cookie -> cookie.getName().equals("access_token"))
						.findFirst())
				.map(Cookie::getValue)
				.orElse(null);
	}
}
