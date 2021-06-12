package com.example.pam_project.features.tasks.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.ActivityResultCode;

import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity implements EditTaskView {
    private final static String TASK_ID_PARAMETER = "id";
    private EditTaskPresenter presenter;
    private long taskId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        String id = getIntent().getData().getQueryParameter(TASK_ID_PARAMETER);
        taskId = Long.parseLong(id);
        createPresenter();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_task);
        setDeleteButton();
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final TaskRepository taskRepository = container.getTasksRepository();
        presenter = new EditTaskPresenter(taskId, taskRepository, this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached();
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
                presenter.updateTask(taskName, taskDescription, taskUrgency);
                finish();
            }
        } else if (itemId == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDeleteButton(){
        Button deleteButton = (Button) (Button)findViewById(R.id.delete_task_button);
        deleteButton.setOnClickListener(v -> presenter.deleteTask(taskId));
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }
}
