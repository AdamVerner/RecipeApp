package no.hvl.dat251.recipeapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.hvl.dat251.recipeapp.jackson.IdSerialization;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rating implements ObjectWithId {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRating")
    @SequenceGenerator(name = "seqRating", sequenceName = "rating_id_seq", allocationSize = 1)
    @JsonIgnore
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "rating_recipe_id_fk"))
    @IdSerialization
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Recipe recipe;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer", nullable = false, foreignKey = @ForeignKey(name = "rating_customer_id_fk"))
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;

    private int rating;

}
