package com.example.pam_project.lists.lists;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pam_project.R;
import com.example.pam_project.db.AppDatabase;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditListActivity extends AppCompatActivity {

    private SpinnerActivity spinnerActivity;
    private List<String> categoriesByName;
    private List<Integer> categoriesById;
    private AppDatabase db;
    private int listId;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(getApplicationContext());
        categoriesByName = new ArrayList<>();
        categoriesById = new ArrayList<>();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_list);
        setContentView(R.layout.activity_edit_list);

        listId = getIntent().getIntExtra("id", -1);

        getAllCategories();
        getList();
    }

    private void getAllCategories() {
        db.categoryDao().getAllCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    setUpCategory(model);
                });
    }

    private void getList() {
        db.listDao().getListById(listId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    setUpList(model);
                });
    }

    private void setUpCategory(List<CategoryEntity> model){
        String[] categoriesNames = new String[model.size()];

        for(int i = 0; i < model.size(); i++) {
            CategoryEntity category = model.get(i);
            categoriesNames[i] = category.name;
            categoriesById.add(category.id);
            categoriesByName.add(category.name);
        }

        spinner = findViewById(R.id.edit_list_category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                categoriesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinnerActivity = new SpinnerActivity();
        spinner.setOnItemSelectedListener(spinnerActivity);
    }

    private void setUpList(ListEntity model){
        EditText title = findViewById(R.id.edit_list_title_input);
        title.setText(model.name);
        spinner.setSelection(categoriesById.indexOf(model.categoryId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final EditText listTitleInput = findViewById(R.id.edit_list_title_input);
        final Spinner spinner = findViewById(R.id.edit_list_category_spinner);

        if (id == R.id.check_add_button) {
            String listTile = listTitleInput.getText().toString();
            Integer index = categoriesByName.indexOf(spinner.getSelectedItem());
            Integer categoryId = categoriesById.get(index);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("listTitle", listTile);
            returnIntent.putExtra("categoryId", categoryId.toString());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}