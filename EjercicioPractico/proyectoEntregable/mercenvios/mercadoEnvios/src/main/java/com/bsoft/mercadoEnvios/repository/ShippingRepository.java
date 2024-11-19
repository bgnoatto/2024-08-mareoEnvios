package com.bsoft.mercadoEnvios.repository;

import com.bsoft.mercadoEnvios.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipping, Integer> {
}
