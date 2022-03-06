package no.hvl.dat251.recipeapp.repository;

import no.hvl.dat251.recipeapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getByEmail(String email);
}
