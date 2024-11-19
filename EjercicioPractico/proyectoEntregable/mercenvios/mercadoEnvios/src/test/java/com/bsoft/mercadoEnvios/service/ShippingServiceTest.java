package com.bsoft.mercadoEnvios.service;

import com.bsoft.mercadoEnvios.model.Shipping;
import com.bsoft.mercadoEnvios.repository.ShippingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShippingServiceTest {

    @InjectMocks
    private ShippingService shippingService;

    @Mock
    private ShippingRepository shippingRepository;

    private Shipping shipping;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shipping = new Shipping();
        shipping.setId(1);
    }

    @Test
    void shouldUpdateState_WhenValidTransition() {
        shipping.setState("initial");
        when(shippingRepository.findById(1)).thenReturn(Optional.of(shipping));
        
        Shipping updatedShipping = new Shipping();
        updatedShipping.setId(1);
        updatedShipping.setState("sendToMail");
        
        when(shippingRepository.save(any(Shipping.class))).thenReturn(updatedShipping);

       
        Shipping result = shippingService.updateShippingState(1, "sendToMail");

      
        assertNotNull(result);
        assertEquals("sendToMail", result.getState());
    }
}
