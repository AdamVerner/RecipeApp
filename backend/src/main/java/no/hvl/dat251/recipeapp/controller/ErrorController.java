package no.hvl.dat251.recipeapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
@Hidden
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(HttpServletRequest request) throws JsonProcessingException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return ResponseEntity.status(statusCode).body(new ObjectMapper().writeValueAsString(Collections.singletonMap("error_message", "Page not found")));
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return ResponseEntity.status(statusCode).body(new ObjectMapper().writeValueAsString(Collections.singletonMap("error_message", "Forbidden")));
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return ResponseEntity.status(statusCode).body(new ObjectMapper().writeValueAsString(Collections.singletonMap("error_message", "Internal server error")));
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ObjectMapper().writeValueAsString(Collections.singletonMap("error_message", "Unknown error")));
    }

}
