package no.hvl.dat251.recipeapp.unit;

import no.hvl.dat251.recipeapp.domain.Grocery;
import no.hvl.dat251.recipeapp.enums.GROCERY_CATEGORY;
import no.hvl.dat251.recipeapp.service.GroceryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

@SpringBootTest
@ActiveProfiles("test")
public class GroceryServiceTests {

	@Autowired
	private GroceryService groceryService;

	private static final String groceryName = "beef";
	private static final GROCERY_CATEGORY groceryCategory = GROCERY_CATEGORY.MEAT;

	private Grocery grocery;

	@BeforeEach
	public void initializePantry() {
		if(grocery == null) {
			grocery = new Grocery();
			grocery.setName(groceryName);
			grocery.setCategory(groceryCategory);
			groceryService.saveGrocery(grocery);
		}
	}

	@Test
	public void getGroceriesTest() {
		List<Grocery> groceries = groceryService.getGroceries();

		Assertions.assertTrue(groceries.size() > 0);
		Assertions.assertTrue(groceries.stream().anyMatch(g -> Objects.equals(groceryName, g.getName()) && Objects.equals(groceryCategory, g.getCategory())));
	}

	@Test
	public void saveGroceryTest() {
		Grocery grocery = new Grocery();
		grocery.setName(groceryName);
		grocery.setCategory(groceryCategory);
		grocery = groceryService.saveGrocery(grocery);

		Assertions.assertNotNull(grocery.getId());
		Assertions.assertEquals(groceryName, grocery.getName());
		Assertions.assertEquals(groceryCategory, grocery.getCategory());
	}

	@Test
	public void searchGroceriesTest() {
		List<Grocery> groceries = groceryService.searchGroceries("xxx");
		Assertions.assertEquals(0, groceries.size());

		groceries = groceryService.searchGroceries(groceryName);
		Assertions.assertTrue(groceries.size() > 0);
	}

}
