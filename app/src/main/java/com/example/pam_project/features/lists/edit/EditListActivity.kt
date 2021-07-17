package com.example.pam_project.features.lists.edit

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.categories.spinner.SpinnerActivity
import com.example.pam_project.features.categories.spinner.SpinnerCategoryAdapter
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.utils.constants.ActivityResultCode
import com.example.pam_project.utils.validators.FormValidator
import java.util.*

class EditListActivity : AppCompatActivity(), EditListView {
    private lateinit var presenter: EditListPresenter
    private lateinit var adapter: SpinnerCategoryAdapter
    private lateinit var spinner: Spinner
    private var listId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_list)
        createPresenter()
        listId = intent.getLongExtra(LIST_ID_PARAMETER, -1)
        supportActionBar?.setTitle(R.string.activity_title_edit_list)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setDeleteButton()
        setUpView()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container?.schedulerProvider
        val categoriesRepository = container?.categoriesRepository
        val listsRepository = container?.listsRepository
        presenter = EditListPresenter(schedulerProvider, categoriesRepository,
                listsRepository, this)
    }

    private fun setUpView() {
        spinner = findViewById(R.id.edit_list_category_spinner)
        adapter = SpinnerCategoryAdapter(this, android.R.layout.simple_spinner_item)
        adapter.categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter.categories
        val spinnerActivity = SpinnerActivity()
        spinner.onItemSelectedListener = spinnerActivity
    }

    public override fun onStart() {
        super.onStart()
        presenter.onViewAttached(listId)
    }

    private fun setDeleteButton() {
        val deleteButton = findViewById<Button>(R.id.delete_list_button)
        deleteButton.setOnClickListener { presenter.onDeletePressed() }
    }

    override fun showDeleteDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.confirm_list_delete)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_dialog) { _: DialogInterface?, _: Int -> presenter.deleteList(listId) }
                .setNegativeButton(R.string.cancel_dialog, null)
                .show()
    }

    override fun bindCategories(model: List<CategoryInformation?>?) {
        adapter.update(model)
    }

    override fun bindList(model: ListInformation?) {
        val title = findViewById<EditText>(R.id.edit_list_title_input)
        val categoryId = model?.categoryId
        val spinnerPosition = adapter.categories.getPosition(adapter.getCategoryById(categoryId!!))
        title.setText(model.title)
        spinner.setSelection(spinnerPosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.check_action_bar, menu)
        return true
    }

    override fun onListDelete() {
        val returnIntent = Intent()
        setResult(ActivityResultCode.DELETE_LIST_CODE.ordinal, returnIntent)
        finish()
    }

    override fun onListDeletedError() {
        Toast.makeText(applicationContext, getString(R.string.error_list_delete), Toast.LENGTH_LONG).show()
    }

    override fun onListUpdatedError() {
        Toast.makeText(applicationContext, getString(R.string.error_list_update), Toast.LENGTH_LONG).show()
    }

    override fun onCategoriesReceivedError() {
        Toast.makeText(applicationContext, getString(R.string.error_categories_fetch), Toast.LENGTH_LONG).show()
    }

    override fun onListReceivedError() {
        Toast.makeText(applicationContext, getString(R.string.error_list_fetch), Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId.toLong()
        val spinner = findViewById<Spinner>(R.id.edit_list_category_spinner)
        val listNameInput = findViewById<EditText>(R.id.edit_list_title_input)
        if (itemId == R.id.check_add_button.toLong()) {
            val listName = listNameInput.text.toString()
            val categoryId = adapter.categoriesMap[spinner.selectedItem.toString()]
            val validForm = FormValidator.validate(applicationContext, createInputMap(listName, listNameInput))
            if (validForm) {
                presenter.updateList(listId, listName, categoryId)
                finish()
            }
        } else if (itemId == android.R.id.home.toLong()) {
            onBackPressed()
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

    companion object {
        private const val LIST_ID_PARAMETER = "id"
    }
}