package com.sopha.app.service;

import com.sopha.app.models.Panier;
import com.sopha.app.models.Produit;
import com.sopha.app.models.ProduitPerimer;
import com.sopha.app.models.User;
import com.sopha.app.repository.PanierRepository;
import com.sopha.app.repository.ProduitPerimerRepository;
import com.sopha.app.repository.ProduitRepository;
import com.sopha.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private ProduitPerimerRepository produitPerimerRepository;

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 86400000) // Vérifie tous les jours
    @Transactional
    public void transfererProduitsPerimes() {
        Date now = new Date();
        List<Produit> produits = produitRepository.findAll();

        for (Produit produit : produits) {
            if (produit.getDateDExpiration().before(now)) {
                ProduitPerimer produitPerimer = new ProduitPerimer(
                        produit.getNomProduit(),
                        produit.getDateDeFabrication(),
                        produit.getDateDExpiration(),
                        produit.getPrix(),
                        produit.getPrixVente(),
                        produit.getQuantite()
                );
                produitPerimerRepository.save(produitPerimer);
                produitRepository.delete(produit);
            }
        }
    }
    public void ajouterAuPanier(Long produitId, int quantite) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));

        // Récupérer l'utilisateur authentifié
        String email = getAuthenticatedUserEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }

        User user = optionalUser.get();
        Double prixTotal = produit.getPrix() * quantite;
        Panier panier = new Panier(produit, quantite, prixTotal, user);
        panierRepository.save(panier);
    }

    private String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    
    public void supprimerProduitDuPanier(Long panierId) {
        panierRepository.deleteById(panierId);
    }
    
    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }
}
