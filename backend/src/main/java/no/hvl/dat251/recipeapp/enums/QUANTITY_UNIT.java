package no.hvl.dat251.recipeapp.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QUANTITY_UNIT {
    GRAM("gram"),
    KILOGRAM("kilogram"),
    MILLILITER("milliliter"),
    LITER("liter"),
    PIECE("piece"),
    BOTTLE("bottle");

    private final String label;

    QUANTITY_UNIT(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
