package com.bsoft.mercadoEnvios.service;

import com.bsoft.mercadoEnvios.model.Customer;
import com.bsoft.mercadoEnvios.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId);
    }
}
