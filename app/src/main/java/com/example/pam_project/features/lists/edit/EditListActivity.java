package com.example.pam_project.features.lists.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.categories.spinner.SpinnerActivity;
import com.example.pam_project.features.categories.spinner.SpinnerCategoryAdapter;
import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;

import java.util.List;
import java.util.Objects;

public class EditListActivity extends AppCompatActivity implements EditListView {

    private static final String LIST_ID_PARAMETER = "id";
    private static final int LIST_CHANGE = -3;
    private static final int DELETE_LIST = -2;
    private EditListPresenter presenter;
    private SpinnerCategoryAdapter adapter;
    private Spinner spinner;
    private long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ApplicationContainer container = ApplicationContainerLocator
                .locateComponent(this);
        final CategoriesRepository categoriesRepository = container.getCategoriesRepository();
        final ListsRepository listsRepository = container.getListsRepository();

        presenter = new EditListPresenter(categoriesRepository, listsRepository, this);

        listId = getIntent().getLongExtra(LIST_ID_PARAMETER, -1);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_edit_list);
        setDeleteButton();
        setup();
    }

    private void setDeleteButton(){
        Button deleteButton = (Button) (Button)findViewById(R.id.delete_list_button);
        deleteButton.setOnClickListener(v -> presenter.deleteList(listId));
    }

    private void setup() {
        spinner = findViewById(R.id.edit_list_category_spinner);
        adapter = new SpinnerCategoryAdapter(this, android.R.layout.simple_spinner_item);
        adapter.getCategories().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter.getCategories());
        SpinnerActivity spinnerActivity = new SpinnerActivity();
        spinner.setOnItemSelectedListener(spinnerActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewAttached(listId);
    }

    @Override
    public void bindCategories(final List<CategoryInformation> model) {
        adapter.update(model);
    }

    @Override
    public void bindList(final ListInformation model) {
        EditText title = findViewById(R.id.edit_list_title_input);
        long categoryId = model.getCategoryId();
        int spinnerPosition = adapter.getCategories().getPosition(adapter.getCategoryById(categoryId));
        title.setText(model.getTitle());
        spinner.setSelection(spinnerPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public void onListChange(){
        Intent returnIntent = new Intent();
        setResult(LIST_CHANGE, returnIntent);
        finish();
    }

    @Override
    public void onListDelete(){
        Intent returnIntent = new Intent();
        setResult(DELETE_LIST, returnIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long itemId = item.getItemId();
        final Spinner spinner = findViewById(R.id.edit_list_category_spinner);
        final EditText listNameInput = findViewById(R.id.edit_list_title_input);

        if (itemId == R.id.check_add_button) {
            String listName = listNameInput.getText().toString();
            Long categoryId = adapter.getCategoriesMap().get(spinner.getSelectedItem().toString());
            String errorMessage = checkForm(listName);
            if (errorMessage != null) {
                listNameInput.setError(errorMessage);
            } else {
                if (categoryId != null)
                    presenter.editList(listId, listName, categoryId);
            }
        } else if (itemId == android.R.id.home) {
            onBackPressed();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}