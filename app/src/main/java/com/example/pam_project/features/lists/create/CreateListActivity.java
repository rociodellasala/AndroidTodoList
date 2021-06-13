package com.example.pam_project.features.lists.create;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.categories.spinner.SpinnerActivity;
import com.example.pam_project.features.categories.spinner.SpinnerCategoryAdapter;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;

import java.util.List;
import java.util.Objects;

public class CreateListActivity extends AppCompatActivity implements CreateListView {
    private CreateListPresenter presenter;
    private SpinnerCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        createPresenter();
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_create_list);
        setUpView();
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final CategoriesRepository categoriesRepository = container.getCategoriesRepository();
        final ListsRepository listsRepository = container.getListsRepository();
        presenter = new CreateListPresenter(categoriesRepository, listsRepository, this);
    }

    private void setUpView() {
        Spinner spinner = findViewById(R.id.create_list_category_spinner);
        adapter = new SpinnerCategoryAdapter(this, android.R.layout.simple_spinner_item);
        adapter.getCategories().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter.getCategories());
        SpinnerActivity spinnerActivity = new SpinnerActivity();
        spinner.setOnItemSelectedListener(spinnerActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewAttached();
    }

    @Override
    public void bindCategories(final List<CategoryInformation> model) {
        adapter.update(model);
    }

    @Override
    public void onListInsertedError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_list_create), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCategoriesReceivedError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_categories_fetch), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long itemId = item.getItemId();
        final Spinner spinner = findViewById(R.id.create_list_category_spinner);
        final EditText listNameInput = findViewById(R.id.create_list_title_input);

        if (itemId == R.id.check_add_button) {
            String listName = listNameInput.getText().toString();
            Long categoryId = adapter.getCategoriesMap().get(spinner.getSelectedItem().toString());
            String errorMessage = checkForm(listName);

            if (errorMessage != null) {
                listNameInput.setError(errorMessage);
            } else {
                presenter.insertList(listName, categoryId);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private String checkForm(String listName) {
        String errorMessage = null;

        if (listName == null || listName.trim().isEmpty()) {
            errorMessage = getString(R.string.error_empty_input);
        }

        return errorMessage;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }
}