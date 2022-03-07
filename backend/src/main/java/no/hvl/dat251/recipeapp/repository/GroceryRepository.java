package no.hvl.dat251.recipeapp.repository;

import no.hvl.dat251.recipeapp.domain.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryRepository extends JpaRepository<Grocery, Integer> {}
