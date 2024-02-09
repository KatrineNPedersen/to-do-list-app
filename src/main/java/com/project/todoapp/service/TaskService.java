package com.project.todoapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.todoapp.model.Task;
import com.project.todoapp.repository.TaskRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task addTask(Task task){
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId){
        taskRepository.deleteById(taskId);
    }

    public List<Task> findAllByCompleted(boolean completed) {
        return taskRepository.findAllByCompleted(completed);

    }

    public void deleteCompletedTasks(){
        taskRepository.deleteAllByCompleted(true);
    }

    public Task updateTaskStatus(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isPresent()){
            Task updatedTask = optionalTask.get();
            boolean status = updatedTask.isCompleted();
            updatedTask.setCompleted(!status);
            taskRepository.save(updatedTask);
            return updatedTask;
        } else {
            return null;
        }
    }

    public Task getTaskByID(Long taskId){
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isPresent()){
            return taskRepository.findById(taskId).get();
        } else {
            return null;
        }
       
    }
}
