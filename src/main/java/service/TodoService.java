package service;

import entity.Todo;
import repository.TodoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Todo operations.
 */
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo createTodo(String title, String description, LocalDate dueDate, Todo.Priority priority) {
        Todo todo = new Todo(title, description, dueDate, priority);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, String title, String description, LocalDate dueDate, Todo.Priority priority, Boolean completed) {
        return todoRepository.findById(id)
                .map(todo -> {
                    if (title != null) todo.setTitle(title);
                    if (description != null) todo.setDescription(description);
                    if (dueDate != null) todo.setDueDate(dueDate);
                    if (priority != null) todo.setPriority(priority);
                    if (completed != null) todo.setCompleted(completed);
                    return todoRepository.save(todo);
                })
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + id));
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public List<Todo> getPendingTodos() {
        return todoRepository.findByCompleted(false);
    }

    public List<Todo> getCompletedTodos() {
        return todoRepository.findByCompleted(true);
    }

    public List<Todo> getTodosByPriority(Todo.Priority priority) {
        return todoRepository.findByPriority(priority);
    }

    public List<Todo> getOverdueTodos() {
        return todoRepository.findByOverdue();
    }

    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + id));
    }

    public long getTotalTodoCount() {
        return todoRepository.count();
    }

    public void clearAllTodos() {
        todoRepository.deleteAll();
    }
}