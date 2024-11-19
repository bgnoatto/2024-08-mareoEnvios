package com.bsoft.mercadoEnvios.repository;

import com.bsoft.mercadoEnvios.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
