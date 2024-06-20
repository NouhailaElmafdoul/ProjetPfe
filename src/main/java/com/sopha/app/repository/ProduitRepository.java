package com.sopha.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sopha.app.models.Produit;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    Optional<Produit> findById(Long id);
    Optional<Produit> findByNomProduit(String nomProduit);

    @Query("SELECT DISTINCT p FROM Produit p")
    List<Produit> findAllWithDetails();

    List<Produit> findByQuantite(int quantite);
    
    List<Produit> findByNomProduitContaining(String keyword);
    
    @Query("SELECT p FROM Produit p WHERE p.nomProduit LIKE %:keyword%")
    List<Produit> findByKeyword(String keyword);
    

    @Query("SELECT p FROM Produit p ORDER BY p.id DESC")
    List<Produit> findAllOrderByIdDesc();
}
