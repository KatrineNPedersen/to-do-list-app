package com.project.todoapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.todoapp.model.Task;
import com.project.todoapp.repository.TaskRepository;
import com.project.todoapp.service.TaskService;

public class TaskServiceTest {
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository);
    }

    @Test
    public void testgetAllTasks(){
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(new Task("Task 1"));
        allTasks.add(new Task("Task 2", true));

        when(taskRepository.findAll()).thenReturn(allTasks);

        List<Task> result = taskService.getAllTasks();
        assertEquals(allTasks, result);
        assertEquals(2, result.size());
        assertEquals(false, result.get(0).isCompleted());
        assertEquals("Task 2", result.get(1).getDescription());
    }

    @Test
    public void testAddTask(){
        Task task = new Task();
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.addTask(task);

        assertEquals(task, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testDeleteTask(){
        doNothing().when(taskRepository).deleteById(anyLong());

        taskService.deleteTask(anyLong());

        verify(taskRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void TestFindAllByCompleted(){
        List<Task> completedTasks = new ArrayList<>();
        completedTasks.add(new Task("Completed Task", true));

        when(taskRepository.findAllByCompleted(true)).thenReturn(completedTasks);

        List<Task> result = taskService.findAllByCompleted(true);

        assertEquals(1, result.size());
        assertEquals("Completed Task", result.get(0).getDescription());
        assertTrue(result.get(0).isCompleted());
    }

    @Test
    public void testDeleteCompletedTasks(){
  
        taskService.deleteCompletedTasks();

        verify(taskRepository, times(1)).deleteAllByCompleted(true);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void testUpdateTaskStatus(){
        Long taskId = 1L;
        Task task = new Task(1L,"Task1", false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.updateTaskStatus(taskId);

        assertTrue(task.isCompleted());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(task);

    }
}
