package no.hvl.dat251.recipeapp.interation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat251.recipeapp.TestService;
import no.hvl.dat251.recipeapp.domain.*;
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
import org.springframework.http.MediaType;
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
	public void listAllRecipesTest() throws Exception {
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

	@Test
	public void listUsersRecipesTest() throws Exception {
		String token = testService.generateToken();

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/recipes")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Recipe> returnedRecipes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		Assertions.assertTrue(returnedRecipes.stream().anyMatch(r ->
				Objects.equals(r.getName(), recipe.getName()) && Objects.equals(r.getPortions(), recipe.getPortions())
		));
	}

	@Test
	public void getRecipeTest() throws Exception {
		String token = testService.generateToken();

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/recipe/" + recipe.getId())
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		Recipe returnedRecipe = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Recipe.class);

		Assertions.assertTrue(Objects.equals(returnedRecipe.getName(), recipe.getName()) && Objects.equals(returnedRecipe.getPortions(), recipe.getPortions()));
	}

	@Test
	public void saveRecipeTest() throws Exception {
		String token = testService.generateToken();

		Recipe newRecipe = new Recipe();
		newRecipe.setName("Boiled potatoes");
		newRecipe.setPortions(5);
		newRecipe.getItems().add(new RecipeItem(null, null, grocery, QUANTITY_UNIT.GRAM, 1000d));

		mockMvc.perform(MockMvcRequestBuilders
				.post("/recipe")
				.header(TestService.AUTHORIZATION_HEADER, token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(newRecipe)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/recipes")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Recipe> returnedRecipes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		Assertions.assertTrue(returnedRecipes.stream().anyMatch(r ->
				Objects.equals(r.getName(), newRecipe.getName()) && Objects.equals(r.getPortions(), newRecipe.getPortions())
		));
	}

	@Test
	public void commentRecipeTest() throws Exception {
		String token = testService.generateToken();

		Comment comment = new Comment(null, recipe, null, "Good recipe!", null);

		mockMvc.perform(MockMvcRequestBuilders
				.post("/comment")
				.header(TestService.AUTHORIZATION_HEADER, token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(comment)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/comments/" + recipe.getId())
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Comment> returnedComments = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		Assertions.assertEquals(1, returnedComments.size());
		Assertions.assertTrue(returnedComments.stream().anyMatch(c -> Objects.equals(c.getText(), comment.getText())));
	}

	@Test
	public void rateRecipeTest() throws Exception {
		String token = testService.generateToken();

		int ratingValue = 5;
		Rating rating = new Rating(null, recipe, null, ratingValue);

		mockMvc.perform(MockMvcRequestBuilders
				.post("/rating")
				.header(TestService.AUTHORIZATION_HEADER, token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(rating)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/recipe/" + recipe.getId())
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		Recipe returnedRecipe = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Recipe.class);

		Assertions.assertEquals(ratingValue, returnedRecipe.getCurrentUserRating());
		Assertions.assertEquals(ratingValue, returnedRecipe.getAverageRating());
	}

}
