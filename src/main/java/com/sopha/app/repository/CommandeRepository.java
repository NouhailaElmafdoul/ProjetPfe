package com.sopha.app.repository;

import com.sopha.app.models.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    @Query("SELECT DISTINCT c FROM Commande c JOIN FETCH c.lignesCommande lc JOIN FETCH lc.produit WHERE c.user.id = :userId")
    List<Commande> findByUserIdWithProducts(Long userId);

    @Query("SELECT DISTINCT c FROM Commande c JOIN FETCH c.lignesCommande lc JOIN FETCH lc.produit")
    List<Commande> findAllWithProducts();
    
    @Query("SELECT DISTINCT c FROM Commande c JOIN FETCH c.lignesCommande lc JOIN FETCH lc.produit WHERE c.status = :status")
    List<Commande> findCommandesByStatus(String status);
    
    
    @Query("SELECT c FROM Commande c LEFT JOIN FETCH c.lignesCommande lc LEFT JOIN FETCH lc.produit WHERE c.status = 'APPROUVÉ'")
    List<Commande> findByStatus(String status);

    @Query("SELECT c.user.nom, c.user.prenom, COUNT(c) FROM Commande c WHERE c.status = 'APPROUVÉ' GROUP BY c.user.nom, c.user.prenom ORDER BY COUNT(c) DESC")
    List<Object[]> findTopClients();

    @Query("SELECT p.nomProduit, SUM(lc.quantite) FROM LigneCommande lc JOIN lc.produit p WHERE lc.commande.status = 'APPROUVÉ' GROUP BY p.nomProduit ORDER BY SUM(lc.quantite) DESC")
    List<Object[]> findTopSellingProducts();
}
