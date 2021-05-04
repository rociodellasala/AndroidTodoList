package com.example.pam_project.lists.tasks.createTaskActivity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pam_project.R;
import com.example.pam_project.db.mappers.TaskMapper;
import com.example.pam_project.db.repositories.RoomTaskRepository;
import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Objects;

public class CreateTaskActivity extends AppCompatActivity implements CreateTaskView {

    private CreateTaskPresenter createTaskPresenter;
    private long listId;

    private final static String  LIST_ID_PARAMETER = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();
        final TaskMapper taskMapper = new TaskMapper();
        final TaskRepository taskRepository = new RoomTaskRepository(mainStorage.getStorage().taskDao(), taskMapper);

        createTaskPresenter = new CreateTaskPresenter(taskRepository, this);
        listId = getIntent().getLongExtra(LIST_ID_PARAMETER, -1);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_create_task);
        setContentView(R.layout.activity_create_task);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public void onSuccessfulInsert(final long id) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
    }

    @Override
    public void onFailedInsert(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long itemId = item.getItemId();
        final EditText taskNameInput = findViewById(R.id.create_task_title_input);
        final EditText taskDescriptionInput = findViewById(R.id.create_task_description_input);
        final CheckBox checkboxUrgencyInput = findViewById(R.id.create_task_priority_checkbox);

        if (itemId == R.id.check_add_button) {
            String taskName = taskNameInput.getText().toString();
            String taskDescription = taskDescriptionInput.getText().toString();
            boolean taskUrgency = checkboxUrgencyInput.isChecked();

            String errorMessage = checkForm(taskName);
            if(errorMessage != null) {
                taskNameInput.setError(errorMessage);
            } else {
                createTaskPresenter.insertTask(taskName, taskDescription, taskUrgency, listId);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private String checkForm(String taskName) {
        String errorMessage = null;

        if(taskName == null || taskName != null && taskName.trim().isEmpty()) {
            errorMessage = getString(R.string.error_empty_input);
        }

        return errorMessage;
    }

    @Override
    public void onStop(){
        super.onStop();
    }
}