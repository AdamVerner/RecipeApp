package no.hvl.dat251.recipeapp.controller;

import no.hvl.dat251.recipeapp.domain.Grocery;
import no.hvl.dat251.recipeapp.service.GroceryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class GroceryController {

    @Autowired
    private GroceryService groceryService;

    @GetMapping("/groceries")
    public ResponseEntity<List<Grocery>> getGroceries() {
        return ResponseEntity.ok(groceryService.getGroceries());
    }

    @PostMapping("/grocery")
    public ResponseEntity<Grocery> saveGrocery(@RequestBody Grocery grocery) {
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(groceryService.saveGrocery(grocery));
    }

}
