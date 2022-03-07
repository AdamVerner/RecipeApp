package no.hvl.dat251.recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer", indexes = @Index(name = "customer_email_uindex", columnList = "email", unique = true))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCustomer")
    @SequenceGenerator(name = "seqCustomer", sequenceName = "customer_id_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Recipe> recipes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

}
