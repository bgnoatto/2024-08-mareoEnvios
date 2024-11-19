package com.bsoft.mercadoEnvios.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductGettersAndSetters() {
        // Crea un objeto Product
        Product product = new Product();
        product.setId(1);
        product.setDescription("Product Description");
        product.setWeight(2.5);
        
        // Verifica los valores con los métodos getters
        assertEquals(1, product.getId());
        assertEquals("Product Description", product.getDescription());
        assertEquals(2.5, product.getWeight());
    }

    @Test
    public void testProductConstructor() {
        // Crea un objeto Product usando el constructor con parámetros
        Product product = new Product(1, "Product Description", 2.5, null);

        // Verifica los valores con los métodos getters
        assertEquals(1, product.getId());
        assertEquals("Product Description", product.getDescription());
        assertEquals(2.5, product.getWeight());
    }

    @Test
    public void testProductEqualsAndHashCode() {
        // Crea dos objetos Product con los mismos atributos
        Product product1 = new Product(1, "Product Description", 2.5, null);
        Product product2 = new Product(1, "Product Description", 2.5, null);

        // Verifica que equals() y hashCode() funcionan correctamente
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    public void testProductToString() {
        // Crea un objeto Product
        Product product = new Product(1, "Product Description", 2.5, null);

        // Verifica que toString() devuelve una representación en cadena no nula
        String toStringResult = product.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Product Description"));
    }
}
