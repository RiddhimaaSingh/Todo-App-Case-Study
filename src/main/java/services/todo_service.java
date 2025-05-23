package services;

import entity.todo;
import repository.todo_repo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class todo_service {
    private todo_repo repo = new todo_repo();

    public void createTodo(String title, String desc, String dateStr) {
        try {
            LocalDate dueDate = LocalDate.parse(dateStr); // expects yyyy-MM-dd
            todo t = new todo(title, desc, dueDate);
            repo.add(t);
            System.out.println("Todo added!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        } catch (Exception e) {
            System.out.println("Something went wrong while adding todo: " + e.getMessage());
        }
    }

    public void completeTodo(int index) {
        todo t = repo.get(index);
        if (t != null) {
            t.setCompleted(true);
            System.out.println("Todo marked as completed.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    public void viewTodos() {
        List<todo> list = repo.getAll();
        if (list.isEmpty()) {
            System.out.println("No todos found.");
            return;
        }

        int i = 0;
        for (todo t : list) {
            System.out.println(i + ". " + t);
            i++;
        }
    }

    public void deleteTodo(int index) {
        boolean deleted = repo.delete(index);
        if (deleted) {
            System.out.println("Todo deleted.");
        } else {
            System.out.println("Invalid index. Nothing deleted.");
        }
    }
}
