package com.bsoft.mercadoEnvios.repository;

import com.bsoft.mercadoEnvios.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
