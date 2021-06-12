package com.example.pam_project.features.tasks.edit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private EditTaskPresenter editTaskPresenter;
    private long taskId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getIntent().getData().getQueryParameter(TASK_ID_PARAMETER);
        this.taskId = Long.parseLong(id);

        final ApplicationContainer container = ApplicationContainerLocator
                .locateComponent(this);
        final TaskRepository taskRepository = container.getTasksRepository();

        editTaskPresenter = new EditTaskPresenter(taskId, taskRepository, this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_task);
        setContentView(R.layout.activity_edit_task);
        setDeleteButton();
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

        Log.e("msg:", String.valueOf(itemId));

        if (itemId == R.id.check_add_button) {
            String taskName = taskNameInput.getText().toString();
            String taskDescription = taskDescriptionInput.getText().toString();
            boolean taskUrgency = checkboxUrgencyInput.isChecked();
            String errorMessage = checkForm(taskName);

            if (errorMessage != null) {
                taskNameInput.setError(errorMessage);
            } else {
                if (!taskName.isEmpty())
                    editTaskPresenter.updateTask(taskName, taskDescription, taskUrgency);
            }
        } else if (itemId == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDeleteButton(){
        Button deleteButton = (Button) (Button)findViewById(R.id.delete_task_button);
        deleteButton.setOnClickListener(v -> editTaskPresenter.deleteTask(taskId));
    }

    private String checkForm(String categoryName) {
        String errorMessage = null;

        if (categoryName == null || categoryName.trim().isEmpty()) {
            errorMessage = getString(R.string.error_empty_input);
        }

        return errorMessage;
    }

    @Override
    public void onTaskEdit(){
        Intent returnIntent = new Intent();
        setResult(ActivityResultCode.EDIT_TASK_CODE.ordinal(), returnIntent);
        finish();
    }

    @Override
    public void onTaskDelete(){
        Intent returnIntent = new Intent();
        setResult(ActivityResultCode.DELETE_TASK_CODE.ordinal(), returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        editTaskPresenter.onViewDetached();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
