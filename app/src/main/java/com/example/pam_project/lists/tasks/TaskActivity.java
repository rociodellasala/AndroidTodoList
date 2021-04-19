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
import com.example.pam_project.db.AppDatabase;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.db.relationships.ListsWithTasks;
import com.example.pam_project.utils.TaskStatus;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class TaskActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPending;
    private RecyclerView recyclerViewDone;
    private final int CREATE_TASK_ACTIVITY_REGISTRY = 2;
    private AppDatabase db;
    private Disposable disposable;
    private int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(getApplicationContext());

        String id = getIntent().getData().getQueryParameter("id");
        this.listId = Integer.parseInt(id);

        setContentView(R.layout.activity_task);
        setup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_action_bar, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        disposable = db.listDao().getListsWithTasks(listId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(model.list.name);
                    final TaskAdapterPending adapterPending = new TaskAdapterPending(adaptModel(getSeparateModels(model, "pending")));
                    final TaskAdapterDone adapterDone = new TaskAdapterDone(adaptModel(getSeparateModels(model, "done")));
                    recyclerViewPending.setAdapter(adapterPending);
                    recyclerViewDone.setAdapter(adapterDone);
                });
    }

    public List<TaskEntity> getSeparateModels(ListsWithTasks model, String status) {
        List<TaskEntity> listOfTasks = new ArrayList<>();

        for (final TaskEntity taskEntity : model.tasks) {
            if(taskEntity.status.equals(status)) {
                listOfTasks.add(taskEntity);
            }
        }

        return listOfTasks;
    }

    private List<TaskInformation> adaptModel(List<TaskEntity> model) {
        final List<TaskInformation> list = new ArrayList<>();

        for (final TaskEntity taskEntity : model) {
            TaskStatus status = taskEntity.status.equals("pending") ? TaskStatus.PENDING : TaskStatus.DONE;
            list.add(new TaskInformation(taskEntity.id, taskEntity.name, taskEntity.description, taskEntity.priority, status));
        }

        return list;
    }


    private void setup() {
        recyclerViewPending = findViewById(R.id.pendingTasks);
        recyclerViewPending.setHasFixedSize(true);
        recyclerViewPending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerViewDone = findViewById(R.id.doneTasks);
        recyclerViewDone.setHasFixedSize(true);
        recyclerViewDone.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setExtendedFloatingButtonAction();
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
                TaskAdapterPending adapter = (TaskAdapterPending) recyclerViewPending.getAdapter();
                /* Urgency harcodeado*/
                boolean isUrgent = true;
                this.insertNewList(newTaskTitle, newTaskDescription, isUrgent, this.listId);
                adapter.addItem(new TaskInformation(newTaskTitle, newTaskDescription, isUrgent, TaskStatus.PENDING));
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void insertNewList(String name, String description, boolean isUrgent, int listId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                TaskEntity listEntity = new TaskEntity(name, description, true, "pending", listId);
                long id = db.taskDao().insertTask(listEntity);
            }
        }).onErrorComplete().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
    }

}