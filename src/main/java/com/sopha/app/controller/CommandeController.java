package com.sopha.app.controller;

import com.sopha.app.models.Commande;
import com.sopha.app.models.Notification;
import com.sopha.app.models.User;
import com.sopha.app.repository.CommandeRepository;
import com.sopha.app.repository.LigneCommandeRepository;
import com.sopha.app.service.CommandeService;
import com.sopha.app.service.NotificationService;
import com.sopha.app.service.UserDetailService;


import com.sopha.app.models.LigneCommande;
import com.sopha.app.models.Produit;
import com.sopha.app.service.ProduitService;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private UserDetailService userDetailService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ProduitService produitService;

    @PostMapping("/passerCommande")
    public String passerCommande(@RequestParam("totalAmount") Double totalAmount,
                                 @RequestParam("cardNumber") String cardNumber,
                                 @RequestParam("verificationCode") String verificationCode,
                                 RedirectAttributes redirectAttributes) {
        User user = userDetailService.getCurrentAuthenticatedUser();
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Erreur : utilisateur non authentifié");
            return "redirect:/panier";
        }

        Commande commande = commandeService.passerCommande(user, totalAmount);
        redirectAttributes.addFlashAttribute("message", "Commande passée avec succès !");
        return "redirect:/panier";
    }
    
    @GetMapping("/mesCommandes")
    public String getMesCommandes(Model model) {
        User user = userDetailService.getCurrentAuthenticatedUser();
        if (user == null) {
            return "redirect:/login"; // ou une autre page de votre choix
        }

        List<Commande> commandes = commandeService.getCommandesByUser(user);
        model.addAttribute("commandes", commandes);
        return "vue/mesCommandes";
    }

    // Afficher toutes les commandes (pour le responsable)
    @GetMapping("/toutesLesCommandes")
    public String toutesLesCommandes(Model model) {
        List<Commande> commandes = commandeService.getCommandesEnAttente();
        model.addAttribute("commandes", commandes);
        return "vue/toutesLesCommandes";
    }

    
    @PostMapping("/updateCommandeStatus")
    public String updateCommandeStatus(@RequestParam("commandeId") Long commandeId,
                                       @RequestParam("status") String status,
                                       RedirectAttributes redirectAttributes) {
        Commande commande = commandeService.updateCommandeStatus(commandeId, status);
        if (commande != null) {
            // Créer une notification
            Notification notification = new Notification();
            User user = commande.getUser(); // Récupérer l'utilisateur associé à la commande
            notification.setUser(user);
            notification.setDate(new Date());

            String message;
            if ("APPROUVÉ".equals(status)) {
                message = "Votre commande (ID: " + commandeId + ") a été approuvée. Date de livraison: " + getDateDeLivraison(commande);
                notification.setMessage(message);
                redirectAttributes.addFlashAttribute("successMessage", message);

                
                // Soustraire les quantités des produits commandés
                soustraireQuantitesProduits(commande);
                
            } else if ("REFUSÉ".equals(status)) {
                message = "Votre commande (ID: " + commandeId + ") a été refusée.";
                notification.setMessage(message);
                redirectAttributes.addFlashAttribute("errorMessage", message);
            }

            notificationService.saveNotification(notification);
            redirectAttributes.addFlashAttribute("message", "Statut de la commande mis à jour avec succès !");
        } else {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour du statut de la commande.");
        }
        return "redirect:/toutesLesCommandes";
    }

    private void soustraireQuantitesProduits(Commande commande) {
        for (LigneCommande ligneCommande : commande. getLignesCommande()) {
            Produit produit = ligneCommande.getProduit();
            int nouvelleQuantite = produit.getQuantite() - ligneCommande.getQuantite();
            produit.setQuantite(nouvelleQuantite);
            produitService.save(produit);
        }
    }

    private String getDateDeLivraison(Commande commande) {
        Date dateLivraison = new Date(commande.getDateCommande().getTime() + (24 * 60 * 60 * 1000)); // ajouter un jour
        return dateLivraison.toString();
    }
    @GetMapping("/notifications")
    public String afficherNotifications(Model model) {
        User user = userDetailService.getCurrentAuthenticatedUser();
        if (user == null) {
            return "redirect:/login";
        }
        List<Notification> notifications = notificationService.getNotificationsByUser(user.getId());
        model.addAttribute("notifications", notifications);
        return "vue/notifications";
    }
    
    
    @DeleteMapping("/notifications/{id}")
    @ResponseBody
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "Notification supprimée";
    }
}
