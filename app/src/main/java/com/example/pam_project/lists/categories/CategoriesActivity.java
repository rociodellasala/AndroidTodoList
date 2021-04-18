package com.example.pam_project.lists.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.CreateListActivity;
import com.example.pam_project.utils.AppColor;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CategoriesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<CategoryInformation> contentList;
    private final int CREATE_CATEGORY_ACTIVITY_REGISTRY = 2;

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
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setExtendedFloatingButtonAction();
    }

    private List<CategoryInformation> createDataSet() {
        List<CategoryInformation> content = new ArrayList<>();
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for(int i = 0; i < 20; i++) {
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
}
