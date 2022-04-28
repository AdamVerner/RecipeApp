package no.hvl.dat251.recipeapp.interation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat251.recipeapp.TestService;
import no.hvl.dat251.recipeapp.domain.Grocery;
import no.hvl.dat251.recipeapp.enums.GROCERY_CATEGORY;
import no.hvl.dat251.recipeapp.enums.QUANTITY_UNIT;
import org.junit.jupiter.api.Assertions;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GroceryTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestService testService;


	@Test
	public void getGroceryCategoriesTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/grocery-categories")
				.header(TestService.AUTHORIZATION_HEADER, testService.generateToken()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<GROCERY_CATEGORY> categories = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
		Assertions.assertEquals(Arrays.asList(GROCERY_CATEGORY.values()), categories);
	}

	@Test
	public void getQuantityUnitsTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/quantity-units")
				.header(TestService.AUTHORIZATION_HEADER, testService.generateToken()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<QUANTITY_UNIT> categories = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
		Assertions.assertEquals(Arrays.asList(QUANTITY_UNIT.values()), categories);
	}

	@Test
	public void saveGroceryTest() throws Exception {
		GROCERY_CATEGORY groceryCategory = GROCERY_CATEGORY.FRUIT;
		String groceryName = "apple";

		Grocery grocery = new Grocery();
		grocery.setCategory(groceryCategory);
		grocery.setName(groceryName);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/groceries")
				.header(TestService.AUTHORIZATION_HEADER, testService.generateToken()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Grocery> returnedGroceries = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		mockMvc.perform(MockMvcRequestBuilders
				.post("/grocery")
				.header(TestService.AUTHORIZATION_HEADER, testService.generateToken())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(grocery)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/groceries")
				.header(TestService.AUTHORIZATION_HEADER, testService.generateToken()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Grocery> returnedGroceries2 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		Assertions.assertEquals(returnedGroceries.size() + 1, returnedGroceries2.size());
		Assertions.assertFalse(returnedGroceries.stream().anyMatch(g -> Objects.equals(g.getName(), groceryName) && Objects.equals(g.getCategory(), groceryCategory)));
		Assertions.assertTrue(returnedGroceries2.stream().anyMatch(g -> Objects.equals(g.getName(), groceryName) && Objects.equals(g.getCategory(), groceryCategory)));
	}

}
