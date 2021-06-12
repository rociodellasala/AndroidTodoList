package com.example.pam_project.features.categories.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.AppColor;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.Objects;

public class EditCategoryActivity extends AppCompatActivity implements EditCategoryView {
    private static final AppColor DEFAULT_COLOR = AppColor.BLUE;
    private static final int CATEGORY_DELETE = -5;
    private int selectedColor = DEFAULT_COLOR.getARGBValue();
    private EditCategoryPresenter presenter;
    private long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        String id = getIntent().getData().getQueryParameter("id");
        categoryId = Long.parseLong(id);

        createPresenter();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_edit_category);
        setDeleteButton();
        setUpView();
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final CategoriesRepository repository = container.getCategoriesRepository();

        presenter = new EditCategoryPresenter(categoryId, repository, this);
    }

    private void setDeleteButton(){
        Button deleteButton = (Button) (Button)findViewById(R.id.delete_category_button);
        deleteButton.setOnClickListener(v -> presenter.deleteCategory(categoryId));
    }

    private void setUpView() {
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
                presenter.updateCategory(categoryName, colorName);
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
    public void onCategoryDelete() {
        Intent returnIntent = new Intent();
        setResult(CATEGORY_DELETE, returnIntent);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

}
