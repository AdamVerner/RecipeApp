package no.hvl.dat251.recipeapp.service;

import lombok.extern.slf4j.Slf4j;
import no.hvl.dat251.recipeapp.domain.*;
import no.hvl.dat251.recipeapp.repository.CommentRepository;
import no.hvl.dat251.recipeapp.repository.RatingRepository;
import no.hvl.dat251.recipeapp.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
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

    public List<Recipe> getSuggestedRecipes(User user) {
        //String hql = "select distinct r from Recipe r left join r.items ri where r.user = :user and ri.grocery in (select distinct p.grocery from Pantry p where p.user = :user)";
        //Session session = sessionFactory.openSession();
        //List<Recipe> recipes = session.createQuery(hql, Recipe.class).setParameter("user", user).list();
        return getAllRecipes()
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
                .collect(Collectors.toList());
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
