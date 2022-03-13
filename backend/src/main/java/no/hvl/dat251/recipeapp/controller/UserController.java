package no.hvl.dat251.recipeapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import no.hvl.dat251.recipeapp.domain.User;
import no.hvl.dat251.recipeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    @Operation(summary = "Get info about currently logged user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token"),
    })
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PostMapping("/user")
    @Operation(summary = "Registration of new user", responses = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Wrong or incomplete parameters, duplicated user"),
    })
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(userService.saveUser(user));
    }

}
