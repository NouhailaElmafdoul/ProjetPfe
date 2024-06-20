package com.sopha.app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "panier")
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produit produit;

    @NotNull(message = "La quantité est obligatoire")
    private Integer quantite;

    @NotNull(message = "Le prix total est obligatoire")
    private Double prixTotal;

    @ManyToOne
    private User user;

    
    public Panier() {
        super();
    }

    public Panier(Produit produit, Integer quantite, Double prixTotal, User user) {
        super();
        this.produit = produit;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
        this.user = user;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    @Override
    public String toString() {
        return "Panier [id=" + id + ", produit=" + produit + ", quantite=" + quantite + ", prixTotal=" + prixTotal + ", user=" + user + "]";
    }
}
