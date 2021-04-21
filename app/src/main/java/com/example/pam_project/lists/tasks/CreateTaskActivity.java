package com.example.pam_project.lists.tasks;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pam_project.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Objects;

public class CreateTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_create_task);
        setContentView(R.layout.activity_create_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();
        final EditText taskTitleInput = findViewById(R.id.create_task_title_input);
        final EditText taskDescriptionInput = findViewById(R.id.create_task_description_input);

        if (id == R.id.check_add_button) {
            String taskTile = taskTitleInput.getText().toString();
            String taskDescription = taskDescriptionInput.getText().toString();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("taskTitle", taskTile);
            returnIntent.putExtra("taskDescription", taskDescription);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}