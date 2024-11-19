package com.bsoft.mercadoEnvios.controller;

import com.bsoft.mercadoEnvios.model.Product;
import com.bsoft.mercadoEnvios.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetTopSentProducts() throws Exception {
        // Crea productos de ejemplo
        Product product1 = new Product(1, "Product 1", 10.0, null);
        Product product2 = new Product(2, "Product 2", 20.0, null);
        Product product3 = new Product(3, "Product 3", 30.0, null);

        // Crea el mapa de resultado esperado
        List<Map<String, Object>> topSentProducts = Arrays.asList(
            createProductMap(product1, 100L),
            createProductMap(product2, 80L),
            createProductMap(product3, 60L)
        );

        // Simula la respuesta del servicio
        Mockito.when(productService.getTopSentProducts())
               .thenReturn(topSentProducts);

        mockMvc.perform(get("/reports/top-sent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                    "[{\"description\":\"Product 1\",\"totalCount\":100},"
                  + "{\"description\":\"Product 2\",\"totalCount\":80},"
                  + "{\"description\":\"Product 3\",\"totalCount\":60}]"
                ));
    }

    private Map<String, Object> createProductMap(Product product, Long totalCount) {
        Map<String, Object> map = new HashMap<>();
        map.put("description", product.getDescription());
        map.put("totalCount", totalCount);
        return map;
    }
}
