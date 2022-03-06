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
@Table(name = "customer", indexes = { @Index(name = "customer_id_uindex", columnList = "id", unique = true), @Index(name = "customer_email_uindex", columnList = "email", unique = true) })
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

}
