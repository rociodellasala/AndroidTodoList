package com.example.pam_project.landing

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pam_project.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val welcomeButton = findViewById<Button>(R.id.welcome_button)
        welcomeButton.setOnClickListener { v: View? -> finish() }
    }
}