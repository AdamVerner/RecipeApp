package no.hvl.dat251.recipeapp.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GROCERY_CATEGORY {
    BEVERAGE("Beverage"),
    ALCOHOLIC_DRINK("Alcoholic drink"),
    FRUIT("Fruit"),
    VEGETABLE("Vegetable"),
    MEAT("Meat"),
    BAKERY("Bakery"),
    CANDY("Candy"),
    SNACK("Snack"),
    PASTA("Pasta"),
    MILK_PRODUCT("Milk products"),
    FOOD_FOR_ANIMALS("Food for animals"),
    SPICE("Spice"),
    SAUCE("Sauce"),
    SIDE_DISH("Side dish"),
    OTHER("Other");

    private final String label;

    GROCERY_CATEGORY(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
