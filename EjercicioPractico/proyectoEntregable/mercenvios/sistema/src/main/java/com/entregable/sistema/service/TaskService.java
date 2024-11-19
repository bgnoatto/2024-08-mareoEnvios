package com.entregable.sistema.service;

import com.entregable.sistema.model.ResultTask;
import com.entregable.sistema.model.Task;
import com.entregable.sistema.model.TaskLog;
import com.entregable.sistema.repository.ResultTaskRepository;
import com.entregable.sistema.repository.TaskLogRepository;
import com.entregable.sistema.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private ResultTaskRepository resultTaskRepository;
    
    

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10); // Tamaño del pool
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Tamaño del pool para ejecución de tareas
    private final ConcurrentHashMap<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    public String processTasks(List<Task> tasks) {
        // Agrupa tareas por shippingId para que no se ejecuten concurrentemente
        Map<Integer, List<Task>> tasksByShippingId = tasks.stream()			//Convierte la lista de tareas en un stream para poder aplicar operaciones funcionales.
                .sorted((t1, t2) -> Integer.compare(t1.getTimeStartInSeg(), t2.getTimeStartInSeg()))	//Ordena las tareas por el tiempo de inicio (timeStartInSeg), de forma ascendente.
                .collect(Collectors.groupingBy(Task::getShippingId));		// Agrupa las tareas en un mapa donde la clave es el shippingId y el valor es una lista de tareas (taskList) asociadas a ese shippingId
        // El resultado es un Map<Integer, List<Task>> donde cada clave es un shippingId y cada valor es una lista de tareas asociadas con ese shippingId.
        
        CountDownLatch latch = new CountDownLatch(tasks.size());

        tasksByShippingId.forEach((shippingId, taskList) -> {				// Recorre cada entrada del mapa tasksByShippingId. shippingId es la clave del mapa, y taskList es la lista de tareas asociadas con ese shippingId.
            Semaphore semaphore = semaphoreMap.computeIfAbsent(shippingId, k -> new Semaphore(1));		// Crea un Semaphore para el shippingId si aún no existe en el mapa semaphoreMap. Esto asegura que las tareas para el mismo shippingId se gestionen de manera sincronizada.
            taskList.forEach(task -> {
                scheduledExecutorService.schedule(() -> {			//programa la ejecución de la tarea en un momento específico.
                    try {
                        semaphore.acquire();				//bloquea el semáforo para que solo una tarea pueda ejecutarse a la vez para ese shippingId.
                        updateTaskStatus(task.getShippingId(), "IN_PROGRESS");

                        executorService.submit(() -> {			// envía la tarea para su ejecución.
                            try {
                                processTask(task);				// método que procesa la tarea
                                updateTaskStatus(task.getShippingId(), "SUCCESS");		//actualiza el estado de la tarea a "SUCCESS" si se completa correctamente.
                            } catch (Exception e) {
                                //updateTaskStatus(task.getShippingId(), "FAILED");
                            	System.out.println(e.getMessage());		//En caso de excepción, se captura el error y se imprime el mensaje 
                            } finally {
                                semaphore.release();					// libera el semáforo para que otras tareas para el mismo shippingId puedan ejecutarse.
                                latch.countDown();						// decrementa el contador del CountDownLatch, indicando que una tarea ha terminado.
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        updateTaskStatus(task.getShippingId(), "FAILED");
                    }
                }, task.getTimeStartInSeg(), TimeUnit.SECONDS);
            });
        });

        try {
            latch.await(); 				// bloquea el hilo hasta que el contador del CountDownLatch llegue a cero, es decir, hasta que todas las tareas hayan terminado.
            return "The tasks have been processed.";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();			// en caso de que el hilo sea interrumpido mientras espera.
            return "Processing interrupted.";
        } finally {
            scheduledExecutorService.shutdown();		//cierran los ejecutores después de que todas las tareas hayan terminado para liberar recursos.
            executorService.shutdown();
        }
    }

    private void processTask(Task task) {
        String shippingId = task.getShippingId().toString();
        boolean nextState = task.isNextState();					// Determina si se debe cambiar al siguiente estado (true) o cancelar (false).
        boolean stateChanged = false;							// Bandera para rastrear si el estado del envío ha cambiado.

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        ResponseEntity<Map> response = null;

        try {
            if (nextState) {
            	try {
            		response = new RestTemplate().exchange(		//Se obtiene el estado actual del envío desde el servidor
                        "http://localhost:8081/shippings/" + shippingId,
                        HttpMethod.GET,
                        null,
                        Map.class
            			);
            	} catch (HttpClientErrorException | HttpServerErrorException e) {
            		// Maneja errores específicos del cliente o del servidor (por ejemplo que el shippingId en la consulta del GET, no exista.
            		String errorMessage = "Transacción no permitida: el id del shipping ingresado no existe en la base de datos.";
            		logTaskError(shippingId, "FAILED", errorMessage);
            		throw new RuntimeException(errorMessage, e);
            	}

            	// Procesa la respuesta si no hubo errores
                if (response != null) { // Verifica si response fue correctamente inicializada
                	String currentState = (String) response.getBody().get("state");
	                String nextStateTransition = getNextStateTransition(currentState);		// se determina el siguiente estado permitido
	                if (nextStateTransition != null) {
	                	sleepForTransitionState(nextStateTransition);	// Añade un retraso basado en el estado de transición para simular el tiempo de proceso.. Ejem:{transition: sendToMail } -> Debe demorar 1 segundo
	                    HttpEntity<String> requestEntity = new HttpEntity<>("{ \"transition\": \"" + nextStateTransition + "\" }", headers);
	                    String patchResponse = HttpClientUtil.sendPatchRequest("http://localhost:8081/shippings/" + shippingId, requestEntity);		//Envía una solicitud PATCH para cambiar el estado del envío.
	                    if (!"FAILED".equals(patchResponse)) {
	                        stateChanged = true;
	                        saveResultTask(shippingId, task.getTimeStartInSeg(), "SUCCESS");
	                    } else {
	                        throw new RuntimeException("No se pudo cambiar el estado: " + currentState);
	                    }
	                } else {
	                    logTaskError(shippingId, "FAILED", "Transición no permitida desde el estado: \"" + currentState + "\" ya que es un estado final y no se puede transaccionar.");		// Registra un error si la transición no es permitida o si ocurre un fallo.
	                    throw new RuntimeException("Transición no permitida desde el estado: " + currentState);
	                }
               }
            } else {
                response = new RestTemplate().exchange(
                        "http://localhost:8081/shippings/" + shippingId,
                        HttpMethod.GET,
                        null,
                        Map.class
                );
                String currentState = (String) response.getBody().get("state");
                
                if (isValidTransitionToCancelled(currentState)) {		// Se verifica si la transición a "cancelled" es válida desde el estado actual
                    sleepForTransitionState("cancelled"); 	// Añade un retraso basado en el estado de transición
                    HttpEntity<String> requestEntity = new HttpEntity<>("{ \"transition\": \"cancelled\" }", headers);
                    String patchResponse = HttpClientUtil.sendPatchRequest("http://localhost:8081/shippings/" + shippingId, requestEntity);
                    if (!"FAILED".equals(patchResponse)) {			// true si patchResponse no es "FAILED"
                        stateChanged = true;
                        saveResultTask(shippingId, task.getTimeStartInSeg(), "SUCCESS");
                    } else {
                        throw new RuntimeException("No se pudo cambiar el estado a cancelled.");
                    }
                } else {
                    logTaskError(shippingId, "FAILED", "Transición a cancelled no permitida desde el estado: \"" + currentState + "\".");
                    throw new RuntimeException("Transición a cancelled no permitida desde el estado: " + currentState);
                }
            }
        } catch (RuntimeException e) {
            if (stateChanged) {
                saveResultTask(shippingId, task.getTimeStartInSeg(), "FAILED");
            }
            throw new RuntimeException("Error al procesar la tarea: " + e.getMessage());
        }
    }
    
    private void sleepForTransitionState(String nextState) {
        try {
            switch (nextState) {
                case "sendToMail":
                    Thread.sleep(1000); // 1 segundos
                    break;
                case "inTravel":
                    Thread.sleep(3000); // 3 segundos
                    break;
                case "delivered":
                	Thread.sleep(5000); // 5 segundos
                    break;
                case "cancelled":
                    Thread.sleep(3000); // 3 segundos para cancelled
                    break;
                default:
                    // No hay retraso para otros estados
                    break;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error al dormir el hilo: " + e.getMessage());
        }
    }
    
    private boolean isValidTransitionToCancelled(String currentState) {
        return "initial".equals(currentState) ||
               "sendToMail".equals(currentState) ||
               "inTravel".equals(currentState);
    }

    private void updateTaskStatus(int shippingId, String status) {
        TaskLog taskLog = new TaskLog();
        taskLog.setShippingId(shippingId);
        taskLog.setStatus(status);
        if(status.equals("SUCCESS")) {
        	taskLog.setMessage("Tarea completada. El estado transaccional del envío pasó a la siguiente etapa del viaje.");
        }else {
        	taskLog.setMessage("Ejecutando tarea..");
        }
        taskLogRepository.save(taskLog);
    }

    private void logTaskError(String shippingId, String status, String message) {
        TaskLog taskLog = new TaskLog();
        taskLog.setShippingId(Integer.parseInt(shippingId));
        taskLog.setStatus(status);
        taskLog.setMessage(message);
        taskLogRepository.save(taskLog);
    }

    private void saveResultTask(String shippingId, Integer timeStartInSeg, String status) {
        ResultTask resultTask = new ResultTask();
        resultTask.setShippingId(Integer.parseInt(shippingId));
        resultTask.setTimeStartInSeg(timeStartInSeg);
        resultTask.setStatus(status);
        resultTaskRepository.save(resultTask);
    }

    private String getNextStateTransition(String currentState) {
        switch (currentState) {
            case "initial":
                return "sendToMail";
            case "sendToMail":
                return "inTravel";
            case "inTravel":
                return "delivered";
            case "delivered":
            case "cancelled":
                return null;
            default:
                return null;
        }
    }
    
    

    public List<TaskLog> getTasksStatus() {
        return taskLogRepository.findAll();
    }
}