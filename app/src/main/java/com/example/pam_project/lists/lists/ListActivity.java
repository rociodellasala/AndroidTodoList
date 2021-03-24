package com.example.pam_project.lists.lists;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPref = getSharedPreferences("app-pref", MODE_PRIVATE);

        if(sharedPref.getBoolean("welcome", true)){
            sharedPref.edit().putBoolean("welcome", false).apply();
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        setContentView(R.layout.activity_main);
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

    private List<String> createDataSet() {
        final List<String> content = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            content.add("Hola mundo");
        }
        return content;
    }
}
