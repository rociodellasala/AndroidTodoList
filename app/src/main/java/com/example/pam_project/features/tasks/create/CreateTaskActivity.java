package com.example.pam_project.features.tasks.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.ActivityResultCode;

import java.util.Objects;

public class CreateTaskActivity extends AppCompatActivity implements CreateTaskView {
    private final static String LIST_ID_PARAMETER = "id";
    private CreateTaskPresenter presenter;
    private Long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        createPresenter();

        listId = getIntent().getLongExtra(LIST_ID_PARAMETER, -1);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_create_task);
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final TaskRepository taskRepository = container.getTasksRepository();
        presenter = new CreateTaskPresenter(taskRepository, this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
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
            if (errorMessage != null) {
                taskNameInput.setError(errorMessage);
            } else {
                presenter.insertTask(taskName, taskDescription, taskUrgency, listId);
                finish();
            }
        } else if (itemId == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private String checkForm(String taskName) {
        String errorMessage = null;

        if (taskName == null || taskName.trim().isEmpty()) {
            errorMessage = getString(R.string.error_empty_input);
        }

        return errorMessage;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }
}