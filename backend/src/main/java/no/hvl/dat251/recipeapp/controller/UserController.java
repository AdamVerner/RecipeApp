package no.hvl.dat251.recipeapp.controller;

import no.hvl.dat251.recipeapp.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/user")
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "kuchato1@fit.cvut.cz", "password", "Tomas Kuchar"));
        users.add(new User(2, "schwepat@fit.cvut.cz", "password", "Patrik Schweika"));
        return users;
    }

}
