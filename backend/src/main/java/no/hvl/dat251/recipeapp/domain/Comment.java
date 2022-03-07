package no.hvl.dat251.recipeapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqComment")
    @SequenceGenerator(name = "seqComment", sequenceName = "comment_id_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "comment_recipe_id_fk"))
    private Recipe recipe;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer", nullable = false, foreignKey = @ForeignKey(name = "comment_customer_id_fk"))
    private User user;

    @Size(max = 10000)
    @Column(length = 10000)
    private String text;

    private Instant created;

}
