package entity;

import java.time.LocalDate;

public class todo {
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean isCompleted;

    public todo(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return "[Title: " + title +
               ", Description: " + description +
               ", Due: " + dueDate +
               ", Completed: " + (isCompleted ? "Yes" : "No") + "]";
    }
}
