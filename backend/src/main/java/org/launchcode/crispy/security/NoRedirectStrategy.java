package org.launchcode.crispy.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

class NoRedirectStrategy implements RedirectStrategy {

    @Override
    public void sendRedirect(final HttpServletRequest request, final HttpServletResponse response, final String url) throws IOException {
        // Empty
    }
}