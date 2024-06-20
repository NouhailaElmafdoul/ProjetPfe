package com.sopha.app.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="produit_perimer")
public class ProduitPerimer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nomProduit;

    private Date dateDeFabrication;
    private Date dateDExpiration;
    private Double prix;
    private Double prixVente;
    private Integer quantite;

    // Constructeurs, getters, et setters
    public ProduitPerimer() {}

    public ProduitPerimer(String nomProduit, Date dateDeFabrication, Date dateDExpiration, Double prix, Double prixVente, Integer quantite) {
        this.nomProduit = nomProduit;
        this.dateDeFabrication = dateDeFabrication;
        this.dateDExpiration = dateDExpiration;
        this.prix = prix;
        this.prixVente = prixVente;
        this.quantite = quantite;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomProduit() {
		return nomProduit;
	}

	public void setNomProduit(String nomProduit) {
		this.nomProduit = nomProduit;
	}

	public Date getDateDeFabrication() {
		return dateDeFabrication;
	}

	public void setDateDeFabrication(Date dateDeFabrication) {
		this.dateDeFabrication = dateDeFabrication;
	}

	public Date getDateDExpiration() {
		return dateDExpiration;
	}

	public void setDateDExpiration(Date dateDExpiration) {
		this.dateDExpiration = dateDExpiration;
	}

	public Double getPrix() {
		return prix;
	}

	public void setPrix(Double prix) {
		this.prix = prix;
	}

	public Double getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(Double prixVente) {
		this.prixVente = prixVente;
	}

	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}

    // Getters et setters ici
    
    
}
