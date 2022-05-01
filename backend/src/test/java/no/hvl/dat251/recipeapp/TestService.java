package no.hvl.dat251.recipeapp;

import com.auth0.jwt.JWT;
import no.hvl.dat251.recipeapp.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestService {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TEST_USER = "test@email.com";

    @Value("${token.expiration.minutes}")
    private int tokenExpirationMinutes;

    @Autowired
    private SecurityService securityService;

    public String generateToken() {
        return generateToken(TEST_USER);
    }

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * tokenExpirationMinutes))
                .sign(securityService.getAlgorithm());
    }

}
