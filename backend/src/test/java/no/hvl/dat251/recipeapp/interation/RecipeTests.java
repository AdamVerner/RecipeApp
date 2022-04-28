package no.hvl.dat251.recipeapp.interation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat251.recipeapp.TestService;
import no.hvl.dat251.recipeapp.domain.Grocery;
import no.hvl.dat251.recipeapp.domain.Recipe;
import no.hvl.dat251.recipeapp.domain.RecipeItem;
import no.hvl.dat251.recipeapp.enums.GROCERY_CATEGORY;
import no.hvl.dat251.recipeapp.enums.QUANTITY_UNIT;
import no.hvl.dat251.recipeapp.service.GroceryService;
import no.hvl.dat251.recipeapp.service.RecipeService;
import no.hvl.dat251.recipeapp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RecipeTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestService testService;

	@Autowired
	private UserService userService;

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private GroceryService groceryService;

	private Grocery grocery;
	private Recipe recipe;

	@BeforeEach
	public void recipeInitialization() {
		if(grocery == null) {
			grocery = new Grocery();
			grocery.setCategory(GROCERY_CATEGORY.VEGETABLE);
			grocery.setName("potato");
			grocery = groceryService.saveGrocery(grocery);
		}

		if(recipe == null) {
			recipe = new Recipe();
			recipe.setUser(userService.getUserByEmail(TestService.TEST_USER));
			recipe.setName("Baked potatoes");
			recipe.setPortions(2);
			recipe.getItems().add(new RecipeItem(null, null, grocery, QUANTITY_UNIT.KILOGRAM, 0.5));
			recipe = recipeService.saveRecipe(recipe);
		}
	}

	@Test
	public void listRecipeTest() throws Exception {
		String token = testService.generateToken();

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/recipes-all")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Recipe> returnedRecipes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		Assertions.assertTrue(returnedRecipes.stream().anyMatch(r ->
				Objects.equals(r.getName(), recipe.getName()) && Objects.equals(r.getPortions(), recipe.getPortions())
		));
	}

}
