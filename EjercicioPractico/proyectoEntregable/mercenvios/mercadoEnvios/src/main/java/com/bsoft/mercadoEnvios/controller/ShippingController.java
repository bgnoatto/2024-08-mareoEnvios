package com.bsoft.mercadoEnvios.controller;

import com.bsoft.mercadoEnvios.model.Shipping;
import com.bsoft.mercadoEnvios.service.ShippingService;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shippings")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @GetMapping("/{shippingId}")		// obtener informacion del envio y productos comprados
    public ResponseEntity<Shipping> getShipping(@PathVariable Integer shippingId) {
        return shippingService.getShippingById(shippingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PatchMapping("/{shippingId}")
    public ResponseEntity<String> updateShippingState(@PathVariable Integer shippingId,
                                                        @RequestBody Map<String, String> transitionRequest) {
        String transition = transitionRequest.get("transition");
        try {
            // Actualizar el estado del envío según la transición
            shippingService.updateShippingState(shippingId, transition);
            return ResponseEntity.ok("Estado del envío actualizado exitosamente.");
        } catch (IllegalArgumentException e) {
            // Devolver un error si la transición no es válida
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}