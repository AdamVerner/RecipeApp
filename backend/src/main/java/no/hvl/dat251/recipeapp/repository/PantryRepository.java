package no.hvl.dat251.recipeapp.repository;

import no.hvl.dat251.recipeapp.domain.Pantry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PantryRepository extends JpaRepository<Pantry, Integer> {}
