package com.example.pam_project.features.tasks.list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.features.lists.edit.EditListActivity;
import com.example.pam_project.features.lists.list.OnListClickedListener;
import com.example.pam_project.features.tasks.create.CreateTaskActivity;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.constants.ActivityResultCode;
import com.example.pam_project.utils.constants.TaskStatus;
import com.example.pam_project.utils.schedulers.SchedulerProvider;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity implements TaskView, OnListClickedListener {
    private static final int EDIT_LIST_ACTIVITY = 1;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TaskPresenter presenter;
    private long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        String id = getIntent().getData().getQueryParameter("id");
        listId = Long.parseLong(id);
        setUpView();
        createPresenter();
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final SchedulerProvider schedulerProvider = container.getSchedulerProvider();
        final TaskRepository taskRepository = container.getTasksRepository();
        final ListsRepository listsRepository = container.getListsRepository();
        presenter = new TaskPresenter(schedulerProvider, taskRepository, listsRepository,
                this, listId);
    }

    private void setUpView() {
        recyclerView = findViewById(R.id.allTasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final ItemTouchHelper touchHelper = new ItemTouchHelper(setSwippableItems());
        touchHelper.attachToRecyclerView(recyclerView);
        setExtendedFloatingButtonAction();

        adapter = new TaskAdapter();
        adapter.setOnClickedListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_action_bar, menu);
        return true;
    }

    private void setExtendedFloatingButtonAction() {
        ExtendedFloatingActionButton addTaskFAB = findViewById(R.id.extended_fab_add_task);
        addTaskFAB.setOnClickListener(view -> presenter.onButtonAddClicked());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_LIST_ACTIVITY) {
            if (resultCode == ActivityResultCode.DELETE_LIST_CODE.ordinal()) {
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();

        if (id == R.id.edit_list_button) {
            presenter.onEditList();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void bindTasks(List<TaskInformation> model) {
        adapter.update(model);
        presenter.onEmptyTask();
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
        startActivityForResult(activityIntent, EDIT_LIST_ACTIVITY);
    }

    @Override
    public void onTasksReceivedError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_tasks_fetch), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskUpdatedError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_task_update), Toast.LENGTH_LONG).show();
    }

    @Override
    public void bindListName(final String listName) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(listName);
    }

    @Override
    public void showAddTask() {
        Intent activityIntent = new Intent(getApplicationContext(), CreateTaskActivity.class);
        activityIntent.putExtra("id", listId);
        startActivity(activityIntent);
    }

    @Override
    public void showTaskContent(final long id) {
        String uri = "pam://edit/task?id=";
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri + id));
        startActivity(activityIntent);
    }

    @Override
    public void onTaskStatusEdit(final TaskInformation model, final int adapterPosition) {
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
                presenter.onTaskChange(taskInformation, swippedItem.getAdapterPosition());
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
    public void onClick(final long id) {
        presenter.onTaskClicked(id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }
}