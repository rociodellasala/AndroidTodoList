package com.example.pam_project.features.tasks.list;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.database.lists.ListMapper;
import com.example.pam_project.database.utils.Database;
import com.example.pam_project.database.utils.Storage;
import com.example.pam_project.features.lists.edit.EditListActivity;
import com.example.pam_project.features.lists.list.OnListClickedListener;
import com.example.pam_project.features.tasks.create.CreateTaskActivity;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.lists.RoomListsRepository;
import com.example.pam_project.repositories.tasks.RoomTaskRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.TaskStatus;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity implements TaskView, OnListClickedListener {
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

        setContentView(R.layout.activity_task);
        setup();
        taskPresenter = new TaskPresenter(taskRepository, listsRepository, this, listId);

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
        }
    }

    @Override
    public void showTasks() {
        adapter = new TaskAdapter();
        adapter.setOnClickedListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();

        if (id == R.id.edit_list_button) {
            taskPresenter.onEditList();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void bindTasks(List<TaskInformation> model) {
        adapter.update(model);
        taskPresenter.onEmptyTask();
    }

    @Override
    public void bindHeaders(int[] headers) {
        String[] titleHeaders = {getString(headers[0]), getString(headers[1])};
        recyclerView.addItemDecoration(new CustomItemDecorator(this, R.layout.text_header, titleHeaders));
    }

    @Override
    public void showEmptyMessage() {
        TextView text = findViewById(R.id.empty_list_message);
        View emptyDataMessage = findViewById(R.id.empty_task);

        if(adapter.getItemCount() == 0) {
            text.setText(R.string.empty_task_message);
            emptyDataMessage.setVisibility(View.VISIBLE);
        } else {
            emptyDataMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEditList() {
        Intent activityIntent = new Intent(getApplicationContext(), EditListActivity.class);
        activityIntent.putExtra("id", listId);
        startActivityForResult(activityIntent, EDIT_LIST_ACTIVITY_REGISTRY);
    }

    @Override
    public void bindTask(TaskInformation model) {
        adapter.addItem(model);
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
    public void showTaskContent(final long id) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pam://edit/task?id=" + id)));
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
                taskPresenter.onTaskChange(taskInformation, swippedItem.getAdapterPosition());
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

    @Override
    public void onClick(final long id) {
        taskPresenter.onTaskClicked(id);
    }
}