package com.entregable.sistema.controller;

import com.entregable.sistema.model.Task;
import com.entregable.sistema.model.TaskLog;
import com.entregable.sistema.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/tarea-concurrente")
    public ResponseEntity<String> handleConcurrentTask(@RequestBody TaskRequest request) {
        List<Task> tasks = request.getEnvios().stream().map(task -> {
            // Mapea los campos del JSON al formato esperado por Task
            Task t = new Task();
            t.setShippingId(task.getShippingId());
            t.setTimeStartInSeg(task.getTimeStartInSeg());
            t.setNextState(task.isNextState());
            return t;
        }).collect(Collectors.toList());

        taskService.processTasks(tasks);
        return ResponseEntity.ok("Las tareas terminaron de ser procesadas.");
    }

    @GetMapping("/status")
    public ResponseEntity<List<TaskLog>> getStatus() {
        List<TaskLog> taskLogs = taskService.getTasksStatus();
        return ResponseEntity.ok(taskLogs);
    }
}
