package com.bsoft.mercadoEnvios.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ShippingTest {

    @Test
    public void testShippingGettersAndSetters() {
        // Crea un objeto Shipping
        Shipping shipping = new Shipping();
        shipping.setId(1);
        shipping.setState("initial");
        shipping.setSendDate(LocalDate.of(2024, 8, 15));
        shipping.setArriveDate(LocalDate.of(2024, 8, 20));
        shipping.setPriority(1);
        shipping.setShippingItems(Collections.emptyList()); // Inicializa con una lista vacía

        // Verifica los valores con los métodos getters
        assertEquals(1, shipping.getId());
        assertEquals("initial", shipping.getState());
        assertEquals(LocalDate.of(2024, 8, 15), shipping.getSendDate());
        assertEquals(LocalDate.of(2024, 8, 20), shipping.getArriveDate());
        assertEquals(1, shipping.getPriority());
        assertNotNull(shipping.getShippingItems());
    }

    @Test
    public void testShippingConstructor() {
        // Crea un objeto Shipping usando el constructor con parámetros
        Shipping shipping = new Shipping(1, new Customer(), "initial", LocalDate.of(2024, 8, 15),
                                         LocalDate.of(2024, 8, 20), 1, Collections.emptyList());

        // Verifica los valores con los métodos getters
        assertEquals(1, shipping.getId());
        assertEquals("initial", shipping.getState());
        assertEquals(LocalDate.of(2024, 8, 15), shipping.getSendDate());
        assertEquals(LocalDate.of(2024, 8, 20), shipping.getArriveDate());
    }
}
