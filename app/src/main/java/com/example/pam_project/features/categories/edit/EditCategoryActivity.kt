package com.example.pam_project.features.categories.edit

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.utils.constants.AppColor
import com.example.pam_project.utils.validators.FormValidator
import com.thebluealliance.spectrum.SpectrumPalette
import java.util.*

class EditCategoryActivity : AppCompatActivity(), EditCategoryView {
    private var selectedColor = DEFAULT_COLOR.aRGBValue
    private lateinit var presenter: EditCategoryPresenter
    private var categoryId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)
        val id = intent.data!!.getQueryParameter("id")
        categoryId = id!!.toLong()
        createPresenter()
        supportActionBar?.title=R.string.activity_title_edit_category.toString()
        setDeleteButton()
        setUpView()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container.schedulerProvider
        val repository = container.categoriesRepository
        presenter = EditCategoryPresenter(categoryId, schedulerProvider, repository, this)
    }

    private fun setDeleteButton() {
        val deleteButton = findViewById<Button>(R.id.delete_category_button)
        if (categoryId != UNCATEGORIZED.toLong()) {
            deleteButton.setOnClickListener { presenter.onDeletePressed() }
        } else {
            deleteButton.visibility = View.GONE
        }
    }

    override fun showDeleteDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.confirm_category_delete)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_dialog) { _: DialogInterface?, _: Int -> presenter.deleteCategory(categoryId) }
                .setNegativeButton(R.string.cancel_dialog, null)
                .show()
    }

    private fun setUpView() {
        val palette = findViewById<SpectrumPalette>(R.id.edit_category_palette_color)
        val colors = IntArray(AppColor.values().size)
        for (i in colors.indices) {
            colors[i] = AppColor.values()[i].aRGBValue
        }
        palette.setColors(colors)
        palette.setOnColorSelectedListener { color: Int -> selectedColor = color }
    }

    public override fun onStart() {
        super.onStart()
        presenter.onViewAttached()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.check_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId.toLong()
        val categoryNameInput = findViewById<EditText>(R.id.edit_category_name_input)
        if (itemId == R.id.check_add_button.toLong()) {
            val categoryName = categoryNameInput.text.toString()
            val color: AppColor? = AppColor.fromARGBValue(selectedColor)
            val validForm = FormValidator.validate(applicationContext, createInputMap(categoryName, categoryNameInput))
            var colorName = DEFAULT_COLOR.name
            if (color != null) {
                colorName = color.name
            }
            if (validForm) {
                presenter.updateCategory(categoryName, colorName)
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

    override fun bindCategory(model: CategoryInformation?) {
        val title = findViewById<EditText>(R.id.edit_category_name_input)
        val palette = findViewById<SpectrumPalette>(R.id.edit_category_palette_color)
        title.setText(model?.title)
        palette.setSelectedColor(model?.color?.aRGBValue!!)
    }

    override fun onCategoryDelete() {
        finish()
    }

    override fun onCategoryUpdateError() {
        Toast.makeText(applicationContext, getString(R.string.error_category_update), Toast.LENGTH_LONG).show()
    }

    override fun onCategoryRetrievedError() {
        Toast.makeText(applicationContext, getString(R.string.error_category_fetch), Toast.LENGTH_LONG).show()
    }

    override fun onCategoryDeletedError() {
        Toast.makeText(applicationContext, getString(R.string.error_category_delete), Toast.LENGTH_LONG).show()
    }

    public override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    companion object {
        private val DEFAULT_COLOR = AppColor.BLUE
        private const val UNCATEGORIZED = 1
    }
}