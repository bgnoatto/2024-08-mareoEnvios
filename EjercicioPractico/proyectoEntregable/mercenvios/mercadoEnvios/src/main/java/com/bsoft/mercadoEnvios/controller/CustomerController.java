package com.bsoft.mercadoEnvios.controller;

import com.bsoft.mercadoEnvios.model.Customer;
import com.bsoft.mercadoEnvios.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{customerId}")		// obtiene informacion del comprador con id:{customerId}
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer customerId) {
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
