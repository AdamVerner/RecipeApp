package no.hvl.dat251.recipeapp.unit;

import no.hvl.dat251.recipeapp.TestService;
import no.hvl.dat251.recipeapp.domain.Grocery;
import no.hvl.dat251.recipeapp.domain.Pantry;
import no.hvl.dat251.recipeapp.domain.User;
import no.hvl.dat251.recipeapp.enums.GROCERY_CATEGORY;
import no.hvl.dat251.recipeapp.enums.QUANTITY_UNIT;
import no.hvl.dat251.recipeapp.service.GroceryService;
import no.hvl.dat251.recipeapp.service.PantryService;
import no.hvl.dat251.recipeapp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class PantryServiceTests {

	@Autowired
	private UserService userService;

	@Autowired
	private PantryService pantryService;

	@Autowired
	private GroceryService groceryService;

	private static final String groceryName = "milk";
	private static final GROCERY_CATEGORY groceryCategory = GROCERY_CATEGORY.MILK_PRODUCT;
	private static final QUANTITY_UNIT quantityUnit = QUANTITY_UNIT.LITER;
	private static final double quantity = 1.75;

	private Integer pantryId;
	private Grocery grocery;

	@BeforeEach
	public void initializePantry() {
		if(pantryId == null) {
			grocery = new Grocery();
			grocery.setName(groceryName);
			grocery.setCategory(groceryCategory);
			groceryService.saveGrocery(grocery);

			Pantry pantry = new Pantry();
			pantry.setUser(userService.getUserByEmail(TestService.TEST_USER));
			pantry.setGrocery(grocery);
			pantry.setUnit(quantityUnit);
			pantry.setQuantity(quantity);
			pantry.setExpiration(Instant.now());
			pantryId = pantryService.savePantry(pantry).getId();
		}
	}

	@Test
	public void getPantryTest() {
		Pantry pantry = pantryService.getPantry(pantryId);
		Assertions.assertNotNull(pantry);
		Assertions.assertEquals(pantryId, pantry.getId());
		Assertions.assertEquals(groceryName, pantry.getGrocery().getName());
		Assertions.assertEquals(groceryCategory, pantry.getGrocery().getCategory());
		Assertions.assertEquals(quantityUnit, pantry.getUnit());
		Assertions.assertEquals(quantity, pantry.getQuantity());
	}

	@Test
	public void savePantryTest() {
		Pantry pantry = new Pantry();
		pantry.setUser(userService.getUserByEmail(TestService.TEST_USER));
		pantry.setGrocery(grocery);
		pantry.setUnit(quantityUnit);
		pantry.setQuantity(quantity);
		pantry.setExpiration(Instant.now());
		pantry = pantryService.savePantry(pantry);

		Assertions.assertNotNull(pantry.getId());
		Assertions.assertEquals(groceryName, pantry.getGrocery().getName());
		Assertions.assertEquals(groceryCategory, pantry.getGrocery().getCategory());
		Assertions.assertEquals(quantityUnit, pantry.getUnit());
		Assertions.assertEquals(quantity, pantry.getQuantity());
	}

	@Test
	public void searchPantryTest() {
		User user = userService.getUserByEmail(TestService.TEST_USER);
		List<Pantry> pantries = pantryService.searchPantry("xxx", user);
		Assertions.assertEquals(0, pantries.size());

		pantries = pantryService.searchPantry(groceryName, user);
		Assertions.assertTrue(pantries.size() > 0);
	}

}
