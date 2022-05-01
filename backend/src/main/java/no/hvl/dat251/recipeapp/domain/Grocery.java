package no.hvl.dat251.recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.hvl.dat251.recipeapp.enums.GROCERY_CATEGORY;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grocery implements ObjectWithId {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGrocery")
    @SequenceGenerator(name = "seqGrocery", sequenceName = "grocery_id_seq", allocationSize = 1)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    @Column(length = 50)
    @Enumerated(value = EnumType.STRING)
    private GROCERY_CATEGORY category;

}
