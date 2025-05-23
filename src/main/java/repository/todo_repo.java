package repository;

import entity.todo;
import java.util.ArrayList;
import java.util.List;

public class todo_repo {
    private List<todo> todos = new ArrayList<>();

    public void add(todo t) {
        todos.add(t);
    }

    public todo get(int index) {
        if (index >= 0 && index < todos.size()) {
            return todos.get(index);
        }
        return null;
    }

    public List<todo> getAll() {
        return todos;
    }

    public boolean delete(int index) {
        if (index >= 0 && index < todos.size()) {
            todos.remove(index);
            return true;
        }
        return false;
    }
}
