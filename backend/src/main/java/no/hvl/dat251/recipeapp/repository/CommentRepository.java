package no.hvl.dat251.recipeapp.repository;

import no.hvl.dat251.recipeapp.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByRecipeIdOrderByCreated(Integer recipeId);
}
