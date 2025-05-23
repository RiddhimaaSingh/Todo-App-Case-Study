package controller;

import entity.Todo;
import service.TodoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Controller for handling user input and coordinating between view and service.
 */
public class TodoController {
    private final TodoService todoService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    }

    public void createTodo() {
        System.out.println("\nCreate New Todo");
        
        String title = promptForString("Title: ", false);
        String description = promptForString("Description: ", true);
        LocalDate dueDate = promptForDate("Due date (YYYY-MM-DD, optional): ", true);
        Todo.Priority priority = promptForPriority();
        
        Todo todo = todoService.createTodo(title, description, dueDate, priority);
        System.out.println("\nTodo created successfully:");
        printTodoDetails(todo);
    }

    public void listAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        printTodoList("All Todos", todos);
    }

    public void listPendingTodos() {
        List<Todo> todos = todoService.getPendingTodos();
        printTodoList("Pending Todos", todos);
    }

    public void listCompletedTodos() {
        List<Todo> todos = todoService.getCompletedTodos();
        printTodoList("Completed Todos", todos);
    }

    public void listTodosByPriority() {
        Todo.Priority priority = promptForPriority();
        List<Todo> todos = todoService.getTodosByPriority(priority);
        printTodoList("Todos with Priority: " + priority, todos);
    }

    public void listOverdueTodos() {
        List<Todo> todos = todoService.getOverdueTodos();
        printTodoList("Overdue Todos", todos);
    }

    public void viewTodoDetails() {
        Long id = promptForLong("Enter Todo ID: ");
        try {
            Todo todo = todoService.getTodoById(id);
            printTodoDetails(todo);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateTodo() {
        Long id = promptForLong("Enter Todo ID to update: ");
        
        try {
            Todo existingTodo = todoService.getTodoById(id);
            System.out.println("Current Todo Details:");
            printTodoDetails(existingTodo);
            
            String title = promptForString("New Title (leave blank to keep current): ", true);
            String description = promptForString("New Description (leave blank to keep current): ", true);
            LocalDate dueDate = promptForDate("New Due Date (YYYY-MM-DD, leave blank to keep current): ", true);
            Todo.Priority priority = promptForPriorityOptional();
            Boolean completed = promptForBooleanOptional("Mark as completed? (y/n, leave blank to keep current): ");
            
            Todo updatedTodo = todoService.updateTodo(
                    id,
                    title.isEmpty() ? null : title,
                    description.isEmpty() ? null : description,
                    dueDate,
                    priority,
                    completed
            );
            
            System.out.println("\nTodo updated successfully:");
            printTodoDetails(updatedTodo);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteTodo() {
        Long id = promptForLong("Enter Todo ID to delete: ");
        try {
            Todo todo = todoService.getTodoById(id);
            System.out.println("You are about to delete this todo:");
            printTodoDetails(todo);
            
            if (promptForBoolean("Are you sure you want to delete? (y/n): ")) {
                todoService.deleteTodo(id);
                System.out.println("Todo deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showStatistics() {
        long total = todoService.getTotalTodoCount();
        long completed = todoService.getCompletedTodos().size();
        long pending = todoService.getPendingTodos().size();
        long overdue = todoService.getOverdueTodos().size();
        
        System.out.println("\nTodo Statistics:");
        System.out.println("Total Todos: " + total);
        System.out.println("Completed: " + completed);
        System.out.println("Pending: " + pending);
        System.out.println("Overdue: " + overdue);
    }

    private void printTodoList(String title, List<Todo> todos) {
        System.out.println("\n" + title + " (" + todos.size() + ")");
        if (todos.isEmpty()) {
            System.out.println("No todos found.");
        } else {
            todos.forEach(todo -> System.out.printf("%d. [%s] %s (Due: %s, Priority: %s)%n",
                    todo.getId(),
                    todo.isCompleted() ? "X" : " ",
                    todo.getTitle(),
                    todo.getDueDate() != null ? dateFormatter.format(todo.getDueDate()) : "No due date",
                    todo.getPriority()));
        }
    }

    private void printTodoDetails(Todo todo) {
        System.out.println("\nTodo Details:");
        System.out.println("ID: " + todo.getId());
        System.out.println("Title: " + todo.getTitle());
        System.out.println("Description: " + todo.getDescription());
        System.out.println("Due Date: " + (todo.getDueDate() != null ? dateFormatter.format(todo.getDueDate()) : "No due date"));
        System.out.println("Priority: " + todo.getPriority());
        System.out.println("Status: " + (todo.isCompleted() ? "Completed" : "Pending"));
        System.out.println("Overdue: " + (todo.isOverdue() ? "Yes" : "No"));
    }

    private String promptForString(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!allowEmpty && input.isEmpty()) {
                System.out.println("This field cannot be empty. Please try again.");
            } else {
                return input;
            }
        }
    }

    private LocalDate promptForDate(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (allowEmpty && input.isEmpty()) {
                return null;
            }
            
            try {
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    private Todo.Priority promptForPriority() {
        while (true) {
            System.out.println("Priority:");
            for (Todo.Priority p : Todo.Priority.values()) {
                System.out.printf("%d. %s%n", p.ordinal() + 1, p);
            }
            System.out.print("Select priority (1-" + Todo.Priority.values().length + "): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= Todo.Priority.values().length) {
                    return Todo.Priority.values()[choice - 1];
                }
                System.out.println("Invalid choice. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private Todo.Priority promptForPriorityOptional() {
        System.out.println("Priority (leave blank to keep current):");
        for (Todo.Priority p : Todo.Priority.values()) {
            System.out.printf("%d. %s%n", p.ordinal() + 1, p);
        }
        System.out.print("Select priority (1-" + Todo.Priority.values().length + "), or blank to skip: ");
        
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        
        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= Todo.Priority.values().length) {
                return Todo.Priority.values()[choice - 1];
            }
            System.out.println("Invalid choice. Keeping current priority.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Keeping current priority.");
        }
        return null;
    }

    private boolean promptForBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    private Boolean promptForBooleanOptional(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.isEmpty()) {
                return null;
            } else if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y', 'n', or leave blank.");
        }
    }

    private Long promptForLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}