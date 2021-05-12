package com.example.pam_project.lists.tasks.taskActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.db.mappers.ListMapper;
import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.db.repositories.RoomListsRepository;
import com.example.pam_project.db.repositories.RoomTaskRepository;
import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;
import com.example.pam_project.lists.lists.editListActivity.EditListActivity;
import com.example.pam_project.lists.tasks.components.CustomItemDecorator;
import com.example.pam_project.lists.tasks.components.TaskAdapter;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.lists.tasks.createTaskActivity.CreateTaskActivity;
import com.example.pam_project.utils.TaskStatus;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity implements TaskView {
    private final int CREATE_TASK_ACTIVITY_REGISTRY = 2;
    private final int EDIT_LIST_ACTIVITY_REGISTRY = 3;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TaskPresenter taskPresenter;
    private long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getIntent().getData().getQueryParameter("id");
        this.listId = Long.parseLong(id);

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();

        final ListMapper listMapper = new ListMapper();
        final TaskRepository taskRepository = new RoomTaskRepository(mainStorage.getStorage().taskDao());
        final ListsRepository listsRepository = new RoomListsRepository(mainStorage.getStorage().listDao(),
                mainStorage.getStorage().categoryDao(), listMapper);

        taskPresenter = new TaskPresenter(taskRepository, listsRepository, this, listId);

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
        taskPresenter.onViewAttached();
    }

    private void setup() {
        recyclerView = findViewById(R.id.allTasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        String[] headers = {getString(R.string.pending_tasks), getString(R.string.done_tasks)};
        recyclerView.addItemDecoration(new CustomItemDecorator(this, R.layout.text_header, headers));

        final ItemTouchHelper touchHelper = new ItemTouchHelper(setSwippableItems());
        touchHelper.attachToRecyclerView(recyclerView);
        setExtendedFloatingButtonAction();
    }

    private void setExtendedFloatingButtonAction() {
        ExtendedFloatingActionButton addTaskFAB = findViewById(R.id.extended_fab_add_task);
        addTaskFAB.setOnClickListener(view -> taskPresenter.onButtonAddClicked());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_TASK_ACTIVITY_REGISTRY) {
            if (resultCode == Activity.RESULT_OK) {
                String taskId = data.getStringExtra("taskId");
                taskPresenter.appendTask(Long.parseLong(taskId));
            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        } else if (requestCode == EDIT_LIST_ACTIVITY_REGISTRY) {
//            if (resultCode == Activity.RESULT_OK) {
//                // Write
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
        }
    }

    @Override
    public void showTasks() {
        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();

        if (id == R.id.edit_list_button) {
            Intent activityIntent = new Intent(getApplicationContext(), EditListActivity.class);
            activityIntent.putExtra("id", listId);
            startActivityForResult(activityIntent, EDIT_LIST_ACTIVITY_REGISTRY);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void bindTasks(List<TaskInformation> model) {
        adapter.update(model);
    }

    @Override
    public void bindTask(TaskInformation model) {
        adapter.addItem(model);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void bindListName(final String listName) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(listName);
    }

    @Override
    public void showAddTask() {
        Intent activityIntent = new Intent(getApplicationContext(), CreateTaskActivity.class);
        activityIntent.putExtra("id", listId);
        startActivityForResult(activityIntent, CREATE_TASK_ACTIVITY_REGISTRY);
    }

    @Override
    public void onSuccessfulUpdate(final TaskInformation model, final int adapterPosition) {
        adapter.update(model, adapterPosition);
        if(adapter.areAllComplete())
            finish();
    }

    private ItemTouchHelper.SimpleCallback setSwippableItems() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView1,
                                  @NonNull RecyclerView.ViewHolder dragged,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder swippedItem, int direction) {
                TaskInformation taskInformation = adapter.getItem(swippedItem.getAdapterPosition());

                if (taskInformation.getStatus().equals(TaskStatus.PENDING)) {
                    taskPresenter.onTaskChange(swippedItem.getAdapterPosition(),
                            taskInformation.getId(), taskInformation.getTitle(),
                            taskInformation.getDescription(), taskInformation.getUrgency(),
                            TaskStatus.DONE, listId);
                } else if (taskInformation.getStatus().equals(TaskStatus.DONE)){
                    taskPresenter.onTaskChange(swippedItem.getAdapterPosition(),
                            taskInformation.getId(), taskInformation.getTitle(),
                            taskInformation.getDescription(), taskInformation.getUrgency(),
                            TaskStatus.PENDING, listId);
                }else
                    adapter.notifyDataSetChanged();

            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder swippedItem) {

                final int dragFlags = 0;
                final int leftSwipeFlag = ItemTouchHelper.LEFT;
                final int rightSwipeFlag = ItemTouchHelper.RIGHT;

                TaskInformation taskInformation = adapter.getItem(swippedItem.getAdapterPosition());
                if (taskInformation.getStatus().equals(TaskStatus.DONE))
                    return makeMovementFlags(dragFlags, rightSwipeFlag);

                return makeMovementFlags(dragFlags, leftSwipeFlag);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskPresenter.onViewDetached();
    }
}