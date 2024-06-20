package com.sopha.app.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class VenteJour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private Double totalVentes;

    public VenteJour() {}

    public VenteJour(Date date, Double totalVentes) {
        this.date = date;
        this.totalVentes = totalVentes;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotalVentes() {
        return totalVentes;
    }

    public void setTotalVentes(Double totalVentes) {
        this.totalVentes = totalVentes;
    }
}
