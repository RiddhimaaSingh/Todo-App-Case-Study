package repository;

import entity.Todo;
import util.FileHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * File-based implementation of TodoRepository.
 */
public class FileTodoRepository implements TodoRepository {
    private static final String FILE_NAME = "todos.dat";
    private final FileHandler fileHandler;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public FileTodoRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        initializeIdGenerator();
    }

    private void initializeIdGenerator() {
        long maxId = findAll().stream()
                .mapToLong(Todo::getId)
                .max()
                .orElse(0);
        idGenerator.set(maxId + 1);
    }

    @Override
    public Todo save(Todo todo) {
        List<Todo> todos = findAll();
        
        if (todo.getId() == null) {
            todo.setId(idGenerator.getAndIncrement());
            todos.add(todo);
        } else {
            todos.removeIf(t -> t.getId().equals(todo.getId()));
            todos.add(todo);
        }
        
        saveAll(todos);
        return todo;
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return findAll().stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Todo> findAll() {
        try {
            List<String> lines = fileHandler.readLines(FILE_NAME);
            List<Todo> todos = new ArrayList<>();
            
            for (String line : lines) {
                try {
                    todos.add(deserialize(line));
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing todo data: " + e.getMessage());
                }
            }
            
            return todos;
        } catch (IOException e) {
            System.err.println("Error reading todos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Todo> findByCompleted(boolean completed) {
        return findAll().stream()
                .filter(todo -> todo.isCompleted() == completed)
                .toList();
    }

    @Override
    public List<Todo> findByPriority(Todo.Priority priority) {
        return findAll().stream()
                .filter(todo -> todo.getPriority() == priority)
                .toList();
    }

    @Override
    public List<Todo> findByOverdue() {
        LocalDate today = LocalDate.now();
        return findAll().stream()
                .filter(todo -> !todo.isCompleted() && todo.getDueDate() != null && todo.getDueDate().isBefore(today))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        List<Todo> todos = findAll();
        todos.removeIf(todo -> todo.getId().equals(id));
        saveAll(todos);
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public void deleteAll() {
        try {
            fileHandler.writeLines(FILE_NAME, new ArrayList<>());
        } catch (IOException e) {
            System.err.println("Error clearing todos: " + e.getMessage());
        }
    }

    private void saveAll(List<Todo> todos) {
        try {
            List<String> lines = new ArrayList<>();
            for (Todo todo : todos) {
                lines.add(serialize(todo));
            }
            fileHandler.writeLines(FILE_NAME, lines);
        } catch (IOException e) {
            System.err.println("Error saving todos: " + e.getMessage());
        }
    }

    private String serialize(Todo todo) {
        return String.join(",",
                todo.getId().toString(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getDueDate() != null ? todo.getDueDate().toString() : "",
                todo.getPriority().name(),
                Boolean.toString(todo.isCompleted()));
    }

    private Todo deserialize(String line) throws DateTimeParseException {
        String[] parts = line.split(",", -1); // -1 to keep trailing empty strings
        
        Todo todo = new Todo();
        todo.setId(Long.parseLong(parts[0]));
        todo.setTitle(parts[1]);
        todo.setDescription(parts[2]);
        todo.setDueDate(parts[3].isEmpty() ? null : LocalDate.parse(parts[3]));
        todo.setPriority(Todo.Priority.valueOf(parts[4]));
        todo.setCompleted(Boolean.parseBoolean(parts[5]));
        
        return todo;
    }
}