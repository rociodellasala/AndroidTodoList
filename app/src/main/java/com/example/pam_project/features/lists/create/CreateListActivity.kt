package com.example.pam_project.features.lists.create

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.categories.spinner.SpinnerActivity
import com.example.pam_project.features.categories.spinner.SpinnerCategoryAdapter
import com.example.pam_project.utils.validators.FormValidator
import java.util.*

class CreateListActivity : AppCompatActivity(), CreateListView {
    private lateinit var presenter: CreateListPresenter
    private lateinit var adapter: SpinnerCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_list)
        createPresenter()
        supportActionBar?.title=R.string.activity_title_create_list.toString()
        setUpView()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container.schedulerProvider
        val categoriesRepository = container.categoriesRepository
        val listsRepository = container.listsRepository
        presenter = CreateListPresenter(schedulerProvider, categoriesRepository,
                listsRepository, this)
    }

    private fun setUpView() {
        val spinner = findViewById<Spinner>(R.id.create_list_category_spinner)
        adapter = SpinnerCategoryAdapter(this, android.R.layout.simple_spinner_item)
        adapter.categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter.categories
        val spinnerActivity = SpinnerActivity()
        spinner.onItemSelectedListener = spinnerActivity
    }

    public override fun onStart() {
        super.onStart()
        presenter.onViewAttached()
    }

    override fun bindCategories(model: List<CategoryInformation?>?) {
        adapter.update(model)
    }

    override fun onListInsertedError() {
        Toast.makeText(applicationContext, getString(R.string.error_list_create), Toast.LENGTH_LONG).show()
    }

    override fun onCategoriesReceivedError() {
        Toast.makeText(applicationContext, getString(R.string.error_categories_fetch), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.check_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId.toLong()
        val spinner = findViewById<Spinner>(R.id.create_list_category_spinner)
        val listNameInput = findViewById<EditText>(R.id.create_list_title_input)
        if (itemId == R.id.check_add_button.toLong()) {
            val listName = listNameInput.text.toString()
            val categoryId = adapter.categoriesMap[spinner.selectedItem.toString()]
            val validForm = FormValidator.validate(applicationContext, createInputMap(listName, listNameInput))
            if (validForm) {
                presenter.insertList(listName, categoryId)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createInputMap(listName: String, listNameInput: EditText): Map<EditText, String?> {
        val map: MutableMap<EditText, String?> = HashMap()
        map[listNameInput] = listName
        return map
    }

    public override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }
}