package no.hvl.dat251.recipeapp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import no.hvl.dat251.recipeapp.domain.Comment;
import no.hvl.dat251.recipeapp.domain.Rating;
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
@Hidden
public class RecipeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/recipes-all")
    @Operation(summary = "List all available recipes", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @GetMapping("/recipes")
    @Operation(summary = "List recipes created by current user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Recipe>> getRecipesForCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(user.getRecipes());
    }

    @GetMapping("/recipe/{id}")
    @Operation(summary = "Get recipe by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PostMapping("/recipe")
    @Operation(summary = "Save new recipe", responses = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<Recipe> saveRecipe(@RequestBody Recipe recipe) {
        User user = userService.getCurrentUser();
        recipe.setUser(user);
        recipe.getItems().forEach(item -> item.setRecipe(recipe));
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(recipeService.saveRecipe(recipe));
    }

    @PostMapping("/comment")
    @Operation(summary = "Save new comment for recipe", responses = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        User user = userService.getCurrentUser();
        comment.setUser(user);
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(recipeService.saveComment(comment));
    }

    @PostMapping("/rating")
    @Operation(summary = "Save new rating for recipe", responses = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<Rating> saveRating(@RequestBody Rating rating) {
        User user = userService.getCurrentUser();
        rating.setUser(user);
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(recipeService.saveRating(rating));
    }

    @GetMapping("/comments/{id}")
    @Operation(summary = "List all comments for recipe with provided ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Comment>> getCommentsForRecipe(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipe(id).getComments());
    }

    @GetMapping("/ratings/{id}")
    @Operation(summary = "List all ratings for recipe with provided ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Rating>> getRatingsForRecipe(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipe(id).getRatings());
    }

}
