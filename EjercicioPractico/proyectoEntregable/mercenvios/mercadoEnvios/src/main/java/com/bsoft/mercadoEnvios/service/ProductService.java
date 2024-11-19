package com.bsoft.mercadoEnvios.service;

import com.bsoft.mercadoEnvios.model.Product;
import com.bsoft.mercadoEnvios.repository.ProductRepository;
import com.bsoft.mercadoEnvios.repository.ShippingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShippingItemRepository shippingItemRepository;

    public List<Map<String, Object>> getTopSentProducts() {
        // Obtener los 3 productos más enviados
        List<Object[]> topSentProductsData = shippingItemRepository.findTopSentProducts();

        // Convertir el resultado en una lista de mapas con la descripción y cantidad total
        return topSentProductsData.stream()
                .limit(3) // Limitar a los 3 productos más enviados
                .map(data -> {
                	Integer productId = (Integer) data[0];
                    Long totalCount = (Long) data[1];
                    Product product = productRepository.findById(productId).orElse(null);
                    if (product != null) {
                        Map<String, Object> result = new HashMap<>();
                        result.put("description", product.getDescription());
                        result.put("totalCount", totalCount);
                        return result;
                    }
                    return null;
                })
                .filter(map -> map != null)
                .collect(Collectors.toList());
    }
}