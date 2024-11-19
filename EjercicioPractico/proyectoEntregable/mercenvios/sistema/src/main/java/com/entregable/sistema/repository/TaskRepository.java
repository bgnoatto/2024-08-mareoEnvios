package com.entregable.sistema.repository;

import com.entregable.sistema.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
	// Define el método para buscar tareas por shippingId y status
    List<Task> findByShippingIdAndStatus(Integer shippingId, String status);
}
