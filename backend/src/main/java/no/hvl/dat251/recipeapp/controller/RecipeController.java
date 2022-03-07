package no.hvl.dat251.recipeapp.controller;

import no.hvl.dat251.recipeapp.domain.Comment;
import no.hvl.dat251.recipeapp.domain.Recipe;
import no.hvl.dat251.recipeapp.domain.User;
import no.hvl.dat251.recipeapp.service.RecipeService;
import no.hvl.dat251.recipeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class RecipeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/recipes/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getRecipesForCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(recipeService.getRecipesByUser(user));
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PostMapping("/recipe")
    public ResponseEntity<Recipe> saveRecipe(@RequestBody Recipe recipe) {
        User user = userService.getCurrentUser();
        recipe.setUser(user);
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(recipeService.saveRecipe(recipe));
    }

    @PostMapping("/comment")
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        User user = userService.getCurrentUser();
        comment.setUser(user);
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(recipeService.saveComment(comment));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<List<Comment>> getCommentsForRecipe(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getCommentsForRecipe(id));
    }

}
