package no.hvl.dat251.recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRecipe")
    @SequenceGenerator(name = "seqRecipe", sequenceName = "recipe_id_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer", nullable = false, foreignKey = @ForeignKey(name = "recipe_customer_id_fk"))
    private User user;

    private String name;

    private Integer portions;

    @Size(max = 10000)
    @Column(length = 10000)
    private String instructions;

    private Instant created;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<RecipeItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

}
