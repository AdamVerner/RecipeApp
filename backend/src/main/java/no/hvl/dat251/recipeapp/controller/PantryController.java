package no.hvl.dat251.recipeapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import no.hvl.dat251.recipeapp.domain.Pantry;
import no.hvl.dat251.recipeapp.domain.User;
import no.hvl.dat251.recipeapp.service.PantryService;
import no.hvl.dat251.recipeapp.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class PantryController {

    @Autowired
    private UserService userService;

    @Autowired
    private PantryService pantryService;


    @GetMapping("/pantry")
    @Operation(summary = "List pantry items for current user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<List<Pantry>> getPantryForCurrentUser(@RequestParam(required = false) String search) {
        User user = userService.getCurrentUser();
        if(StringUtils.isBlank(search)) {
            return ResponseEntity.ok(user.getPantry());
        } else {
            return ResponseEntity.ok(pantryService.searchPantry(search, user));
        }
    }

    @DeleteMapping("/pantry/{id}")
    @Operation(summary = "Delete pantry with given ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    public ResponseEntity<Integer> deletePantry(@PathVariable Integer id) {
        User user = userService.getCurrentUser();
        Pantry pantry = pantryService.getPantry(id);
        if(pantry == null || !pantry.getUser().equalsById(user)) {
            return ResponseEntity.notFound().build();
        }
        pantryService.deletePantry(pantry);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/pantry")
    @Operation(summary = "Save new pantry item", responses = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden for requests without authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorController.ErrorResponse.class))),
    })
    public ResponseEntity<Pantry> savePantry(@RequestBody Pantry pantry) {
        User user = userService.getCurrentUser();
        pantry.setUser(user);
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()))
                .body(pantryService.savePantry(pantry));
    }

}
