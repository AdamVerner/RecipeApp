package no.hvl.dat251.recipeapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.hvl.dat251.recipeapp.controller.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final Algorithm algorithm;
    private final Map<HttpMethod, Set<String>> excludedEndpoints;

    public AuthorizationFilter(Algorithm algorithm, Map<HttpMethod, Set<String>> excludedEndpoints) {
        this.algorithm = algorithm;
        this.excludedEndpoints = excludedEndpoints;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AntPathMatcher matcher = new AntPathMatcher();
        boolean excludedEndpoint = excludedEndpoints.keySet().stream()
                .filter(httpMethod -> httpMethod.name().equalsIgnoreCase(request.getMethod()))
                .flatMap(httpMethod -> excludedEndpoints.get(httpMethod).stream())
                .anyMatch(pattern -> matcher.match(pattern, request.getServletPath()));
        if(excludedEndpoint) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            String username = JWT.require(algorithm).build().verify(token).getSubject();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch(Exception e) {
            log.error("Wrong authorization token", e);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), new ErrorController.ErrorResponse("Wrong authorization token: " + e.getMessage()));
        }
    }

}
