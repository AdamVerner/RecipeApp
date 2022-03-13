package no.hvl.dat251.recipeapp.repository;

import no.hvl.dat251.recipeapp.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {}
