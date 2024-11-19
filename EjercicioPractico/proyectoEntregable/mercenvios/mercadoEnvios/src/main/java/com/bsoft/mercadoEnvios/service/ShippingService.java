package com.bsoft.mercadoEnvios.service;

import com.bsoft.mercadoEnvios.model.Shipping;
import com.bsoft.mercadoEnvios.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    public Optional<Shipping> getShippingById(Integer shippingId) {
        return shippingRepository.findById(shippingId);
    }

    public Shipping updateShippingState(Integer shippingId, String transition) {
        Optional<Shipping> shippingOptional = shippingRepository.findById(shippingId);
        if (shippingOptional.isPresent()) {
            Shipping shipping = shippingOptional.get();
            String currentState = shipping.getState();

            // Verificar la transición válida basada en el estado actual
            String newState = validateTransition(currentState, transition);

            // Actualizar el estado solo si la transición es válida
            if (newState != null) {
                shipping.setState(newState);
                return shippingRepository.save(shipping);
            } else {
                throw new IllegalArgumentException("Transición no permitida desde el estado: " + currentState);
            }
        }
        return null;
    }

    private String validateTransition(String currentState, String transition) {
    	// Verificar si la transición es válida
        if (!isValidTransition(transition)) {
            throw new IllegalArgumentException("El estado del envío al que se quiere cambiar, no corresponde con ninguno que sea válido");
        }
        switch (currentState) {
            case "initial":
                if ("sendToMail".equals(transition)) return "sendToMail";
                if ("cancelled".equals(transition)) return "cancelled";
                break;
            case "sendToMail":
                if ("inTravel".equals(transition)) return "inTravel";
                if ("cancelled".equals(transition)) return "cancelled";
                break;
            case "inTravel":
                if ("delivered".equals(transition)) return "delivered";
                if ("cancelled".equals(transition)) return "cancelled";
                break;
            case "delivered":
            case "cancelled": 
            default:
                return "El estado del envío no corresponde con ninguno que sea válido"; // Estado desconocido o no manejado
        }
        return null; // Si no se encontró una transición válida
    }
    
    private boolean isValidTransition(String transition) {
        return "initial".equals(transition) || 
               "sendToMail".equals(transition) || 
               "inTravel".equals(transition) || 
               "delivered".equals(transition) || 
               "cancelled".equals(transition);
    }
}
