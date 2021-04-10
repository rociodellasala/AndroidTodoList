package com.example.pam_project.lists.tasks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.utils.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<TaskInformation> contentList;
    // private final int CRATE_TASK_ACTIVITY_REGISTRY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        setup();
    }

    private void setup() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        contentList = createDataSet();
        adapter = new TaskAdapter(createDataSet());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // setExtendedFloatingButtonAction();
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

//    private void setExtendedFloatingButtonAction(){
//        ExtendedFloatingActionButton addListFAB = findViewById(R.id.extended_fab_add_list);
//        addListFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent activityIntent = new Intent(getApplicationContext(), CreateListActivity.class);
//                startActivityForResult(activityIntent, CRATE_TASK_ACTIVITY_REGISTRY);
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CREATE_LIST_ACTIVITY_REGISTRY) {
//            if(resultCode == Activity.RESULT_OK){
//                String newListTile = data.getStringExtra("listTile");
//                contentList.add(new TaskInformation("List name " + newListTile, "Description"));
//                adapter.notifyDataSetChanged();
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.list_action_bar, menu);
//        return true;
//    }
}
