package no.hvl.dat251.recipeapp.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import no.hvl.dat251.recipeapp.domain.*;
import no.hvl.dat251.recipeapp.repository.RecipeRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RecipeService {

    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserService userService;


    public Recipe saveRecipe(Recipe recipe) {
        if(recipe.getUser() == null) {
            recipe.setUser(userService.getCurrentUser());
        }
        recipe.setCreated(Instant.now());
        recipe.getItems().forEach(item -> item.setRecipe(recipe));
        if(ArrayUtils.isNotEmpty(recipe.getImage())) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(recipe.getImage());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                Thumbnails.of(inputStream)
                        .size(800, 600)
                        .outputFormat("JPEG")
                        .outputQuality(0.8)
                        .toOutputStream(outputStream);
                recipe.setImage(outputStream.toByteArray());
            } catch(Exception e) {
                recipe.setImage(null);
            }
        }
        return recipeRepository.save(recipe);
    }

    public Recipe getRecipe(Integer id) {
        User user = userService.getCurrentUser();
        return recipeRepository.findById(id).map(r -> computeRecipeFields(r, user)).orElse(null);
    }

    public byte[] getRecipeImage(Integer id) {
        return recipeRepository.findById(id).map(Recipe::getImage).orElse(null);
    }

    public byte[] getRecipeImage(Integer id) {
        return recipeRepository.findById(id).map(Recipe::getImage).orElse(null);
    }

    public List<Recipe> getAllRecipes() {
        User user = userService.getCurrentUser();
        return recipeRepository.findAll().stream().map(r -> computeRecipeFields(r, user)).collect(Collectors.toList());
    }

    public List<Recipe> getRecipesByCurrentUser() {
        User user = userService.getCurrentUser();
        return user.getRecipes().stream().map(r -> computeRecipeFields(r, user)).collect(Collectors.toList());
    }

    public List<Recipe> getSuggestedRecipes() {
        User user = userService.getCurrentUser();
        return recipeRepository.findAll()
                .stream()
                .filter(r -> r.getItems().stream().map(RecipeItem::getGrocery).distinct()
                        .allMatch(g -> user.getPantry().stream().map(Pantry::getGrocery).distinct().anyMatch(g::equalsById)))
                .sorted((r1, r2) -> {
                    List<Rating> ratings1 = r1.getRatings();
                    List<Rating> ratings2 = r2.getRatings();
                    double sum1 = ratings1.stream().map(Rating::getRating).reduce(0, Integer::sum);
                    double sum2 = ratings2.stream().map(Rating::getRating).reduce(0, Integer::sum);
                    return (int) ((100 * sum2 / (ratings2.size() + 1)) - (100 * sum1 / (ratings1.size() + 1)));
                })
                .limit(10)
                .map(r -> computeRecipeFields(r, user))
                .collect(Collectors.toList());
    }

    public List<Recipe> searchRecipes(String search) {
        User user = userService.getCurrentUser();
        return recipeRepository.findByNameContainingIgnoreCase(search)
                .stream().map(r -> computeRecipeFields(r, user)).collect(Collectors.toList());
    }

    public List<Recipe> searchRecipesByCurrentUser(String search) {
        User user = userService.getCurrentUser();
        return recipeRepository.findByNameContainingIgnoreCaseAndUserEquals(search, user)
                .stream().map(r -> computeRecipeFields(r, user)).collect(Collectors.toList());
    }

    public Comment saveComment(Comment comment) {
        User user = userService.getCurrentUser();
        comment.setUser(user);
        comment.setCreated(Instant.now());
        comment.getRecipe().getComments().add(comment);
        recipeRepository.save(comment.getRecipe());
        return comment;
    }

    public Rating saveRating(Rating rating) {
        User user = userService.getCurrentUser();
        rating.setUser(user);
        rating.getRecipe().getRatings().removeIf(r -> r.getUser().equalsById(user));
        rating.getRecipe().getRatings().add(rating);
        recipeRepository.save(rating.getRecipe());
        return rating;
    }

    private Recipe computeRecipeFields(Recipe recipe, User user) {
        List<Rating> ratings = recipe.getRatings();
        if(!ratings.isEmpty()) {
            recipe.setAverageRating((double) ratings.stream().mapToInt(Rating::getRating).reduce(0, Integer::sum) / ratings.size());
        }
        ratings.stream().filter(r -> r.getUser().equalsById(user)).findFirst().ifPresent(r -> recipe.setCurrentUserRating(r.getRating()));
        if(ArrayUtils.isNotEmpty(recipe.getImage())) {
            recipe.setImageUrl(String.format("%s/recipe-image/%d.jpg", serverUrl, recipe.getId()));
        }
        return recipe;
    }

}
