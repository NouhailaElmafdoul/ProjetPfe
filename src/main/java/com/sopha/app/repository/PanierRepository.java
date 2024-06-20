package com.sopha.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sopha.app.models.Panier;

public interface PanierRepository extends JpaRepository<Panier, Long> {
    List<Panier> findByUserId(Long userId);
}
