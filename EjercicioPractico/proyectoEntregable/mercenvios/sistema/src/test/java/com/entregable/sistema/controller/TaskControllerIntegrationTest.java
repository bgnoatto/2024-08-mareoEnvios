package com.entregable.sistema.controller;

import com.entregable.sistema.model.TaskLog;
import com.entregable.sistema.repository.TaskLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskLogRepository taskLogRepository;

    @BeforeEach
    void setUp() {
        taskLogRepository.deleteAll(); // Limpia la base de datos antes de cada prueba
    }

    @Test
    void testGetStatus() throws Exception {
        TaskLog taskLog = new TaskLog();
        taskLog.setShippingId(1);
        taskLog.setStatus("SUCCESS");
        taskLog.setMessage("Transacción completada con éxito.");

        taskLogRepository.save(taskLog);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/status")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"shippingId\":1,\"status\":\"SUCCESS\",\"message\":\"Transacción completada con éxito.\"}]"));
    }
}
