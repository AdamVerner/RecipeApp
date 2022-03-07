package no.hvl.dat251.recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.hvl.dat251.recipeapp.enums.GROCERY_CATEGORY;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grocery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGrocery")
    @SequenceGenerator(name = "seqGrocery", sequenceName = "grocery_id_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    @Enumerated(value = EnumType.STRING)
    private GROCERY_CATEGORY category;

}
