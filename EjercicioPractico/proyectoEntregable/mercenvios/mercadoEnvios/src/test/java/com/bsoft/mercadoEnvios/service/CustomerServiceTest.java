package com.bsoft.mercadoEnvios.service;

import com.bsoft.mercadoEnvios.model.Customer;
import com.bsoft.mercadoEnvios.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCustomerById() {
     
        Integer customerId = 1;
        Customer customer = new Customer(
            customerId,               
            "Jose",                    
            "Domenech",                     
            "123 Elm Street",         
            "Banfield", null            
        );
        
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

      
        Optional<Customer> result = customerService.getCustomerById(customerId);

        assertEquals(Optional.of(customer), result);
    }
}

