package com.example.pam_project.lists.categories.editCategoryActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.RoomCategoriesRepository;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;
import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.utils.AppColor;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.Objects;

public class EditCategoryActivity extends AppCompatActivity implements EditCategoryView {
    private static final AppColor DEFAULT_COLOR = AppColor.BLUE;
    private EditCategoryPresenter presenter;
    private int selectedColor;
    private long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getIntent().getData().getQueryParameter("id");
        this.categoryId = Long.parseLong(id);

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();

        final CategoryMapper mapper = new CategoryMapper();
        final CategoriesRepository repository = new RoomCategoriesRepository(
                mainStorage.getStorage().categoryDao(), mapper);


        presenter = new EditCategoryPresenter(categoryId, repository, this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_category);
        setContentView(R.layout.activity_edit_category);
        setup();
    }

    private void setup() {
        SpectrumPalette palette = findViewById(R.id.edit_category_palette_color);
        final int[] colors = new int[AppColor.values().length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = AppColor.values()[i].getARGBValue();
        }
        palette.setColors(colors);
        palette.setOnColorSelectedListener(color -> selectedColor = color);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewAttached();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long itemId = item.getItemId();
        final EditText categoryNameInput = findViewById(R.id.edit_category_name_input);

        if (itemId == R.id.check_add_button) {
            String categoryName = categoryNameInput.getText().toString();
            final AppColor color = AppColor.fromARGBValue(selectedColor);
            String errorMessage = checkForm(categoryName);

            String colorName = DEFAULT_COLOR.name();
            if (color != null) {
                colorName = color.name();
            }

            if (errorMessage != null) {
                categoryNameInput.setError(errorMessage);
            } else {
                if (!categoryName.isEmpty()) {
                    presenter.editCategory(categoryName, colorName);
                } else {
                    this.onFailedUpdate();
                }
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private String checkForm(String categoryName) {
        String errorMessage = null;

        if (categoryName == null || categoryName.trim().isEmpty()) {
            errorMessage = getString(R.string.error_empty_input);
        }

        return errorMessage;
    }


    @Override
    public void bindCategory(final CategoryInformation model) {
        EditText title = findViewById(R.id.edit_category_name_input);
        SpectrumPalette palette = findViewById(R.id.edit_category_palette_color);
        title.setText(model.getTitle());
        palette.setSelectedColor(model.getColor().getARGBValue());
    }

    @Override
    public void onSuccessfulUpdate(final String name, final String color) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("categoryName", name);
        returnIntent.putExtra("color", color);
        returnIntent.putExtra("id", categoryId);
        setResult(Activity.RESULT_OK, returnIntent);
    }

    @Override
    public void onFailedUpdate() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

}
