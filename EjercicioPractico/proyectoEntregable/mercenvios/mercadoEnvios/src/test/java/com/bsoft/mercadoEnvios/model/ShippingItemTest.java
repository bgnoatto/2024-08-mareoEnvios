package com.bsoft.mercadoEnvios.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ShippingItemTest {

    @Test
    public void testShippingItemGettersAndSetters() {
        // Crea un objeto ShippingItem
        ShippingItem shippingItem = new ShippingItem();
        shippingItem.setId(1);
        shippingItem.setShipping(new Shipping(1, new Customer(), "initial", null, null, 1, Collections.emptyList()));
        shippingItem.setProduct(new Product(1, "Product A", 1.0, Collections.emptyList()));
        shippingItem.setProductCount(5);

        // Verifica los valores con los métodos getters
        assertEquals(1, shippingItem.getId());
        assertNotNull(shippingItem.getShipping());
        assertNotNull(shippingItem.getProduct());
        assertEquals(5, shippingItem.getProductCount());
    }

    @Test
    public void testShippingItemConstructor() {
        // Crea un objeto ShippingItem usando el constructor con parámetros
        Shipping shipping = new Shipping(1, new Customer(), "initial", null, null, 1, Collections.emptyList());
        Product product = new Product(1, "Product A", 1.0, Collections.emptyList());
        ShippingItem shippingItem = new ShippingItem(1, shipping, product, 5);

        // Verifica los valores con los métodos getters
        assertEquals(1, shippingItem.getId());
        assertEquals(shipping, shippingItem.getShipping());
        assertEquals(product, shippingItem.getProduct());
        assertEquals(5, shippingItem.getProductCount());
    }

    @Test
    public void testShippingItemEqualsAndHashCode() {
        // Crea dos objetos ShippingItem con los mismos atributos
        Shipping shipping = new Shipping(1, new Customer(), "initial", null, null, 1, Collections.emptyList());
        Product product = new Product(1, "Product A", 1.0, Collections.emptyList());
        ShippingItem shippingItem1 = new ShippingItem(1, shipping, product, 5);
        ShippingItem shippingItem2 = new ShippingItem(1, shipping, product, 5);

        // Verifica que equals() y hashCode() funcionan correctamente
        assertEquals(shippingItem1, shippingItem2);
        assertEquals(shippingItem1.hashCode(), shippingItem2.hashCode());
    }

    @Test
    public void testShippingItemToString() {
        // Crea un objeto ShippingItem
        Shipping shipping = new Shipping(1, new Customer(), "initial", null, null, 1, Collections.emptyList());
        Product product = new Product(1, "Product A", 1.0, Collections.emptyList());
        ShippingItem shippingItem = new ShippingItem(1, shipping, product, 5);

        // Verifica que toString() devuelve una representación en cadena no nula
        String toStringResult = shippingItem.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("5"));
    }
}
