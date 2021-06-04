package com.example.pam_project.features.categories.list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.database.categories.CategoryMapper;
import com.example.pam_project.database.utils.Database;
import com.example.pam_project.database.utils.Storage;
import com.example.pam_project.features.categories.create.CreateCategoryActivity;
import com.example.pam_project.features.lists.list.OnListClickedListener;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.categories.RoomCategoriesRepository;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

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
        presenter.onEmptyCategory();
    }

    @Override
    public void showEmptyMessage() {
        TextView text = findViewById(R.id.empty_list_message);
        View emptyDataMessage = findViewById(R.id.empty_category);

        if(adapter.getItemCount() == 0) {
            text.setText(R.string.empty_category_message);
            emptyDataMessage.setVisibility(View.VISIBLE);
        } else {
            emptyDataMessage.setVisibility(View.GONE);
        }
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

    @Override
    public void onCategoriesSwap(final int draggedPosition, final int targetPosition){
        adapter.swapItems(draggedPosition, targetPosition);
    }

    private ItemTouchHelper.SimpleCallback setDraggableItems() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView1, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int draggedPosition = dragged.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                presenter.swapCategories(draggedPosition, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("SWIPE!", "onSwiped: ");
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        };
    }
}
