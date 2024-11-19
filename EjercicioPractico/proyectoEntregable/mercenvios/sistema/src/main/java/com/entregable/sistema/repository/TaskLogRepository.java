package com.entregable.sistema.repository;

import com.entregable.sistema.model.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskLogRepository extends JpaRepository<TaskLog, Integer> {
}