package no.hvl.dat251.recipeapp.security;

import no.hvl.dat251.recipeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${token.expiration.minutes}")
    private int tokenExpirationMinutes;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(securityService.getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Map<HttpMethod, Set<String>> excludedEndpoints = new HashMap<>();
        excludedEndpoints.put(HttpMethod.GET, new HashSet<>(Arrays.asList("/api-documentation/**", "/v3/api-docs/**", "/swagger-ui/**")));
        excludedEndpoints.put(HttpMethod.POST, new HashSet<>(Arrays.asList("/login", "/user")));

        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        for(HttpMethod httpMethod : excludedEndpoints.keySet()) http.authorizeRequests().antMatchers(httpMethod, excludedEndpoints.get(httpMethod).toArray(String[]::new)).permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new AuthenticationFilter(authenticationManagerBean(), securityService.getAlgorithm(), tokenExpirationMinutes));
        http.addFilterBefore(new AuthorizationFilter(securityService.getAlgorithm(), excludedEndpoints), UsernamePasswordAuthenticationFilter.class);
    }
}
