package no.hvl.dat251.recipeapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.hvl.dat251.recipeapp.enums.QUANTITY_UNIT;
import no.hvl.dat251.recipeapp.jackson.IdSerialization;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecipeItem implements ObjectWithId {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRecipeItem")
    @SequenceGenerator(name = "seqRecipeItem", sequenceName = "recipe_item_id_seq", allocationSize = 1)
    @JsonIgnore
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "recipe_item_recipe_id_fk"))
    @JsonIgnore
    private Recipe recipe;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "recipe_item_grocery_id_fk"))
    @IdSerialization
    private Grocery grocery;

    @Column(length = 50)
    @Enumerated(value = EnumType.STRING)
    private QUANTITY_UNIT unit;

    private Double quantity;

}
