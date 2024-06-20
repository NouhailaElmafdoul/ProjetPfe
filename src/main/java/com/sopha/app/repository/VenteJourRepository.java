package com.sopha.app.repository;

import com.sopha.app.models.VenteJour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface VenteJourRepository extends JpaRepository<VenteJour, Long> {
    VenteJour findByDate(Date date);
}
