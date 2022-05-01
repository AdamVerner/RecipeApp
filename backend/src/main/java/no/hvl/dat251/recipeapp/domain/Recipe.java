package no.hvl.dat251.recipeapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
public class Recipe implements ObjectWithId {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRecipe")
    @SequenceGenerator(name = "seqRecipe", sequenceName = "recipe_id_seq", allocationSize = 1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "recipe_customer_id_fk"))
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;

    private String name;

    private Integer portions;

    @Size(max = 10000)
    @Column(length = 10000)
    private String instructions;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant created;

    @Lob
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] image;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private transient String imageUrl;

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private transient double averageRating;

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private transient int currentUserRating;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<RecipeItem> items = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

}
