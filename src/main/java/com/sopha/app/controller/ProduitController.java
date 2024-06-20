package com.sopha.app.controller;


import com.sopha.app.models.Produit;
import com.sopha.app.models.ProduitPerimer;
import com.sopha.app.models.User;
import com.sopha.app.repository.ProduitPerimerRepository;
import com.sopha.app.repository.ProduitRepository;
import com.sopha.app.service.PanierService;
import com.sopha.app.service.ProduitService;
import com.sopha.app.service.UserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ProduitController {

    @Autowired
    private ProduitPerimerRepository produitPerimerRepository;
    
    @Autowired
    private ProduitRepository produitRepository;

    @GetMapping("/produitsPerimes")
    public String afficherProduitsPerimes(Model model) {
        List<ProduitPerimer> produitsPerimes = produitPerimerRepository.findAll();
        model.addAttribute("produitsPerimes", produitsPerimes);
        return "vue/produits_perimes";
    }

    @GetMapping("/produitsTermines")
    public String afficherProduitsTerminés(Model model) {
        List<Produit> produitsTermines = produitRepository.findByQuantite(0);
        model.addAttribute("produitsTermines", produitsTermines);

        return "vue/produits_termines";
    }
    
    
    @GetMapping("/produits")
    public String afficherProduitsDisponibles(Model model) {
        List<Produit> produits = produitRepository.findAllOrderByIdDesc();
        List<Produit> produitsAvecImagesBase64 = produits.stream().map(produit -> {
            if (produit.getImage() != null) {
                String imageBase64 = Base64.getEncoder().encodeToString(produit.getImage());
                produit.setImageBase64(imageBase64);
            }
            return produit;
        }).collect(Collectors.toList());
        model.addAttribute("produits", produitsAvecImagesBase64);
        return "vue/produits";
    }
   
   
}
