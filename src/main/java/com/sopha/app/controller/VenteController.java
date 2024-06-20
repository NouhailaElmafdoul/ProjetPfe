package com.sopha.app.controller;

import com.sopha.app.models.VenteJour;
import com.sopha.app.repository.VenteJourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ventes")
public class VenteController {

    @Autowired
    private VenteJourRepository venteJourRepository;

    @GetMapping("/jour")
    public List<VenteJour> getVentesParJour() {
        return venteJourRepository.findAll();
    }
}
