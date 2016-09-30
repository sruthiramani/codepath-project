package app.android.com.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Sruthi on 8/28/2016.
 */
public class CustomTodoListAdapter extends ArrayAdapter<ToDoItem> {
    public static final int LOW = 0, MEDIUM = 1, HIGH = 2, NONE = 3;
    Context context;

    public CustomTodoListAdapter(Context context, int resourceId, List<ToDoItem> objects) {
        super(context, resourceId, objects);
       this.context = context;
    }
    public class ViewHolder {
        TextView remTitle;
        CheckBox remStatus;
        TextView remPriority;
        TextView remDescription;
        TextView remDueDate;
        Button edit;
        TextView remStatusLabel;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ToDoItem rowItem = getItem(position);


        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.todo_item, null);
            convertView.setBackgroundColor(getAlternateColor(position));
            holder = new ViewHolder();
            holder.remTitle = (TextView) convertView.findViewById(R.id.titleView);
            holder.remStatusLabel = (TextView) convertView.findViewById(R.id.statusLabel);
            holder.remStatus = (CheckBox) convertView.findViewById(R.id.statusCheckBox);
            holder.remPriority = (TextView) convertView.findViewById(R.id.priorityView);
            holder.remDescription = (TextView) convertView.findViewById(R.id.descriptionView);
            holder.remDueDate = (TextView)convertView.findViewById(R.id.dateView);
            holder.edit = (Button)convertView.findViewById(R.id.btEditItem);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.remTitle.setText(rowItem.getTitle());
        holder.remDescription.setText(rowItem.getDescription());

        switch (rowItem.getTodoPriority()) {
            case LOW:
                holder.remPriority.setText("Low");
                break;
            case MEDIUM:
                holder.remPriority.setText("Medium");
                break;
            case HIGH:
                holder.remPriority.setText("High");
                break;
            default:
                holder.remPriority.setText("None ");

        }
       // holder.remDueDate.setText(new String(getDateFromMilliSec(rowItem.getTodoDueDate())));
        Log.d("Date here", rowItem.getTodoDueDate());
        holder.remDueDate.setText(rowItem.getTodoDueDate());

        holder.remStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("App", rowItem.getId()+"status"+rowItem.getTodoStatus()+"ischecked"+isChecked);
                rowItem.setToDoStatus(isChecked);
                MainActivity.todoDB.updateToDoItemStatus(rowItem.getId(),isChecked);
            }
        });
        holder.remStatus.setChecked(rowItem.getTodoStatus());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"Yet to implement..", Toast.LENGTH_SHORT).show();;

            }
        });
        return convertView;
    }

    private String getDateFromMilliSec(long reminderDueDateInMilliSec) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(reminderDueDateInMilliSec);
        return new String(cal.get(Calendar.YEAR) +"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DAY_OF_MONTH));
    }

    public int getAlternateColor(int position) {
        if(position%2 == 0) {
            return Color.rgb(100,149,237);
        } else {
            return Color.rgb(135,206,250);
        }
    }
}
