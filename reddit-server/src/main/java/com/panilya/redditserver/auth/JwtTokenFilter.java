package com.panilya.redditserver.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtTokenFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtProvider.validateToken(extractJwtTokenFromRequest(authorizationHeader))) {
            filterChain.doFilter(request, response);
            return;
        }

        String usernameFromToken = jwtProvider.getUsernameFromToken(extractJwtTokenFromRequest(authorizationHeader));

        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameFromToken, null, userDetails.getAuthorities());
        authenticationToken.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }


    private String extractJwtTokenFromRequest(String header) {
        if (!header.isEmpty() && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}
