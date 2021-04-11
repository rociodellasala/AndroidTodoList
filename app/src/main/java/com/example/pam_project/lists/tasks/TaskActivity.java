package com.example.pam_project.lists.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.utils.TaskStatus;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TaskActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPending;
    private RecyclerView recyclerViewDone;
    private TaskAdapterPending adapterPending;
    private TaskAdapterDone adapterDone;
    private List<TaskInformation> contentListPending;
    private List<TaskInformation> contentListDone;
    private final int CREATE_TASK_ACTIVITY_REGISTRY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lista x"); // CAMBIAR DSPP
        setContentView(R.layout.activity_task);
        setup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_action_bar, menu);
        return true;
    }

    private void setup() {
        recyclerViewPending = findViewById(R.id.pendingTasks);
        recyclerViewPending.setHasFixedSize(true);
        recyclerViewDone = findViewById(R.id.doneTasks);
        recyclerViewDone.setHasFixedSize(true);
        contentListPending = createDataSetPending();
        contentListDone = createDataSetDone();
        adapterPending = new TaskAdapterPending(contentListPending);
        adapterDone = new TaskAdapterDone(contentListDone);
        recyclerViewPending.setAdapter(adapterPending);
        recyclerViewPending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewDone.setAdapter(adapterDone);
        recyclerViewDone.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setExtendedFloatingButtonAction();
    }

    private List<TaskInformation> createDataSetPending() {
        final List<TaskInformation> content = new ArrayList<>();

        for(int i = 0; i < 15; i++) {
            Random randomUrgency = new Random();
            TaskInformation information = new TaskInformation("Task  " + i, "Description", randomUrgency.nextBoolean(),
                    TaskStatus.PENDING);
            content.add(information);
        }

        return content;
    }

    private List<TaskInformation> createDataSetDone() {
        final List<TaskInformation> content = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            Random randomUrgency = new Random();
            TaskInformation information = new TaskInformation("Task  " + i, "Description", randomUrgency.nextBoolean(),
                    TaskStatus.DONE);
            content.add(information);
        }

        return content;
    }

    private void setExtendedFloatingButtonAction(){
        ExtendedFloatingActionButton addListFAB = findViewById(R.id.extended_fab_add_task);
        addListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                startActivityForResult(activityIntent, CREATE_TASK_ACTIVITY_REGISTRY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_TASK_ACTIVITY_REGISTRY) {
            if(resultCode == Activity.RESULT_OK){
                String newTaskTitle = data.getStringExtra("taskTitle");
                String newTaskDescription = data.getStringExtra("taskDescription");
                contentListPending.add(new TaskInformation("Task name " + newTaskTitle, newTaskDescription, false, TaskStatus.PENDING));
                adapterPending.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
