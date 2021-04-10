package com.example.pam_project.lists.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<TaskInformation> contentList;
    private final int CREATE_TASK_ACTIVITY_REGISTRY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lista x"); // CAMBIAR DSPP
        setContentView(R.layout.activity_task);
        setup();
    }

    private void setup() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        contentList = createDataSet();
        adapter = new TaskAdapter(createDataSet());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setExtendedFloatingButtonAction();
    }

    private List<TaskInformation> createDataSet() {
        final List<TaskInformation> content = new ArrayList<>();

        for(int i = 0; i < 6; i++) {
            Random randomUrgency = new Random();
            TaskStatus status = TaskStatus.DONE;
            TaskInformation information = new TaskInformation("Task  " + i, "Description", randomUrgency.nextBoolean(),
                    status);
            content.add(information);
        }

        for(int i = 0; i < 6; i++) {
            Random randomUrgency = new Random();
            TaskStatus status = TaskStatus.PENDING;
            TaskInformation information = new TaskInformation("Task  " + i, "Description", randomUrgency.nextBoolean(),
                    status);
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
//                contentList.add(new TaskInformation("Task name " + newTaskTitle, newTaskDescription, false));
                contentList = null;
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
