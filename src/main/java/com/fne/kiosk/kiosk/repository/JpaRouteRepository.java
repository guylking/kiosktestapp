package com.fne.kiosk.kiosk.repository;


import com.fne.kiosk.kiosk.model.CamelRouteComposite;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRouteRepository extends JpaRepository<CamelRouteComposite, String> {
}
