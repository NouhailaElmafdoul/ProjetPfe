package com.sopha.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopha.app.repository.PanierRepository;

@Service
public class PanierService {
	@Autowired
    private PanierRepository panierRepository;

    public void supprimerProduitDuPanier(Long produitId) {
        panierRepository.deleteById(produitId); 
    }

}
