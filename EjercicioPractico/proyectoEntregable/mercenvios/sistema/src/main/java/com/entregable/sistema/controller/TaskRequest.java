package com.entregable.sistema.controller;

import com.entregable.sistema.model.Task;
import lombok.Data;

import java.util.List;

@Data
public class TaskRequest {
    private List<Task> envios;
}