package com.example.pam_project.lists.lists;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pam_project.R;
import com.example.pam_project.db.AppDatabase;
import com.example.pam_project.db.entities.CategoryEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateListActivity extends AppCompatActivity {

    private SpinnerActivity spinnerActivity;
    private Map<String, Long> categories;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(getApplicationContext());
        categories = new HashMap<>();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_create_list);
        setContentView(R.layout.activity_create_list);

        getAllCategories();
    }

    private void getAllCategories() {
        db.categoryDao().getAllCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    setup(model);
                });
    }


    private void setup(List<CategoryEntity> model){
        String[] categoriesNames = new String[model.size()];

        for(int i = 0; i < model.size(); i++) {
            CategoryEntity category = model.get(i);
            categoriesNames[i] = category.name;
            categories.put(category.name, category.id);
        }

        Spinner spinner = findViewById(R.id.create_list_category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                categoriesNames);
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
        long id = item.getItemId();
        final Spinner spinner = findViewById(R.id.create_list_category_spinner);
        final EditText listTitleInput = findViewById(R.id.create_list_title_input);

        if (id == R.id.check_add_button) {
            String listTile = listTitleInput.getText().toString();
            long categoryId = categories.get(spinner.getSelectedItem().toString());
            Intent returnIntent = new Intent();
            returnIntent.putExtra("listTile", listTile);
            returnIntent.putExtra("categoryId", String.valueOf(categoryId));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}