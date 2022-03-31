package no.hvl.dat251.recipeapp.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QUANTITY_UNIT {
    GRAM("grams"),
    KILOGRAM("kilograms"),
    MILLILITER("milliliters"),
    LITER("liters"),
    PIECE("pieces"),
    BOTTLE("bottles"),
    CUP("cups"),
    TABLE_SPOON("table spoons"),
    TEA_SPOON("tea spoons"),
    PINCH("pinches");

    private final String label;

    QUANTITY_UNIT(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
