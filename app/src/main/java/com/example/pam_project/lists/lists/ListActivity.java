package com.example.pam_project.lists.lists;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.WelcomeActivity;
import com.example.pam_project.utils.AppColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Random;


public class ListActivity extends AppCompatActivity {
    private static final String FTU_KEY = "is_ftu";
    private static final String PAM_PREF = "app-pref";

    private RecyclerView recyclerView;
    private ListAdapter adapter;

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
        adapter = new ListAdapter(createDataSet());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
    }

    private List<ListInformation> createDataSet() {
        final List<ListInformation> content = new ArrayList<>();
        final List<AppColor> colors = Arrays.asList(AppColor.values());
        for(int i = 0; i < 20; i++) {
            Random rand = new Random();
            AppColor color = colors.get(rand.nextInt(colors.size()));
            ListInformation information =
                    new ListInformation("List name " + i, "Tasks: " + 5, color);
            content.add(information);
        }

        return content;
    }
}
