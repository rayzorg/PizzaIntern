package com.example.internproject.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.internproject.models.Topping;
import com.example.internproject.repository.ToppingRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ToppingService {

    private final ToppingRepository toppingRepository;

    public ToppingService(ToppingRepository toppingRepository) {
        this.toppingRepository = toppingRepository;
    }

    public Topping addTopping(Topping topping) { return toppingRepository.save(topping); }
    
    public List<Topping> getAll() {
        return toppingRepository.findAll();
    }
}
