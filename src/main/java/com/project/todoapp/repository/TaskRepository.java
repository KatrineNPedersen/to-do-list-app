package com.project.todoapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.todoapp.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByCompleted(boolean completed);
    List<Task> deleteAllByCompleted(boolean completed);
}
