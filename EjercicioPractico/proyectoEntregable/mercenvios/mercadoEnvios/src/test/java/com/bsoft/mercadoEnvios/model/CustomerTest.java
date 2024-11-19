package com.bsoft.mercadoEnvios.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    public void testCustomerGettersAndSetters() {
        // Crea un objeto Customer
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAddress("123 Elm Street");
        customer.setCity("Springfield");

        // Verifica los valores con los métodos getters
        assertEquals(1, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("123 Elm Street", customer.getAddress());
        assertEquals("Springfield", customer.getCity());
    }

    @Test
    public void testCustomerConstructor() {
        // Crea un objeto Customer usando el constructor con parámetros
        Customer customer = new Customer(1, "John", "Doe", "123 Elm Street", "Springfield", null);

        // Verifica los valores con los métodos getters
        assertEquals(1, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("123 Elm Street", customer.getAddress());
        assertEquals("Springfield", customer.getCity());
    }

    @Test
    public void testCustomerEqualsAndHashCode() {
        // Crea dos objetos Customer con los mismos atributos
        Customer customer1 = new Customer(4, "John", "Doe", "123 Elm Street", "Springfield", null);
        Customer customer2 = new Customer(4, "John", "Doe", "123 Elm Street", "Springfield", null);

        // Verifica que equals() y hashCode() funcionan correctamente
        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    public void testCustomerToString() {
        // Crea un objeto Customer
        Customer customer = new Customer(1, "John", "Doe", "123 Elm Street", "Springfield", null);

        // Verifica que toString() devuelve una representación en cadena no nula
        String toStringResult = customer.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("John"));
        assertTrue(toStringResult.contains("Doe"));
    }
}
