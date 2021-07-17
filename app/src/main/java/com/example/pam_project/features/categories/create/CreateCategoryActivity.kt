package com.example.pam_project.features.categories.create

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.utils.constants.AppColor
import com.example.pam_project.utils.validators.FormValidator
import com.thebluealliance.spectrum.SpectrumPalette
import java.util.*

class CreateCategoryActivity : AppCompatActivity(), CreateCategoryView {
    private lateinit var presenter: CreateCategoryPresenter
    private var selectedColor = DEFAULT_COLOR.aRGBValue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)
        createPresenter()
        supportActionBar?.setTitle(R.string.activity_title_create_category)
        setUpView()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container?.schedulerProvider
        val repository = container?.categoriesRepository
        presenter = CreateCategoryPresenter(schedulerProvider, repository, this)
    }

    private fun setUpView() {
        val palette = findViewById<SpectrumPalette>(R.id.create_category_palette_color)
        val colors = IntArray(AppColor.values().size)
        for (i in colors.indices) {
            colors[i] = AppColor.values()[i].aRGBValue
        }
        palette.setColors(colors)
        palette.setSelectedColor(selectedColor)
        palette.setOnColorSelectedListener { color: Int -> selectedColor = color }
    }

    public override fun onStart() {
        super.onStart()
    }

    override fun onCategoryInsertedError() {
        Toast.makeText(applicationContext, getString(R.string.error_category_create), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.check_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId.toLong()
        val categoryNameInput = findViewById<EditText>(R.id.create_category_name_input)
        if (itemId == R.id.check_add_button.toLong()) {
            val categoryName = categoryNameInput.text.toString()
            val color: AppColor? = AppColor.fromARGBValue(selectedColor)
            val validForm = FormValidator.validate(applicationContext, createInputMap(categoryName, categoryNameInput))
            var colorName = DEFAULT_COLOR.name
            if (color != null) {
                colorName = color.name
            }
            if (validForm) {
                presenter.insertCategory(categoryName, colorName)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createInputMap(categoryName: String, categoryNameInput: EditText): Map<EditText, String?> {
        val map: MutableMap<EditText, String?> = HashMap()
        map[categoryNameInput] = categoryName
        return map
    }

    public override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    companion object {
        private val DEFAULT_COLOR = AppColor.BLUE
    }
}