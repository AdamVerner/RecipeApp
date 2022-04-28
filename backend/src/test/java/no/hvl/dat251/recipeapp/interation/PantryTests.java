package no.hvl.dat251.recipeapp.interation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat251.recipeapp.TestService;
import no.hvl.dat251.recipeapp.domain.Grocery;
import no.hvl.dat251.recipeapp.domain.Pantry;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PantryTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestService testService;

	@Test
	public void savePantryTest() throws Exception {
		String token = testService.generateToken();

		String groceryName = "onion";
		Grocery grocery = new Grocery();
		grocery.setCategory(GROCERY_CATEGORY.VEGETABLE);
		grocery.setName(groceryName);

		mockMvc.perform(MockMvcRequestBuilders
				.post("/grocery")
				.header(TestService.AUTHORIZATION_HEADER, token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(grocery)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/groceries")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Grocery> groceries = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
		grocery = groceries.stream().filter(g -> g.getName().equals(groceryName)).findFirst().orElse(null);

		Instant pantryExpiration = Instant.now().plus(10, ChronoUnit.DAYS);
		double pantryQuantity = 5;
		QUANTITY_UNIT pantryUnit = QUANTITY_UNIT.PIECE;

		Pantry pantry = new Pantry();
		pantry.setGrocery(grocery);
		pantry.setExpiration(pantryExpiration);
		pantry.setQuantity(pantryQuantity);
		pantry.setUnit(pantryUnit);

		mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/pantry")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Pantry> returnedPantry = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		mockMvc.perform(MockMvcRequestBuilders
				.post("/pantry")
				.header(TestService.AUTHORIZATION_HEADER, token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(pantry)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/pantry")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Pantry> returnedPantry2 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		Assertions.assertEquals(returnedPantry.size() + 1, returnedPantry2.size());
		Assertions.assertFalse(returnedPantry.stream().anyMatch(p ->
				Objects.equals(p.getGrocery().getName(), groceryName)
				&& Objects.equals(p.getQuantity(), pantryQuantity)
				&& Objects.equals(p.getUnit(), pantryUnit)
		));
		Assertions.assertTrue(returnedPantry2.stream().anyMatch(p ->
				Objects.equals(p.getGrocery().getName(), groceryName)
				&& Objects.equals(p.getQuantity(), pantryQuantity)
				&& Objects.equals(p.getUnit(), pantryUnit)
		));
	}

	@Test
	public void deletePantryTest() throws Exception {
		String token = testService.generateToken();

		String groceryName = "banana";
		Grocery grocery = new Grocery();
		grocery.setCategory(GROCERY_CATEGORY.FRUIT);
		grocery.setName(groceryName);

		mockMvc.perform(MockMvcRequestBuilders
				.post("/grocery")
				.header(TestService.AUTHORIZATION_HEADER, token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(grocery)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/groceries")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Grocery> groceries = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
		grocery = groceries.stream().filter(g -> g.getName().equals(groceryName)).findFirst().orElse(null);

		Pantry pantry = new Pantry();
		pantry.setGrocery(grocery);

		mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.post("/pantry")
				.header(TestService.AUTHORIZATION_HEADER, token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(pantry)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();
		Pantry returnedPantry = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Pantry.class);

		mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/pantry")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Pantry> returnedPantryList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		mockMvc.perform(MockMvcRequestBuilders
				.delete(String.format("/pantry/%d", returnedPantry.getId()))
				.header(TestService.AUTHORIZATION_HEADER, token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(pantry)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/pantry")
				.header(TestService.AUTHORIZATION_HEADER, token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		List<Pantry> returnedPantryList2 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

		Assertions.assertEquals(returnedPantryList.size() - 1, returnedPantryList2.size());
	}

}
