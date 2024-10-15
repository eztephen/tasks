package com.take_home.exercise.tasks;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.take_home.exercise.tasks.controllers.TaskController;
import com.take_home.exercise.tasks.enums.Status;
import com.take_home.exercise.tasks.models.Task;
import com.take_home.exercise.tasks.repositories.TaskRepository;
import com.take_home.exercise.tasks.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(TaskController.class)
@Import({TaskService.class, TaskRepository.class})
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(1L, "Test Task", "Description", Status.OPEN, LocalDate.now().plusDays(1));
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task));

        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(task.id()))
                .andExpect(jsonPath("$[0].title").value(task.title()))
                .andExpect(jsonPath("$[0].description").value(task.description()))
                .andExpect(jsonPath("$[0].status").value(task.status().toString()))
                .andExpect(jsonPath("$[0].dueDate").value(task.dueDate().toString()));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(ResponseEntity.ok(task));

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.id()))
                .andExpect(jsonPath("$.title").value(task.title()))
                .andExpect(jsonPath("$.description").value(task.description()))
                .andExpect(jsonPath("$.status").value(task.status().toString()))
                .andExpect(jsonPath("$.dueDate").value(task.dueDate().toString()));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        when(taskService.getTaskById(task.id())).thenReturn(ResponseEntity.ok(task));

        mockMvc.perform(get("/tasks/{id}", task.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.id()))
                .andExpect(jsonPath("$.title").value(task.title()))
                .andExpect(jsonPath("$.description").value(task.description()))
                .andExpect(jsonPath("$.status").value(task.status().toString()))
                .andExpect(jsonPath("$.dueDate").value(task.dueDate().toString()));

        verify(taskService, times(1)).getTaskById(task.id());
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        when(taskService.getTaskById(anyLong())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/tasks/{id}", 999L))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(999L);
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task updatedTask = new Task(1L, "Updated Task", "Updated Description", Status.IN_PROGRESS, LocalDate.now().plusDays(2));

        when(taskService.getTaskById(task.id())).thenReturn(ResponseEntity.ok(task));
        when(taskService.updateTask(eq(task.id()), any(Task.class))).thenReturn(ResponseEntity.ok(updatedTask));

        mockMvc.perform(put("/tasks/{id}", task.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedTask.id()))
                .andExpect(jsonPath("$.title").value(updatedTask.title()))
                .andExpect(jsonPath("$.description").value(updatedTask.description()))
                .andExpect(jsonPath("$.status").value(updatedTask.status().toString()))
                .andExpect(jsonPath("$.dueDate").value(updatedTask.dueDate().toString()));

        verify(taskService, times(1)).updateTask(eq(task.id()), any(Task.class));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingTask() throws Exception {
        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(put("/tasks/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(anyLong(), any(Task.class));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        when(taskService.getTaskById(task.id())).thenReturn(ResponseEntity.ok(task));

        mockMvc.perform(delete("/tasks/{id}", task.id()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(task.id());
    }
    @Test
    void shouldReturn404WhenDeletingNonExistingTask() throws Exception {
        when(taskService.deleteTask(anyLong())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/tasks/{id}", 999L))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(999L);
    }
}
