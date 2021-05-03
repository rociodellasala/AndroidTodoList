package com.example.pam_project.lists.lists.editListActivity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pam_project.R;
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.db.mappers.ListMapper;
import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.db.repositories.RoomCategoriesRepository;
import com.example.pam_project.db.repositories.RoomListsRepository;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;
import com.example.pam_project.lists.categories.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.lists.lists.components.SpinnerActivity;
import com.example.pam_project.lists.lists.components.SpinnerCategoryAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;
import java.util.Objects;

public class EditListActivity extends AppCompatActivity implements EditListView{

    private EditListPresenter editListPresenter;
    private SpinnerCategoryAdapter adapter;
    private Spinner spinner;
    private long listId;

    private final static String  LIST_ID_PARAMETER = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();
        final CategoryMapper categoryMapper = new CategoryMapper();
        final ListMapper listMapper = new ListMapper();
        final CategoriesRepository categoriesRepository = new RoomCategoriesRepository(mainStorage.getStorage().categoryDao(), categoryMapper);
        final ListsRepository listsRepository = new RoomListsRepository(mainStorage.getStorage().listDao(),
                mainStorage.getStorage().categoryDao(), listMapper);

        editListPresenter = new EditListPresenter(categoriesRepository, listsRepository, this);

        listId = getIntent().getLongExtra(LIST_ID_PARAMETER, -1);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_list);
        setContentView(R.layout.activity_edit_list);
        setup();
    }

    private void setup(){
        spinner = findViewById(R.id.edit_list_category_spinner);
        adapter = new SpinnerCategoryAdapter(this, android.R.layout.simple_spinner_item);
        adapter.getCategories().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter.getCategories());
        SpinnerActivity spinnerActivity = new SpinnerActivity();
        spinner.setOnItemSelectedListener(spinnerActivity);
    }

    @Override
    public void onStart(){
        super.onStart();
        editListPresenter.onViewAttached(listId);
    }

    @Override
    public void bindCategories(final List<CategoryInformation> model) {
        adapter.update(model);
    }

    @Override
    public void bindList(final ListInformation model){
        EditText title = findViewById(R.id.edit_list_title_input);
        long categoryId = model.getCategoryId();
        int spinnerPosition = adapter.getCategories().getPosition(adapter.getCategoryById(categoryId));
        title.setText(model.getTitle());
        spinner.setSelection(spinnerPosition);
    }

    @Override
    public void onSuccessfulUpdate(final String name, final long categoryId) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("listTitle", name);
        returnIntent.putExtra("categoryId", String.valueOf(categoryId));
        returnIntent.putExtra("id", listId);
        setResult(Activity.RESULT_OK, returnIntent);
    }

    @Override
    public void onFailedUpdate(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long itemId = item.getItemId();
        final Spinner spinner = findViewById(R.id.edit_list_category_spinner);
        final EditText listTitleInput = findViewById(R.id.edit_list_title_input);

        if (itemId == R.id.check_add_button) {
            String listTile = listTitleInput.getText().toString();
            Long categoryId = adapter.getCategoriesMap().get(spinner.getSelectedItem().toString());

            if(categoryId != null){
                editListPresenter.editList(listId, listTile, categoryId);
            } else {
                this.onFailedUpdate();
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop(){
        super.onStop();
        editListPresenter.onViewDetached();
    }
}