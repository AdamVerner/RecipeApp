package no.hvl.dat251.recipeapp.repository;

import no.hvl.dat251.recipeapp.domain.Recipe;
import no.hvl.dat251.recipeapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByNameContainingIgnoreCase(String search);
    List<Recipe> findByNameContainingIgnoreCaseAndUserEquals(String search, User user);
}
