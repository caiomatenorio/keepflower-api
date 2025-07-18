package com.keepflower.api.common.util;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenExtractUtil {
	public String extractFromServlet(HttpServletRequest request);
	
	// TODO: implement extract from MVC scope
}
