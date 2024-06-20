package com.sopha.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopha.app.models.ProduitPerimer;

public interface ProduitPerimerRepository extends JpaRepository<ProduitPerimer, Long> {
}
