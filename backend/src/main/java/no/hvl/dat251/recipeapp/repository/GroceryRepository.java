package no.hvl.dat251.recipeapp.repository;

import no.hvl.dat251.recipeapp.domain.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface GroceryRepository extends JpaRepository<Grocery, Integer> {
    List<Grocery> findByNameContainingIgnoreCase(String search);
}
