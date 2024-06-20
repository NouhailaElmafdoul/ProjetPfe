package com.sopha.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sopha.app.service.ProduitService;
import com.sopha.app.models.Panier;
import com.sopha.app.models.Produit;
import com.sopha.app.models.User;
import com.sopha.app.repository.PanierRepository;
import com.sopha.app.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PanierController {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/ajouterAuPanier")
    public String ajouterAuPanier(@RequestParam("produitId") Long produitId, @RequestParam("quantite") int quantite, Model model) {
        produitService.ajouterAuPanier(produitId, quantite);
        return "redirect:/produits";
    }

    @GetMapping("/panier")
    public String afficherPanier(Model model) {
        String email = getAuthenticatedUserEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        List<Panier> produitsPanier = panierRepository.findByUserId(user.getId());
        produitsPanier.forEach(panier -> {
            Produit produit = panier.getProduit();
            if (produit.getImage() != null) {
                String imageBase64 = Base64.getEncoder().encodeToString(produit.getImage());
                produit.setImageBase64(imageBase64);
            }
        });
        model.addAttribute("produitsPanier", produitsPanier);
        return "vue/panier"; 
    }


    private String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    
    @PostMapping("/supprimerProduitPanier")
    @ResponseBody
    public Map<String, Object> supprimerProduitPanier(@RequestParam("id") Long panierId) {
        Map<String, Object> response = new HashMap<>();
        try {
            produitService.supprimerProduitDuPanier(panierId);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

}
