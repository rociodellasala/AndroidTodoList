package com.example.pam_project.landing;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;

import com.example.pam_project.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Button welcomeButton = findViewById(R.id.welcome_button);
        welcomeButton.setOnClickListener(v -> finish());

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();
        mainStorage.populateStorage();
    }

}
