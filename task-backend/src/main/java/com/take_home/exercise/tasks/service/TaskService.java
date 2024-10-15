package com.take_home.exercise.tasks.service;

import com.take_home.exercise.tasks.models.Task;
import com.take_home.exercise.tasks.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    // Methods for CRUD operations
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public ResponseEntity<Task> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Task> createTask(Task task) {
        return ResponseEntity.ok(taskRepository.save(task));
    }

    public ResponseEntity<Task>  updateTask(Long id, Task taskDetails) {
        // update fields
        return taskRepository.findById(id)
                .map(task -> {
                    Task updatedTask = new Task(
                            id,
                            taskDetails.title(),
                            taskDetails.description(),
                            taskDetails.status(),
                            taskDetails.dueDate()
                    );
                    return ResponseEntity.ok(taskRepository.save(updatedTask));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteTask(Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
