package com.sopha.app.repository;

import com.sopha.app.models.LigneCommande;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
	
	@Query("SELECT lc.produit.id, SUM(lc.quantite) FROM LigneCommande lc GROUP BY lc.produit.id ORDER BY SUM(lc.quantite) DESC")
    List<Object[]> findTopSellingProducts();
	
}