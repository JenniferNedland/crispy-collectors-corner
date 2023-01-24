package org.launchcode.crispy.security;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static java.util.Optional.ofNullable;

final class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String CLIENT_ID = "144302576001-o81ltuu0kfts285u4jr004jejbvgv4fm.apps.googleusercontent.com";
    private GoogleIdTokenVerifier verifier;

    TokenAuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(List.of(CLIENT_ID))
                .build();
    }

    /**
     * Called when a secured resource is requested.
     */
    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) {
        final String param = ofNullable(request.getHeader(AUTHORIZATION)).orElse(request.getParameter("t"));

        final String token = ofNullable(param).map(value -> value.replaceFirst("^Bearer", ""))
                .map(String::trim).orElseThrow(() -> new BadCredentialsException("No Token Found!"));

        GoogleIdToken.Payload payload;
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null) {
                throw new BadCredentialsException("Unable to verify token");
            }
            payload = idToken.getPayload();
//            if (!payload.getAudienceAsList().contains(CLIENT_ID)) {
//                throw new BadCredentialsException("Wrong audience");
//            }
        } catch (GeneralSecurityException | IOException e) {
            throw new BadCredentialsException("Unable to verify token", e);
        }

        final Authentication auth = new UsernamePasswordAuthenticationToken(payload.getSubject(), payload);
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response, final FilterChain chain,
                                            final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
