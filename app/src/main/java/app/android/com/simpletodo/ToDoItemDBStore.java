package app.android.com.simpletodo;

/**
 * Created by Sruthi on 8/31/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

public class ToDoItemDBStore extends  SQLiteOpenHelper{

    public static final String DATABASE_NAME = "SimpleToDo.db";
    public static final String TABLE_NAME = "TodoItems";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DUE_DATE_IN_MS = "duedate";

    public ToDoItemDBStore(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " +TABLE_NAME+
                        "("+COLUMN_ID+" integer primary key,"+COLUMN_TITLE+ " text," + COLUMN_DESCRIPTION+" text,"+COLUMN_PRIORITY+" integer,"+COLUMN_STATUS+" integer,"+COLUMN_DUE_DATE_IN_MS+" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertToDoItem(String title, String desc, int priority, boolean status,String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, desc);
        contentValues.put(COLUMN_PRIORITY, priority);
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_DUE_DATE_IN_MS,dueDate);
        long id = db.insert(TABLE_NAME, null, contentValues);
        return id;
    }

    public ArrayList<ToDoItem> getToDoItems() {
        ArrayList<ToDoItem> arrayList = new ArrayList<ToDoItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME , null);
        res.moveToFirst();
        while (res.isAfterLast()==false) {
            ToDoItem entry = getToDoItemFromCursor(res);
            arrayList.add(entry);
            res.moveToNext();
        }
        return arrayList;
    }

    @NonNull
    private ToDoItem getToDoItemFromCursor(Cursor res) {
        long id = Long.parseLong(res.getString(res.getColumnIndex(COLUMN_ID)));
        String title = res.getString(res.getColumnIndex(COLUMN_TITLE));
        String desc = res.getString(res.getColumnIndex(COLUMN_DESCRIPTION));
        int priority = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_PRIORITY)));
        int st = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_STATUS)));
        boolean status = false;
        if(st == 1) status = true;
       //long dateInMS = Long.parseLong(res.getString(res.getColumnIndex(COLUMN_DUE_DATE_IN_MS)));
        String date = res.getString(res.getColumnIndex(COLUMN_DUE_DATE_IN_MS));
        return new ToDoItem(id,title,desc,status,priority,date);
    }

    public int updateToDoItemStatus(long id, boolean value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, value);
        return db.update(TABLE_NAME, values, COLUMN_ID + " ="+id,null);

    }

    public boolean deleteToDoItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
         return db.delete(TABLE_NAME,COLUMN_ID + "=" + id, null)>0;

    }

    public ToDoItem getToDoItemById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME +" where "+COLUMN_ID + "=" +id, null);
        res.moveToFirst();
        ToDoItem entry = getToDoItemFromCursor(res);
        return  entry;
    }
}
