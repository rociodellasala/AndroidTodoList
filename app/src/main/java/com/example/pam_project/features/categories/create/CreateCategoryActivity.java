package com.example.pam_project.features.categories.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.AppColor;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.Objects;

public class CreateCategoryActivity extends AppCompatActivity implements CreateCategoryView {
    private static final AppColor DEFAULT_COLOR = AppColor.BLUE;
    private CreateCategoryPresenter presenter;
    private int selectedColor = DEFAULT_COLOR.getARGBValue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ApplicationContainer container = ApplicationContainerLocator
                .locateComponent(this);
        final CategoriesRepository repository = container.getCategoriesRepository();

        presenter = new CreateCategoryPresenter(repository, this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_create_category);
        setContentView(R.layout.activity_create_category);

        setup();
    }

    private void setup() {
        SpectrumPalette palette = findViewById(R.id.create_category_palette_color);
        final int[] colors = new int[AppColor.values().length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = AppColor.values()[i].getARGBValue();
        }
        palette.setColors(colors);
        palette.setSelectedColor(selectedColor);
        palette.setOnColorSelectedListener(color -> selectedColor = color);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long itemId = item.getItemId();
        final EditText categoryNameInput = findViewById(R.id.create_category_name_input);

        if (itemId == R.id.check_add_button) {
            final String categoryName = categoryNameInput.getText().toString();
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
                    presenter.insertCategory(categoryName, colorName);
                } else {
                    this.onFailedInsert();
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
    public void onSuccessfulInsert(final long id, final String name, final String color) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("categoryName", name);
        returnIntent.putExtra("color", color);
        returnIntent.putExtra("id", id);
        setResult(Activity.RESULT_OK, returnIntent);
    }

    @Override
    public void onFailedInsert() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }
}
