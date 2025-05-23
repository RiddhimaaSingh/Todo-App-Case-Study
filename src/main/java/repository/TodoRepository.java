package repository;


import java.util.List;
import java.util.Optional;

import entity.Todo;

/**
 * Repository interface for Todo CRUD operations.
 */
public interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(Long id);
    List<Todo> findAll();
    List<Todo> findByCompleted(boolean completed);
    List<Todo> findByPriority(Todo.Priority priority);
    List<Todo> findByOverdue();
    void deleteById(Long id);
    long count();
    void deleteAll();
}