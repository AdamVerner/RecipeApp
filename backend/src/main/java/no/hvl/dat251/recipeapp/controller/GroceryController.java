package no.hvl.dat251.recipeapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import no.hvl.dat251.recipeapp.domain.Grocery;
import no.hvl.dat251.recipeapp.enums.GROCERY_CATEGORY;
import no.hvl.dat251.recipeapp.enums.QUANTITY_UNIT;
import no.hvl.dat251.recipeapp.service.GroceryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
public class GroceryController {

    @Autowired
    private GroceryService groceryService;

    @GetMapping("/groceries")
    @Operation(summary = "List all available groceries", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Grocery>> getGroceries() {
        return ResponseEntity.ok(groceryService.getGroceries());
    }

    @GetMapping("/grocery-categories")
    @Operation(summary = "List all available grocery categories", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<GROCERY_CATEGORY>> getGroceryCategories() {
        return ResponseEntity.ok(Arrays.asList(GROCERY_CATEGORY.values()));
    }

    @GetMapping("/quantity-units")
    @Operation(summary = "List all available quantity units", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<QUANTITY_UNIT>> getQuantityUnits() {
        return ResponseEntity.ok(Arrays.asList(QUANTITY_UNIT.values()));
    }

    @PostMapping("/grocery")
    @Operation(summary = "Save new grocery", responses = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<Grocery> saveGrocery(@RequestBody Grocery grocery) {
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(groceryService.saveGrocery(grocery));
    }

}
