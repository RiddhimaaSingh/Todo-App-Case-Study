package controller;

import services.todo_service;

import java.util.InputMismatchException;
import java.util.Scanner;

public class todo_controller {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        todo_service service = new todo_service();
        sc.close(); 
        while (true) {
            try {
                System.out.println("\n=== Todo Menu ===");
                System.out.println("1. Add");
                System.out.println("2. View");
                System.out.println("3. Complete");
                System.out.println("4. Delete");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int ch = Integer.parseInt(sc.nextLine());

                switch (ch) {
                    case 1:
                        System.out.print("Title: ");
                        String title = sc.nextLine();
                        System.out.print("Description: ");
                        String desc = sc.nextLine();
                        System.out.print("Due Date (yyyy-MM-dd): ");
                        String date = sc.nextLine();
                        service.createTodo(title, desc, date);
                        break;

                    case 2:
                        service.viewTodos();
                        break;

                    case 3:
                        System.out.print("Index to complete: ");
                        int idx = Integer.parseInt(sc.nextLine());
                        service.completeTodo(idx);
                        break;

                    case 4:
                        System.out.print("Index to delete: ");
                        int idx1 = Integer.parseInt(sc.nextLine());
                        service.deleteTodo(idx1);
                        break;

                    case 5:
                        System.out.println("Quit");
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (InputMismatchException e) {
                System.out.println("Mismatch input type.");
                sc.nextLine(); // clear buffer
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
          }
}
