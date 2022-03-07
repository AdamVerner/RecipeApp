package no.hvl.dat251.recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.hvl.dat251.recipeapp.enums.QUANTITY_UNIT;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecipeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRecipeItem")
    @SequenceGenerator(name = "seqRecipeItem", sequenceName = "recipe_item_id_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "recipe_item_recipe_id_fk"))
    private Recipe recipe;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "recipe_item_grocery_id_fk"))
    private Grocery grocery;

    @Size(max = 50)
    @Column(length = 50)
    @Enumerated(value = EnumType.STRING)
    private QUANTITY_UNIT unit;

    private Double quantity;

}
