package com.example.pam_project.lists.tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.WelcomeActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private static final String FTU_KEY = "is_ftu";
    private static final String PAM_PREF = "app-pref";

    private RecyclerView recyclerView;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        setup();
    }

    private void setup() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        adapter = new TaskAdapter(createDataSet());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setExtendedFloatingButtonAction();
    }

    private List<TaskInformation> createDataSet() {
        final List<TaskInformation> content = new ArrayList<>();

        for(int i = 0; i < 6; i++) {
            TaskInformation information = new TaskInformation("Task  " + i, "Description: ");
            content.add(information);
        }

        return content;
    }

    private void setExtendedFloatingButtonAction(){
        ExtendedFloatingActionButton add_list_fab = findViewById(R.id.extended_add_list_fab);
        add_list_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Toast por defecto", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });
    }
}
