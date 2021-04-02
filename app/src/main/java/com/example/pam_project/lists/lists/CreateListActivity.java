package com.example.pam_project.lists.lists;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pam_project.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Objects;


public class CreateListActivity extends AppCompatActivity {

    private SpinnerActivity spinnerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_create_list);
        setContentView(R.layout.activity_create_list);
        setup();
    }

    private void setup(){
        // Must be populated with real categories
        String[] categories = {getResources().getString(R.string.default_category)};
        Spinner spinner = findViewById(R.id.create_list_category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinnerActivity = new SpinnerActivity();
        spinner.setOnItemSelectedListener(spinnerActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Spinner spinner = findViewById(R.id.create_list_category_spinner);
        final EditText listTitleInput = findViewById(R.id.create_list_title_input);

        if (id == R.id.check_add_list_button) {
            String listTile = listTitleInput.getText().toString();
            String listCategory = spinner.getSelectedItem().toString();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("listTile", listTile);
            returnIntent.putExtra("listCategory", listCategory);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}