package no.hvl.dat251.recipeapp.security;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Value("${token.cypher.key}")
    private String cypherKey;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(cypherKey.getBytes());
    }

    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
