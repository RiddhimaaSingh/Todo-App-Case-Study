package com.cg.ToDoApplication.ToDoApplication.cg.com;

import controller.TodoController;
import repository.FileTodoRepository;
import service.TodoService;
import util.FileHandler;

import java.util.Scanner;

/**
 * Main application class for the Todo Console App.
 */
public class App {
    private final TodoController todoController;
    private final Scanner scanner;

    public App(TodoController todoController) {
        this.todoController = todoController;
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        FileTodoRepository todoRepository = new FileTodoRepository(fileHandler);
        TodoService todoService = new TodoService(todoRepository);
        TodoController todoController = new TodoController(todoService);
        
        App app = new App(todoController);
        app.run();
    }

    public void run() {
        System.out.println("=== Todo Console Application ===");
        
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    todoController.createTodo();
                    break;
                case "2":
                    todoController.listAllTodos();
                    break;
                case "3":
                    todoController.listPendingTodos();
                    break;
                case "4":
                    todoController.listCompletedTodos();
                    break;
                case "5":
                    todoController.listTodosByPriority();
                    break;
                case "6":
                    todoController.listOverdueTodos();
                    break;
                case "7":
                    todoController.viewTodoDetails();
                    break;
                case "8":
                    todoController.updateTodo();
                    break;
                case "9":
                    todoController.deleteTodo();
                    break;
                case "10":
                    todoController.showStatistics();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        System.out.println("Goodbye!");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Create new Todo");
        System.out.println("2. List all Todos");
        System.out.println("3. List pending Todos");
        System.out.println("4. List completed Todos");
        System.out.println("5. List Todos by priority");
        System.out.println("6. List overdue Todos");
        System.out.println("7. View Todo details");
        System.out.println("8. Update Todo");
        System.out.println("9. Delete Todo");
        System.out.println("10. Show statistics");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
}