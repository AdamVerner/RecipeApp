package no.hvl.dat251.recipeapp.unit;

import no.hvl.dat251.recipeapp.TestService;
import no.hvl.dat251.recipeapp.domain.User;
import no.hvl.dat251.recipeapp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {

	@Autowired
	private UserService userService;

	@Test
	public void getUserTest() {
		User user = userService.getUserByEmail(TestService.TEST_USER);
		Assertions.assertNotNull(user);
		Assertions.assertEquals(TestService.TEST_USER, user.getEmail());
	}

}
