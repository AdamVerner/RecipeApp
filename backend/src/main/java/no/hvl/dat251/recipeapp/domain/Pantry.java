package no.hvl.dat251.recipeapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.hvl.dat251.recipeapp.enums.QUANTITY_UNIT;
import no.hvl.dat251.recipeapp.jackson.IdSerialization;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pantry implements ObjectWithId {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPantry")
    @SequenceGenerator(name = "seqPantry", sequenceName = "pantry_id_seq", allocationSize = 1)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "pantry_customer_id_fk"))
    @JsonIgnore
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "pantry_grocery_id_fk"))
    @IdSerialization
    private Grocery grocery;

    @Column(length = 50)
    @Enumerated(value = EnumType.STRING)
    private QUANTITY_UNIT unit;

    private Double quantity;

    private Instant expiration;

}
