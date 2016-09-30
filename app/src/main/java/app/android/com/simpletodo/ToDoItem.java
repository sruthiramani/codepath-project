package app.android.com.simpletodo;

/**
 * Created by Sruthi on 9/29/2016.
 */
public class ToDoItem {
    public static final boolean DONE = true, NOT_DONE = false;
    long id;
    private String title;
    private String description;
    private boolean todoStatus;
    private int todoPriority;
    private String todoDueDate;

    ToDoItem(long id,
             String title,
             String description,
             boolean todoStatus,
             int todoPriority,
             String todoDueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.todoStatus = todoStatus;
        this.todoPriority = todoPriority;
        this.todoDueDate = todoDueDate;


    }

    public String getTitle() {
        return  title;
    }
    public boolean getTodoStatus() {
        return  todoStatus;
    }

    public int getTodoPriority() {
        return todoPriority;
    }
    public String getDescription() {
        return description;
    }

    public  String getTodoDueDate() { return  todoDueDate;}

    public long getId() {
        return id;
    }


    public void setToDoStatus(boolean flag) {
        this.todoStatus = flag;
    }
}