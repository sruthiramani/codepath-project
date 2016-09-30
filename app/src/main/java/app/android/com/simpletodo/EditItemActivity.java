package app.android.com.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    EditText etEditItem;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        position = getIntent().getIntExtra("position", 0);
        String value = getIntent().getStringExtra("value");
        etEditItem.setText(value);
        etEditItem.setSelection(value.length());

    }
    public void onSaveItem(View v) {

        String editedValue = etEditItem.getText().toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("edited-value", editedValue);
        resultIntent.putExtra("position", position);

        if(editedValue.isEmpty()) {
            setResult(RESULT_CANCELED, resultIntent);
        } else {
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }
}
