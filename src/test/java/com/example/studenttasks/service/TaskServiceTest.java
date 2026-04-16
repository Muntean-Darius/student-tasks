package com.example.studenttasks.service;

import com.example.studenttasks.model.dto.response.TaskResponse;
import com.example.studenttasks.model.entity.Role;
import com.example.studenttasks.model.entity.Task;
import com.example.studenttasks.model.entity.User;
import com.example.studenttasks.repository.TaskRepository;
import com.example.studenttasks.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getMyTasks_ReturnsTaskList() {
        // Arrange
        User mockUser = new User(1L, "testuser", "pass", Role.USER);
        Task mockTask = new Task(1L, "Title", "Desc", LocalDateTime.now(), mockUser);

        when(taskRepository.findAllByUserId(1L)).thenReturn(List.of(mockTask));

        // Act
        List<TaskResponse> tasks = taskService.getMyTasks(1L);

        // Assert
        assertEquals(1, tasks.size());
        assertEquals("Title", tasks.get(0).getTitle());
        assertEquals("testuser", tasks.get(0).getOwnerUsername());
    }
}