package com.sopha.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sopha.app.models.Produit;
import com.sopha.app.models.User;
import com.sopha.app.repository.ProduitRepository;
import com.sopha.app.service.CommandeService;
import com.sopha.app.service.LigneCommandeService;
import com.sopha.app.service.UserDetailService;


@EnableScheduling
@Controller
public class sophaController {
	
	@Autowired
	 private  ProduitRepository produitRepository; 
	
	@Autowired
    UserDetailService userDetailService;
		
	
	@Autowired
    private CommandeService commandeService;

    @Autowired
    private LigneCommandeService ligneCommandeService;

   
    
    
    @GetMapping("/admin")
    public String afficher(Model model) {
        User user = userDetailService.getCurrentAuthenticatedUser();
        model.addAttribute("user", user);

        
        List<Object[]> topClients = commandeService.getTopClients();
        List<Object[]> topSellingProducts = commandeService.getTopSellingProducts();
        Double totalGains = commandeService.getTotalGainsApprovedOrders();

        model.addAttribute("topClients", topClients);
        model.addAttribute("topSellingProducts", topSellingProducts);
        model.addAttribute("totalGains", totalGains);

        return "vue/AdminInterface";
    }


	 @GetMapping("/user")
	 public String afficherDaretUser(Model model) {
	     User user = userDetailService.getCurrentAuthenticatedUser();
	     model.addAttribute("user", user);

	     List<Produit> produits = produitRepository.findAllOrderByIdDesc();
	     List<Produit> produitsAvecImagesBase64 = produits.stream().map(produit -> {
	         if (produit.getImage() != null) {
	             String imageBase64 = Base64.getEncoder().encodeToString(produit.getImage());
	             produit.setImageBase64(imageBase64);
	         }
	         return produit;
	     }).collect(Collectors.toList());
	     model.addAttribute("produits", produitsAvecImagesBase64);

	     return "vue/UserInterface";
	 }

		
		
		@GetMapping("/creer_Produit")
		public String afficherProduits(Model model) {
			 List<Produit> listeProduits = produitRepository.findAllOrderByIdDesc();
			    model.addAttribute("produits", listeProduits);


		    return "vue/creer_Produit";
		}
		@GetMapping("/deleteProduit/{id}")
		public String supprimerProduit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		    try {
		        produitRepository.deleteById(id);
		        redirectAttributes.addFlashAttribute("message", "Produit supprimé avec succès!");
		    } catch (DataIntegrityViolationException e) {
		        redirectAttributes.addFlashAttribute("error", "Impossible de supprimer le produit car il est référencé par des commandes.");
		    }
		    return "redirect:/creer_Produit";
		}



		  @GetMapping("/ajouterProduit")
		  public String ajouterProduit(Model model){  
		      model.addAttribute("produit", new Produit());
		      return "vue/ajouter_produit";
		  }
		
		  
		  
		  @PostMapping("/ajouterProduit")
		    public String ajouterProduit(@ModelAttribute Produit produit, @RequestParam("imageFile") MultipartFile imageFile) {
		        if (!imageFile.isEmpty()) {
		            try {
		                byte[] imageBytes = imageFile.getBytes();
		                produit.setImage(imageBytes);
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }

		        produitRepository.save(produit);
		        return "redirect:/creer_Produit";
		    }
		  
		  
		  @GetMapping("/modifierProduit")
		    public String afficherModifierProduit(Model model) {
			  List<Produit> listeProduits = produitRepository.findAllOrderByIdDesc();
			    model.addAttribute("produits", listeProduits);

			    return "vue/modifierProduit";
		    }
		  @PostMapping("/validateProduit")
		  public String modifierProduit(
		          @RequestParam("id") Long id,
		          @RequestParam("nomProduit") String nomProduit,
		          @RequestParam("dateDeFabrication") @DateTimeFormat(pattern = "dd/MM/yyyy") Date dateDeFabrication,
		          @RequestParam("dateDExpiration") @DateTimeFormat(pattern = "dd/MM/yyyy") Date dateDExpiration,
		          @RequestParam("prix") Double prix,
		          @RequestParam("prixVente") Double prixVente,
		          @RequestParam("quantite") Integer quantite) {

		      Optional<Produit> optionalProduit = produitRepository.findById(id);
		      if (optionalProduit.isPresent()) {
		          Produit produit = optionalProduit.get();
		          produit.setNomProduit(nomProduit);
		          produit.setDateDeFabrication(dateDeFabrication);
		          produit.setDateDExpiration(dateDExpiration);
		          produit.setPrix(prix);
		          produit.setPrixVente(prixVente);
		          produit.setQuantite(quantite);
		          produitRepository.save(produit);
		      }

		      return "redirect:/modifierProduit";
		  }
}