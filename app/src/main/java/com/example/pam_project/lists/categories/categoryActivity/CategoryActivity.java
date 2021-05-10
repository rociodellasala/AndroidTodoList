package com.example.pam_project.lists.categories.categoryActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.RoomCategoriesRepository;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;
import com.example.pam_project.lists.categories.components.CategoryAdapter;
import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.lists.categories.createCategoryActivity.CreateCategoryActivity;
import com.example.pam_project.lists.lists.listActivity.OnListClickedListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity implements CategoryView, OnListClickedListener {
    private static final int CREATE_CATEGORY_ACTIVITY_REGISTRY = 8;
    private RecyclerView recyclerView;
    private CategoryPresenter presenter;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();

        final CategoryMapper categoryMapper = new CategoryMapper();
        final CategoriesRepository repository = new RoomCategoriesRepository(
                mainStorage.getStorage().categoryDao(), categoryMapper);

        presenter = new CategoryPresenter(repository, this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_category);

        setContentView(R.layout.activity_category);
        setup();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached();
    }

    @Override
    protected void onStop() {
        Log.d("aca", "on view detached");
        super.onStop();
        presenter.onViewDetached();
    }

    private void setup() {
        recyclerView = findViewById(R.id.category);
        recyclerView.setHasFixedSize(true);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(setDraggableItems());
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
        ));
        setExtendedFloatingButtonAction();
    }

    private void setExtendedFloatingButtonAction() {
        ExtendedFloatingActionButton addListFAB = findViewById(R.id.extended_fab_add_category);
        addListFAB.setOnClickListener(view -> {
            Intent activityIntent = new Intent(getApplicationContext(), CreateCategoryActivity.class);
            startActivityForResult(activityIntent, CREATE_CATEGORY_ACTIVITY_REGISTRY);
        });
    }

    @Override
    public void bindCategories(List<CategoryInformation> model) {
        adapter.update(model);
    }

    @Override
    public void showCategories() {
        adapter = new CategoryAdapter();
        adapter.setOnClickedListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(final long id) {
        presenter.onCategoryClicked(id);
    }

    @Override
    public void showCategoryForm(final long id) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pam://edit/category?id=" + id)));
    }

    private ItemTouchHelper.SimpleCallback setDraggableItems() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView1, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int draggedPosition = dragged.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                Collections.swap(adapter.getDataSet(), draggedPosition, targetPosition);
                adapter.notifyItemMoved(draggedPosition, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("SWIPE!", "onSwiped: ");
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                // Disable swipe (don't override this method or return true, if you want to have swipe)
                return false;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                // Set movement flags to specify the movement direction
                // final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;  <-- for all directions
                // In this case only up and down is allowed
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        };
    }
}
