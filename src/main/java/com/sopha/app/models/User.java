package com.sopha.app.models;

import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name="Users")
public class User {
    @Id  	
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @NotBlank(message = "Le nom est obligatoire !!!!!")
	private String nom;

    @NotBlank(message = "Le Prénom est obligatoire !!!!!")
	private String prenom;

    @NotBlank(message = "Le cin est obligatoire !!!!!")
	private String cin;

    @NotBlank(message = "Le telephone est obligatoire !!!!!")
	@Pattern(regexp = "0[5-7][0-9]{8}", message = "Le numéro de téléphone invalide")
	private String telephone;

    @NotBlank(message = "Le adresse est obligatoire !!!!!")
	private String adresse;

    @NotNull(message = "L'âge est obligatoire")
	private Integer age ;

    @NotBlank(message = "L'email est obligatoire !!!!!")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Adresse e-mail invalide")
	@Column(unique=true)
	private String email ;

    @NotBlank(message = "Le genre est obligatoire !!!!!")
    @Pattern(regexp = "homme|femme", message = "Veuillez sélectionner un genre valide")
	private String genre ;

    @NotBlank(message = "Le mot de passe est obligatoire !!!!!")
	private String mot_de_passe;

    @NotBlank(message = "Le nom de la pharmacy est obligatoire !!!!!")
	private String nomPharmacy;

    @NotBlank(message = "L'adresse de la pharmacy est obligatoire !!!!!")
	private String adressePharmacy;

    @ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;		

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Commande> commandes;

    // Getters et Setters
	public Long getId() {
		return id;
	}

    public void setId(long id) {
		this.id = id;
	}

    public String getNom() {
		return nom;
	}

    public void setNom(String nom) {
		this.nom = nom;
	}

    public String getPrenom() {
		return prenom;
	}

    public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

    public String getCin() {
		return cin;
	}

    public void setCin(String cin) {
		this.cin = cin;
	}

    public String getTelephone() {
		return telephone;
	}

    public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

    public String getEmail() {
		return email;
	}

    public void setEmail(String email) {
		this.email = email;
	}

    public String getMot_de_passe() {
		return mot_de_passe;
	}

    public void setMot_de_passe(String mot_de_passe) {
		this.mot_de_passe = mot_de_passe;
    }

    public Integer getAge() {
		return age;
	}

    public void setAge(Integer age) {
		this.age = age;
	}

    public String getGenre() {
		return genre;
	}

    public void setGenre(String genre) {
		this.genre = genre;
	}

    public String getAdresse() {
		return adresse;
	}

    public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

    public String getNomPharmacy() {
        return nomPharmacy;
    }

    public void setNomPharmacy(String nomPharmacy) {
        this.nomPharmacy = nomPharmacy;
    }

    public String getAdressePharmacy() {
        return adressePharmacy;
    }

    public void setAdressePharmacy(String adressePharmacy) {
        this.adressePharmacy = adressePharmacy;
    }

    public Role getRole() { 
        return role; 
    }

    public void setRole(Role userRole) { 
        this.role = userRole; 
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
    
    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }
}
