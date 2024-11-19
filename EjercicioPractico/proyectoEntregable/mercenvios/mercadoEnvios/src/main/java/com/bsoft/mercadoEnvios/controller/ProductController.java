package com.bsoft.mercadoEnvios.controller;

import com.bsoft.mercadoEnvios.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/top-sent")		// obtener un listado con la descripcion y cantidad de los 3 productos más solicitados para su envio
    public ResponseEntity<List<Map<String, Object>>> getTopSentProducts() {
        List<Map<String, Object>> topSentProducts = productService.getTopSentProducts();
        return ResponseEntity.ok(topSentProducts);
    }
}