package no.hvl.dat251.recipeapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import no.hvl.dat251.recipeapp.domain.Comment;
import no.hvl.dat251.recipeapp.domain.Rating;
import no.hvl.dat251.recipeapp.domain.Recipe;
import no.hvl.dat251.recipeapp.service.RecipeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/recipes-all")
    @Operation(summary = "List all available recipes", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Recipe>> getAllRecipes(@RequestParam(required = false) String search) {
        if(StringUtils.isBlank(search)) {
            return ResponseEntity.ok(recipeService.getAllRecipes());
        } else {
            return ResponseEntity.ok(recipeService.searchRecipes(search));
        }
    }

    @GetMapping("/recipes-suggested")
    @Operation(summary = "List suggested recipes", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Recipe>> getSuggestedRecipes() {
        return ResponseEntity.ok(recipeService.getSuggestedRecipes());
    }

    @GetMapping("/recipes")
    @Operation(summary = "List recipes created by current user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Recipe>> getRecipesForCurrentUser(@RequestParam(required = false) String search) {
        if(StringUtils.isBlank(search)) {
            return ResponseEntity.ok(recipeService.getRecipesByCurrentUser());
        } else {
            return ResponseEntity.ok(recipeService.searchRecipesByCurrentUser(search));
        }
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

    @GetMapping(value = "/recipe-image/{id}.jpg", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Get recipe image by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    public ResponseEntity<byte[]> getRecipeImage(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipeImage(id));
    }

    @PostMapping("/recipe")
    @Operation(summary = "Save new recipe", responses = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<Recipe> saveRecipe(@RequestBody Recipe recipe) {
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

}
