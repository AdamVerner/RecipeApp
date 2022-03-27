package no.hvl.dat251.recipeapp.domain;

import java.util.Objects;

public interface ObjectWithId {
    Integer getId();
    void setId(Integer id);

    default boolean equalsById(ObjectWithId other) {
        return this == other || Objects.equals(this.getId(), other.getId());
    }
}
