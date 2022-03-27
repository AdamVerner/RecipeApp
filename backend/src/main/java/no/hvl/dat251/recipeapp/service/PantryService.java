package no.hvl.dat251.recipeapp.service;

import no.hvl.dat251.recipeapp.domain.Pantry;
import no.hvl.dat251.recipeapp.domain.User;
import no.hvl.dat251.recipeapp.repository.PantryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PantryService {

    @Autowired
    private PantryRepository pantryRepository;

    @Autowired
    private SessionFactory sessionFactory;


    public Pantry getPantry(Integer id) {
        return pantryRepository.findById(id).orElse(null);
    }

    public Pantry savePantry(Pantry pantry) {
        return pantryRepository.save(pantry);
    }

    public List<Pantry> searchPantry(String search, User user) {
        String hql = "select pantry from Pantry pantry, Grocery grocery where pantry.user = :user and lower(grocery.name) like lower(:search)";
        Session session = sessionFactory.openSession();
        return session.createQuery(hql, Pantry.class).setParameter("user", user).setParameter("search", "%" + search + "%").list();
    }

    public void deletePantry(Pantry pantry) {
        pantryRepository.delete(pantry);
    }

}
