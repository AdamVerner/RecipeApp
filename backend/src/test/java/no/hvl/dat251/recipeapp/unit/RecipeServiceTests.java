package no.hvl.dat251.recipeapp.unit;

import no.hvl.dat251.recipeapp.TestService;
import no.hvl.dat251.recipeapp.domain.Recipe;
import no.hvl.dat251.recipeapp.service.RecipeService;
import no.hvl.dat251.recipeapp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeServiceTests {

	@Autowired
	private UserService userService;

	@Autowired
	private RecipeService recipeService;

	@Test
	public void saveRecipeTest() {
		Recipe recipe = new Recipe();
		recipe.setUser(userService.getUserByEmail(TestService.TEST_USER));
		recipe.setName("Recipe");
		recipe.setPortions(5);
		Recipe savedRecipe = recipeService.saveRecipe(recipe);

		Assertions.assertNotNull(savedRecipe.getId());
		Assertions.assertNotNull(savedRecipe.getCreated());
	}

}
