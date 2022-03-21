package no.hvl.dat251.recipeapp.service;

import no.hvl.dat251.recipeapp.domain.Comment;
import no.hvl.dat251.recipeapp.domain.Rating;
import no.hvl.dat251.recipeapp.domain.Recipe;
import no.hvl.dat251.recipeapp.domain.User;
import no.hvl.dat251.recipeapp.repository.CommentRepository;
import no.hvl.dat251.recipeapp.repository.RatingRepository;
import no.hvl.dat251.recipeapp.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe getRecipe(Integer id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> searchRecipes(String search) {
        return recipeRepository.findByNameContainingIgnoreCase(search);
    }

    public List<Recipe> searchRecipes(String search, User user) {
        return recipeRepository.findByNameContainingIgnoreCaseAndUserEquals(search, user);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

}
