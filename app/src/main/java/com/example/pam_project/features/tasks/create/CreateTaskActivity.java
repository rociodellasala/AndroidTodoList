package com.example.pam_project.features.tasks.create;

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
import com.example.pam_project.utils.FormValidator;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.util.HashMap;
import java.util.Map;
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
        final SchedulerProvider schedulerProvider = container.getSchedulerProvider();
        final TaskRepository taskRepository = container.getTasksRepository();
        presenter = new CreateTaskPresenter(schedulerProvider, taskRepository, this);
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
            boolean validForm = FormValidator.validate(getApplicationContext(), createInputMap(taskName, taskNameInput));

            if (validForm) {
                presenter.insertTask(taskName, taskDescription, taskUrgency, listId);
                finish();
            }
        } else if (itemId == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private Map<EditText, String> createInputMap(String taskName, EditText taskNameInput) {
        Map<EditText, String> map = new HashMap<>();
        map.put(taskNameInput, taskName);
        return map;
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