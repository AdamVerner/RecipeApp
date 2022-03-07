package no.hvl.dat251.recipeapp.service;

import no.hvl.dat251.recipeapp.domain.Grocery;
import no.hvl.dat251.recipeapp.repository.GroceryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroceryService {

    @Autowired
    private GroceryRepository repository;

    public Grocery saveGrocery(Grocery grocery) {
        return repository.save(grocery);
    }

    public List<Grocery> getGroceries() {
        return repository.findAll();
    }

}
