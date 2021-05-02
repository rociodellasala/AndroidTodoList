package com.example.pam_project.lists.categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.createListActivity.CreateListActivity;
import com.example.pam_project.utils.AppColor;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CategoriesActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<CategoryInformation> contentList;
    private static final int CREATE_CATEGORY_ACTIVITY_REGISTRY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        setup();
    }

    private void setup() {
        recyclerView = findViewById(R.id.category);
        recyclerView.setHasFixedSize(true);
        contentList = createDataSet();
        adapter = new CategoryAdapter(contentList);
        ItemTouchHelper touchHelper = new ItemTouchHelper(SetDraggableItems());
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setExtendedFloatingButtonAction();
    }

    private List<CategoryInformation> createDataSet() {
        List<CategoryInformation> content = new ArrayList<>();
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for(int i = 0; i < 4; i++) {
            Random rand = new Random();
            int r = rand.nextInt(colors.size());
            AppColor color = colors.get(r);
            CategoryInformation information = new CategoryInformation("Category: " + i, color);
            content.add(information);
        }

        return content;
    }

    private void setExtendedFloatingButtonAction(){
        ExtendedFloatingActionButton addListFAB = findViewById(R.id.extended_fab_add_category);
        addListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(getApplicationContext(), CreateListActivity.class);
                startActivityForResult(activityIntent, CREATE_CATEGORY_ACTIVITY_REGISTRY);
            }
        });
    }

    private ItemTouchHelper.SimpleCallback SetDraggableItems(){
        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView1, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int draggedPosition = dragged.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                Collections.swap(contentList, draggedPosition, targetPosition);
                adapter.notifyItemMoved(draggedPosition, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("SWIPE!", "onSwiped: ");
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                // Disable swipe (dont override this method or return true, if you want to have swipe)
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // Set movement flags to specify the movement direction
                // final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;  <-- for all directions
                // In this case only up and down is allowed
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        };

        return itemTouchHelper;
    }
}
