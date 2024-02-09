package com.project.todoapp.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.project.todoapp.model.Task;
import com.project.todoapp.service.TaskService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //Gets all completed tasks
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok().body(taskService.getAllTasks());
    }

    //Adds new task
    @PostMapping("/tasks")
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        if(task.getDescription() != "" && task.getDescription().length() < 40){
            Task newTask = taskService.addTask(task);
            return ResponseEntity.ok(newTask);
        }
        return ResponseEntity.badRequest().body(task);
    }

    //Deletes specific task
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId){
        if(taskService.getTaskByID(taskId) != null){
            taskService.deleteTask(taskId);
            return ResponseEntity.ok().body("Task was deleted");
        } else {
            return ResponseEntity.ok().body("No task was found");
        }
        
    }

    //Gets all completed tasks
    @GetMapping("/tasks/completed")
    public ResponseEntity<List<Task>> getAllCompletedTasks(boolean completed) {
        completed = true;
        return ResponseEntity.ok().body(taskService.findAllByCompleted(completed));
    }

    //Gets all active tasks
    @GetMapping("/tasks/active")
    public ResponseEntity<List<Task>> getAllActiveTasks(boolean completed) {
        completed = false;
        return ResponseEntity.ok().body(taskService.findAllByCompleted(completed));
    }

    //Delete all completed tasks
    @DeleteMapping("/tasks/completed")
    public ResponseEntity<String> deleteCompletedTasks(){
        if(taskService.findAllByCompleted(true) != null){
            taskService.deleteCompletedTasks();
            return ResponseEntity.ok().body("Completed task(s) were deleted");
        } else {
            return ResponseEntity.ok().body("No completed tasks were found");
        }
        
    }

    //Update status on task
    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long taskId) {
        if(taskService.getTaskByID(taskId) != null){
            taskService.updateTaskStatus(taskId);
            return ResponseEntity.ok().body("Task status was updated");
        } else {
            return ResponseEntity.badRequest().body("No task was found");
        }
        
    }

}
