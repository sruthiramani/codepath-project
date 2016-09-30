package app.android.com.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_EDIT_ITEM = 2;
    public static final int REQUEST_ADD_ITEM = 1;
    ArrayList<ToDoItem> items;
    CustomTodoListAdapter itemsAdapter;
    ListView lvItems;
    public static ToDoItemDBStore todoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoDB = new ToDoItemDBStore(this);
        setupListView();
       setupListViewListener();

    }

    private void setupListView() {

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = todoDB.getToDoItems();
        itemsAdapter = new CustomTodoListAdapter(getBaseContext(), R.layout.todo_item, items);
        lvItems.setAdapter(itemsAdapter);

    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("App", "Long press detected");
                ToDoItem removedItem = items.get(position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                todoDB.deleteToDoItem(removedItem.getId());
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("App", "click detected");
               // Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
                Intent editIntent = new Intent(MainActivity.this, NewToDoItem.class);
                editIntent.putExtra("id", items.get(position).getId());
                editIntent.putExtra("request-code", REQUEST_EDIT_ITEM);
                startActivityForResult(editIntent, REQUEST_EDIT_ITEM);
            }
        });
    }
    public void onAddTodoItem(View v) {
        Intent newToDoIntent = new Intent(this, NewToDoItem.class);
        startActivityForResult(newToDoIntent, REQUEST_ADD_ITEM);

    }

/*
    public void onAddItem(View v) {

        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        ToDoItem item = null;
        if(item != null) {
            items.add(item);
           writeItems();
            etNewItem.setText("");
        } else {
            Toast.makeText(this, "Empty to-do item not added to the list", Toast.LENGTH_SHORT).show();
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException e) {
            items = new ArrayList<String>();
        }
    }
    private  void  writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            Log.d("App", "Writing");
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_EDIT_ITEM) {
            Log.d("App", "received edited values 1");
            if (resultCode == RESULT_OK) {
                Log.d("App", "received edited values");
                itemsAdapter.notifyDataSetChanged();
            } else if( resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Empty text not updated in to-do item", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == REQUEST_ADD_ITEM) {
            if(resultCode == RESULT_OK) {
                String title = data.getStringExtra("Title");
                String desc = data.getStringExtra("Desc");
                int priority = data.getIntExtra("Priority", 0);
                String date = data.getStringExtra("date");
                Log.d("date",date);
                long id = todoDB.insertToDoItem(title, desc, priority, false, date);
                ToDoItem item1 = new ToDoItem(id,title, desc,false,priority,date);
                items.add(item1);
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }
}
