package no.hvl.dat251.recipeapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.hvl.dat251.recipeapp.jackson.IdSerialization;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment implements ObjectWithId {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqComment")
    @SequenceGenerator(name = "seqComment", sequenceName = "comment_id_seq", allocationSize = 1)
    @JsonIgnore
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "comment_recipe_id_fk"))
    @IdSerialization
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Recipe recipe;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "comment_customer_id_fk"))
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;

    @Size(max = 10000)
    @Column(length = 10000)
    private String text;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant created;

}
