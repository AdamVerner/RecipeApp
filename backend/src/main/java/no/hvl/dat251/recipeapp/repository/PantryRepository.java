package no.hvl.dat251.recipeapp.repository;

import no.hvl.dat251.recipeapp.domain.Pantry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface PantryRepository extends JpaRepository<Pantry, Integer> {}
