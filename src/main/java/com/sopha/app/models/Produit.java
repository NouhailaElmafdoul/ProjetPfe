package com.sopha.app.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Le nom du produit est obligatoire !!!!!")
    private String nomProduit;

    @NotNull(message = "La date de fabrication est obligatoire !!!!!")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateDeFabrication;

    @NotNull(message = "La date d'expiration est obligatoire !!!!!")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateDExpiration;

    @NotNull(message = "Le prix est obligatoire !!!!!")
    private Double prix;

    @NotNull(message = "Le prix de vente est obligatoire !!!!!")
    private Double prixVente;

    @NotNull(message = "La quantité est obligatoire !!!!!")
    private Integer quantite;

    @Lob
    private byte[] image;

    @Transient
    private String imageBase64;
    
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LigneCommande> lignesCommande;

    public Produit() {}

    public Produit(String nomProduit, Date dateDeFabrication, Date dateDExpiration, Double prix, Double prixVente, Integer quantite, byte[] image) {
        this.nomProduit = nomProduit;
        this.dateDeFabrication = dateDeFabrication;
        this.dateDExpiration = dateDExpiration;
        this.prix = prix;
        this.prixVente = prixVente;
        this.quantite = quantite;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    public Set<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(Set<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    @Override
    public String toString() {
        return "Produit [id=" + id + ", nomProduit=" + nomProduit + ", dateDeFabrication=" + dateDeFabrication
                + ", dateDExpiration=" + dateDExpiration + ", prix=" + prix + ", prixVente=" + prixVente + ", quantite=" + quantite + "]";
    }
}
