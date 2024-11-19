package com.bsoft.mercadoEnvios.controller;

import com.bsoft.mercadoEnvios.model.Customer;
import com.bsoft.mercadoEnvios.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testGetCustomerFound() throws Exception {
        // Crea un cliente de ejemplo
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAddress("123 Main St");
        customer.setCity("Springfield");

        // Simulamos la respuesta del servicio
        Mockito.when(customerService.getCustomerById(anyInt()))
                .thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"123 Main St\",\"city\":\"Springfield\"}"));
    }

    @Test
    public void testGetCustomerNotFound() throws Exception {
        // Simulamos que no se encuentra el cliente
        Mockito.when(customerService.getCustomerById(anyInt()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
