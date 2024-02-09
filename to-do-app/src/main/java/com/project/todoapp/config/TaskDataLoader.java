package com.project.todoapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.todoapp.model.Task;
import com.project.todoapp.repository.TaskRepository;

@Component
public class TaskDataLoader implements CommandLineRunner {
    @Autowired
    TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {
        loadStartData();
    }

    private void loadStartData() {
        if(taskRepository.count() == 0 || taskRepository == null){
            taskRepository.save(new Task("Complete online JavaScript course", true));
            taskRepository.save(new Task("Jog around the park 3x"));
            taskRepository.save(new Task("10 minutes meditation"));
            taskRepository.save(new Task("Read for 1 hour"));
            taskRepository.save(new Task("Pick up groceries"));
            taskRepository.save(new Task("Complete Todo App on Frontend Mentor"));
        }
    }
    
}
