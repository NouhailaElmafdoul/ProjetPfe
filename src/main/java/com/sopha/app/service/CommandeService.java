package com.sopha.app.service;

import com.sopha.app.models.Commande;
import com.sopha.app.models.LigneCommande;
import com.sopha.app.models.Panier;
import com.sopha.app.models.VenteJour;
import com.sopha.app.models.Produit;
import com.sopha.app.models.User;
import com.sopha.app.repository.CommandeRepository;
import com.sopha.app.repository.LigneCommandeRepository;
import com.sopha.app.repository.PanierRepository;
import com.sopha.app.repository.VenteJourRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private VenteJourRepository venteJourRepository;

    public Commande passerCommande(User user, Double totalAmount) {
        Commande commande = new Commande();
        commande.setUser(user);
        commande.setTotal(totalAmount);
        commande.setDateCommande(new Date());
        commande.setStatus("En attente");
        commandeRepository.save(commande);

        // Ajouter les produits du panier à la commande
        List<Panier> panierList = panierRepository.findByUserId(user.getId());
        for (Panier panier : panierList) {
            LigneCommande ligneCommande = new LigneCommande();
            ligneCommande.setProduit(panier.getProduit());
            ligneCommande.setQuantite(panier.getQuantite());
            ligneCommande.setPrix(panier.getPrixTotal());
            ligneCommande.setCommande(commande);
            ligneCommandeRepository.save(ligneCommande);
        }

        // Vider le panier de l'utilisateur
        panierRepository.deleteAll(panierList);

        return commande;
    }

    public List<Commande> getCommandesByUser(User user) {
        return commandeRepository.findByUserIdWithProducts(user.getId());
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAllWithProducts();
    }

    public List<Commande> getCommandesEnAttente() {
        return commandeRepository.findCommandesByStatus("En attente");
    }

    @Transactional
    public Commande updateCommandeStatus(Long commandeId, String newStatus) {
        Optional<Commande> optionalCommande = commandeRepository.findById(commandeId);
        if (optionalCommande.isPresent()) {
            Commande commande = optionalCommande.get();
            commande.setStatus(newStatus);

            // Charger explicitement les lignes de commande
            commande.getLignesCommande().size();

            // Si la commande est approuvée, mettre à jour les ventes journalières
            if ("APPROUVÉ".equals(newStatus)) {
                updateDailySales(commande);
            }

            return commandeRepository.save(commande);
        }
        return null;
    }

    private void updateDailySales(Commande commande) {
        Date today = new Date();
        VenteJour venteJour = venteJourRepository.findByDate(today);
        if (venteJour == null) {
            venteJour = new VenteJour(today, 0.0);
        }

        double totalCommande = commande.getLignesCommande().stream()
                .mapToDouble(lc -> lc.getQuantite() * (lc.getProduit().getPrixVente() - lc.getProduit().getPrix()))
                .sum();

        venteJour.setTotalVentes(venteJour.getTotalVentes() + totalCommande);
        venteJourRepository.save(venteJour);
    }

    public List<Object[]> getTopClients() {
        return commandeRepository.findTopClients();
    }

    public List<Object[]> getTopSellingProducts() {
        return commandeRepository.findTopSellingProducts();
    }

    @Transactional
    public Double getTotalGainsApprovedOrders() {
        List<Commande> approvedOrders = commandeRepository.findByStatus("APPROUVÉ");
        double totalGains = 0.0;
        for (Commande commande : approvedOrders) {
            for (LigneCommande ligneCommande : commande.getLignesCommande()) {
                double prixVente = ligneCommande.getProduit().getPrixVente();
                double prixAchat = ligneCommande.getProduit().getPrix();
                int quantite = ligneCommande.getQuantite();
                totalGains += (prixVente - prixAchat) * quantite;
            }
        }
        return totalGains;
    }
}
