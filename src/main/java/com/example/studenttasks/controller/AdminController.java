package com.example.studenttasks.controller;

import com.example.studenttasks.model.dto.request.UserRegistrationRequest;
import com.example.studenttasks.model.dto.response.TaskResponse;
import com.example.studenttasks.model.dto.response.UserResponse;
import com.example.studenttasks.service.TaskService;
import com.example.studenttasks.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    private final TaskService taskService;
    private final UserService userService;

    public AdminController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/users")
    public ResponseEntity<UserResponse> createAdminUser(@Valid @RequestBody UserRegistrationRequest request){
        UserResponse response = userService.createAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
