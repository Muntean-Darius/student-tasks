package com.example.studenttasks.controller;

import com.example.studenttasks.model.dto.request.TaskCreateRequest;
import com.example.studenttasks.model.dto.response.TaskResponse;
import com.example.studenttasks.model.entity.User;
import com.example.studenttasks.repository.UserRepository;
import com.example.studenttasks.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request, Principal principal) {
        Long currentUserId = getUserIdFromPrincipal(principal);
        TaskResponse response = taskService.createTask(request,currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/tasks/my")
    public ResponseEntity<List<TaskResponse>> getMyTasks(Principal principal) {
        Long currentUserId = getUserIdFromPrincipal(principal);
        List<TaskResponse> tasks = taskService.getMyTasks(currentUserId);
        return ResponseEntity.ok(tasks);
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getId();
    }
}
