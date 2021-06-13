package com.example.pam_project.features.categories.create;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.constants.AppColor;
import com.example.pam_project.utils.schedulers.SchedulerProvider;
import com.example.pam_project.utils.validators.FormValidator;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateCategoryActivity extends AppCompatActivity implements CreateCategoryView {
    private static final AppColor DEFAULT_COLOR = AppColor.BLUE;
    private CreateCategoryPresenter presenter;
    private int selectedColor = DEFAULT_COLOR.getARGBValue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        createPresenter();
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_create_category);
        setUpView();
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final SchedulerProvider schedulerProvider = container.getSchedulerProvider();
        final CategoriesRepository repository = container.getCategoriesRepository();
        presenter = new CreateCategoryPresenter(schedulerProvider, repository);
    }

    private void setUpView() {
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
            boolean validForm = FormValidator.validate(getApplicationContext(), createInputMap(categoryName, categoryNameInput));

            String colorName = DEFAULT_COLOR.name();
            if (color != null) {
                colorName = color.name();
            }

            if (validForm) {
                presenter.insertCategory(categoryName, colorName);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private Map<EditText, String> createInputMap(String categoryName, EditText categoryNameInput) {
        Map<EditText, String> map = new HashMap<>();
        map.put(categoryNameInput, categoryName);
        return map;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }
}
