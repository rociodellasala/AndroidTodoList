package com.example.pam_project.lists.lists;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.WelcomeActivity;
import com.example.pam_project.utils.AppColor;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Random;

public class ListActivity extends AppCompatActivity {
    private static final String FTU_KEY = "is_ftu";
    private static final String PAM_PREF = "app-pref";

    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private List<ListInformation> contentList;
    private final int CREATE_LIST_ACTIVITY_REGISTRY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPref = getSharedPreferences(PAM_PREF, MODE_PRIVATE);


        if(sharedPref.getBoolean(FTU_KEY, true)){
            sharedPref.edit().putBoolean(FTU_KEY, false).apply();
            startActivity(new Intent(this, WelcomeActivity.class));
        }


        setContentView(R.layout.activity_list);
        setup();
    }

    private void setup() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        contentList = createDataSet();
        adapter = new ListAdapter(contentList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setExtendedFloatingButtonAction();
    }

    private List<ListInformation> createDataSet() {
        List<ListInformation> content = new ArrayList<>();
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for(int i = 0; i < 20; i++) {
            Random rand = new Random();
            int r = rand.nextInt(colors.size());
            AppColor color = colors.get(r);
            ListInformation information = new ListInformation("List name " + i, "Tasks: " + r, color);
            content.add(information);
        }

        return content;
    }

    private void setExtendedFloatingButtonAction(){
        ExtendedFloatingActionButton addListFAB = findViewById(R.id.extended_fab_add_list);
        addListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(getApplicationContext(), CreateListActivity.class);
                startActivityForResult(activityIntent, CREATE_LIST_ACTIVITY_REGISTRY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final List<AppColor> colors = Arrays.asList(AppColor.values()); // must be removed

        if (requestCode == CREATE_LIST_ACTIVITY_REGISTRY) {
            if(resultCode == Activity.RESULT_OK){
                String newListTile = data.getStringExtra("listTile");
                String newListCategory = data.getStringExtra("listCategory");
                contentList.add(new ListInformation("List name " + newListTile,
                        "Tasks: " + "0", colors.get(0)));
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_action_bar, menu);
        return true;
    }
}
