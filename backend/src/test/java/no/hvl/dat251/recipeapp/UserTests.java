package no.hvl.dat251.recipeapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat251.recipeapp.domain.User;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestService testService;


	@Test
	void getUserTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/user")
				.header("Authorization", testService.generateToken()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		User user = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
		Assertions.assertEquals("test@email.com", user.getEmail());
		Assertions.assertEquals("password", user.getPassword());
		Assertions.assertEquals("Test", user.getFirstName());
		Assertions.assertEquals("Test", user.getLastName());
	}

	@Test
	void userCreationTest() throws Exception {
		User user = new User(null, "email@email.com", "password", "First", "Last");

		mockMvc.perform(MockMvcRequestBuilders
				.post("/user")
				.header("Authorization", testService.generateToken())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(user)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/user")
				.header("Authorization", testService.generateToken("email@email.com")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		User returnedUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
		Assertions.assertEquals("email@email.com", returnedUser.getEmail());
		Assertions.assertEquals("First", returnedUser.getFirstName());
		Assertions.assertEquals("Last", returnedUser.getLastName());
	}

}
