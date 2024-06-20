package com.sopha.app.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopha.app.repository.LigneCommandeRepository;

import java.util.List;

@Service
public class LigneCommandeService {
    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    public List<Object[]> getTopSellingProducts() {
        return ligneCommandeRepository.findTopSellingProducts();
    }
}