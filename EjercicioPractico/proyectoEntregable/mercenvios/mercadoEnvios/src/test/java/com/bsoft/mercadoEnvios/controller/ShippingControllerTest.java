package com.bsoft.mercadoEnvios.controller;

import com.bsoft.mercadoEnvios.model.Shipping;
import com.bsoft.mercadoEnvios.service.ShippingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShippingController.class)
public class ShippingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShippingService shippingService;

    @Test
    public void testGetShippingFound() throws Exception {
        // Crea un objeto Shipping de ejemplo
        Shipping shipping = new Shipping(1, null, "initial", LocalDate.now(), LocalDate.now().plusDays(1), 1, null);

        // Simula la respuesta del servicio
        Mockito.when(shippingService.getShippingById(1))
               .thenReturn(Optional.of(shipping));

        mockMvc.perform(get("/shippings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                    "{\"id\":1,\"state\":\"initial\",\"sendDate\":\"" + shipping.getSendDate() + "\","
                    + "\"arriveDate\":\"" + shipping.getArriveDate() + "\",\"priority\":1}"
                ));
    }

    @Test
    public void testGetShippingNotFound() throws Exception {
        // Simula que no se encuentra el envío
        Mockito.when(shippingService.getShippingById(1))
               .thenReturn(Optional.empty());

        mockMvc.perform(get("/shippings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateShippingStateSuccess() throws Exception {
        // Crea un objeto Shipping de ejemplo
        Shipping shipping = new Shipping(1, null, "initial", LocalDate.now(), LocalDate.now().plusDays(1), 1, null);

        // Simula la actualización del estado del envío
        Mockito.when(shippingService.updateShippingState(1, "sendToMail"))
               .thenReturn(shipping);

        Map<String, String> transitionRequest = new HashMap<>();
        transitionRequest.put("transition", "sendToMail");

        mockMvc.perform(patch("/shippings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"transition\":\"sendToMail\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Estado del envío actualizado exitosamente."));
    }

    @Test
    public void testUpdateShippingStateFailure() throws Exception {
        // Simula una transición inválida
        Mockito.when(shippingService.updateShippingState(1, "invalidState"))
               .thenThrow(new IllegalArgumentException("Transición no permitida desde el estado: initial"));

        Map<String, String> transitionRequest = new HashMap<>();
        transitionRequest.put("transition", "invalidState");

        mockMvc.perform(patch("/shippings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"transition\":\"invalidState\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Transición no permitida desde el estado: initial"));
    }
}
