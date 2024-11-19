package com.bsoft.mercadoEnvios.service;

import com.bsoft.mercadoEnvios.model.Product;
import com.bsoft.mercadoEnvios.repository.ProductRepository;
import com.bsoft.mercadoEnvios.repository.ShippingItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShippingItemRepository shippingItemRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTopSentProducts() {
    	
        List<Object[]> topSentProductsData = Arrays.asList(
            new Object[]{1, 50L}, // Producto 1, con 50 envíos
            new Object[]{2, 40L}, // Producto 2, con 40 envíos
            new Object[]{3, 30L}  // Producto 3, con 30 envíos
        );

        Product product1 = new Product(1, "Producto 1", 100.0, null);
        Product product2 = new Product(2, "Producto 2", 200.0, null);
        Product product3 = new Product(3, "Producto 3", 300.0, null);

        when(shippingItemRepository.findTopSentProducts()).thenReturn(topSentProductsData);
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));
        when(productRepository.findById(3)).thenReturn(Optional.of(product3));

      
        List<Map<String, Object>> result = productService.getTopSentProducts();

      
        assertEquals(3, result.size());
        assertEquals("Producto 1", result.get(0).get("description"));
        assertEquals(50L, result.get(0).get("totalCount"));
        assertEquals("Producto 2", result.get(1).get("description"));
        assertEquals(40L, result.get(1).get("totalCount"));
        assertEquals("Producto 3", result.get(2).get("description"));
        assertEquals(30L, result.get(2).get("totalCount"));
    }
}
