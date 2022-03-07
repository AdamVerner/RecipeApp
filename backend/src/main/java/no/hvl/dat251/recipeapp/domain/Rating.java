package no.hvl.dat251.recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRating")
    @SequenceGenerator(name = "seqRating", sequenceName = "rating_id_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "rating_recipe_id_fk"))
    private Recipe recipe;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer", nullable = false, foreignKey = @ForeignKey(name = "rating_customer_id_fk"))
    private User user;

    private int rating;

}
