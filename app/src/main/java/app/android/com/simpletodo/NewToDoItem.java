package app.android.com.simpletodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewToDoItem extends AppCompatActivity {


    private int priorityValue;
    private static String dateString, failureString;
    private static EditText title, description;
    Spinner priority;
    DatePicker datePicker;
    long id;
    int requestCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todoitem);
        Button add, cancel;


        title = (EditText) findViewById(R.id.newTitle);
        description = (EditText) findViewById(R.id.newDescription);
        priority = (Spinner) findViewById(R.id.choosePriority);
        setupPrioritySpinner();

        datePicker = (DatePicker) findViewById(R.id.datePicker);


        dateString = "";
        Calendar cal=Calendar.getInstance();

        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(year, month, day);


        add = (Button) findViewById(R.id.btAddItem);
        cancel = (Button) findViewById(R.id.btCancelItem);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateString(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                if (false == validateInputData()) {
                    Toast.makeText(getBaseContext(), failureString, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent resultIntent = fillResultIntent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent();
                setResult(RESULT_CANCELED, cancelIntent);
                finish();
            }
        });

         requestCode = getIntent().getIntExtra("request-code",1);
        id = getIntent().getLongExtra("id",0);
        if(requestCode == MainActivity.REQUEST_EDIT_ITEM) {
            /* fill data */
            add.setText("Save");

            ToDoItem toDoItem = MainActivity.todoDB.getToDoItemById(id);
            title.setText(toDoItem.getTitle());
            title.setSelection(title.getText().length());
            description.setText(toDoItem.getDescription());
            switch (toDoItem.getTodoPriority()) {
                case CustomTodoListAdapter.LOW:
                   priority.setSelection(0);
                    break;
                case CustomTodoListAdapter.MEDIUM:
                    priority.setSelection(1);
                    break;
                case CustomTodoListAdapter.HIGH:
                  priority.setSelection(2);
                    break;
                default:
                   priority.setSelection(3);

            }
        }
    }


    @NonNull
    private Intent fillResultIntent() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Title", title.getText().toString());
        resultIntent.putExtra("Desc", description.getText().toString());
        resultIntent.putExtra("Priority", priorityValue);
        resultIntent.putExtra("id",id);
        resultIntent.putExtra("request-code",requestCode);

        Log.d("Date", dateString);
        resultIntent.putExtra("date", dateString);


        return resultIntent;
    }

    private boolean validateInputData() {
        if (title.getText().toString().isEmpty()) {
            failureString = " Please enter a title for the reminder";
            return false;
        }
        if (description.getText().toString().isEmpty()) {
            failureString = " Please enter a description for the reminder";
            return false;
        }



        if (dateString.isEmpty()) {
            failureString = " Please choose a due date for the reminder";
            return false;
        }

        return true;
    }
    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        dateString = year + "-" + mon + "-" + day;
    }


    private void setupPrioritySpinner() {

        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priorityValue = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner Drop down elements
        List<String> priorityList = new ArrayList<String>();

        priorityList.add("Low");
        priorityList.add("Medium");
        priorityList.add("High");
        priorityList.add("None");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorityList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        priority.setAdapter(dataAdapter);
    }
}



