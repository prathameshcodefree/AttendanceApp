package com.attendance.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.attendance.model.User;
import com.attendance.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;
    

    @Autowired
    private UserService userService;
    
//    @Autowired
//    private TokenLogRepository tokenLogRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("i am coming here in filter levl 2");
        
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findByUsername(username);
            if (jwtService.validateToken(jwt, user)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        
        filterChain.doFilter(request, response);

//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            System.out.println("initiating jwt verification");
//            username = jwtService.extractUsername(jwt);
//            System.out.println(username);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.jwtService.userDetailsService().loadUserByUsername(username);
//            
////            boolean isValidToken = tokenLogRepository.findByToken(jwt)
////                                .map(t -> !t.isExpired() && !t.isRevoked())
////                                .orElse(false);
//
//            if (jwtService.validateToken(jwt, userDetails)) {
//                SecurityContext context = SecurityContextHolder.createEmptyContext();
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                context.setAuthentication(authToken);
//                SecurityContextHolder.setContext(context);
//            }
//        }

//        chain.doFilter(request, response);
    }
}
