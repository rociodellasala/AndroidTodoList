package com.example.pam_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /*final SharedPreferences sharedPref = getSharedPreferences("app-pref",MODE_PRIVATE);

        if(sharedPref.getBoolean("welcome", true)){
            sharedPref.edit().putBoolean("welcome",false).apply();
            startActivity(new Intent(this, WelcomeActivity.class));
        }*/

    }
}