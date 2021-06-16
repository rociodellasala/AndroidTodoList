package com.example.pam_project.features.lists.edit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.example.pam_project.utils.constants.ActivityResultCode;
import com.example.pam_project.utils.schedulers.SchedulerProvider;
import com.example.pam_project.utils.validators.FormValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditListActivity extends AppCompatActivity implements EditListView {
    private static final String LIST_ID_PARAMETER = "id";
    private EditListPresenter presenter;
    private SpinnerCategoryAdapter adapter;
    private Spinner spinner;
    private long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        createPresenter();

        listId = getIntent().getLongExtra(LIST_ID_PARAMETER, -1);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setDeleteButton();
        setUpView();
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final SchedulerProvider schedulerProvider = container.getSchedulerProvider();
        final CategoriesRepository categoriesRepository = container.getCategoriesRepository();
        final ListsRepository listsRepository = container.getListsRepository();
        presenter = new EditListPresenter(schedulerProvider, categoriesRepository,
                listsRepository, this);
    }

    private void setUpView() {
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

    private void setDeleteButton(){
        Button deleteButton = findViewById(R.id.delete_list_button);
        deleteButton.setOnClickListener(v -> presenter.onDeletePressed());
    }

    @Override
    public void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_list_delete)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_dialog, (dialog, id) -> presenter.deleteList(listId))
                .setNegativeButton(R.string.cancel_dialog, null)
                .show();
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
    public void onListDelete(){
        Intent returnIntent = new Intent();
        setResult(ActivityResultCode.DELETE_LIST_CODE.ordinal(), returnIntent);
        finish();
    }

    @Override
    public void onListDeletedError(){
        Toast.makeText(getApplicationContext(), getString(R.string.error_list_delete), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListUpdatedError(){
        Toast.makeText(getApplicationContext(), getString(R.string.error_list_update), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCategoriesReceivedError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_categories_fetch), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long itemId = item.getItemId();
        final Spinner spinner = findViewById(R.id.edit_list_category_spinner);
        final EditText listNameInput = findViewById(R.id.edit_list_title_input);

        if (itemId == R.id.check_add_button) {
            String listName = listNameInput.getText().toString();
            Long categoryId = adapter.getCategoriesMap().get(spinner.getSelectedItem().toString());
            boolean validForm = FormValidator.validate(getApplicationContext(), createInputMap(listName, listNameInput));

            if (validForm) {
                presenter.updateList(listId, listName, categoryId);
                finish();
            }
        } else if (itemId == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private Map<EditText, String> createInputMap(String listName, EditText listNameInput) {
        Map<EditText, String> map = new HashMap<>();
        map.put(listNameInput, listName);
        return map;
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