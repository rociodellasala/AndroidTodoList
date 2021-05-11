package com.example.pam_project.lists.tasks.editTaskActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.db.repositories.RoomTaskRepository;
import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;
import com.example.pam_project.lists.tasks.components.TaskInformation;

import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity implements EditTaskView {

    private final static String TASK_ID_PARAMETER = "id";
    private EditTaskPresenter editTaskPresenter;
    private long taskId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getIntent().getData().getQueryParameter(TASK_ID_PARAMETER);
        this.taskId = Long.parseLong(id);

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();
        final TaskRepository taskRepository = new RoomTaskRepository(mainStorage.getStorage().taskDao());

        editTaskPresenter = new EditTaskPresenter(taskId, taskRepository, this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_category);
        setContentView(R.layout.activity_edit_task);
    }

    @Override
    protected void onStart() {
        super.onStart();
        editTaskPresenter.onViewAttached();
    }

    @Override
    public void bindTask(TaskInformation model) {
        final EditText name = findViewById(R.id.edit_task_title_input);
        final EditText description = findViewById(R.id.edit_task_description_input);
        final CheckBox urgency = findViewById(R.id.edit_task_priority_checkbox);
        name.setText(model.getTitle());
        description.setText(model.getDescription());
        urgency.setChecked(model.getUrgency());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long itemId = item.getItemId();
        final EditText taskNameInput = findViewById(R.id.edit_task_title_input);
        final EditText taskDescriptionInput = findViewById(R.id.edit_task_description_input);
        final CheckBox checkboxUrgencyInput = findViewById(R.id.edit_task_priority_checkbox);

        if (itemId == R.id.check_add_button) {
            String taskName = taskNameInput.getText().toString();
            String taskDescription = taskDescriptionInput.getText().toString();
            boolean taskUrgency = checkboxUrgencyInput.isChecked();
            String errorMessage = checkForm(taskName);

            if (errorMessage != null) {
                taskNameInput.setError(errorMessage);
            } else {
                if (!taskName.isEmpty()) {
                    editTaskPresenter.editTask(taskName, taskDescription, taskUrgency);
                } else {
                    this.onFailedUpdate();
                }
                finish();
            }
        } else if (itemId == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private String checkForm(String categoryName) {
        String errorMessage = null;

        if (categoryName == null || categoryName.trim().isEmpty()) {
            errorMessage = getString(R.string.error_empty_input);
        }

        return errorMessage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public void onSuccessfulUpdate(final String name, final String description, final boolean priority) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("taskName", name);
        returnIntent.putExtra("taskDescription", description);
        returnIntent.putExtra("priority", priority);
        setResult(Activity.RESULT_OK, returnIntent);
    }

    @Override
    public void onFailedUpdate() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
