package com.example.pam_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        final Button welcomeButton = findViewById(R.id.welcome_button);
        welcomeButton.setOnClickListener(v -> finish());
    }

}
