package com.example.pam_project.features.tasks.create

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.utils.validators.FormValidator
import java.util.*

class CreateTaskActivity : AppCompatActivity(), CreateTaskView {
    private lateinit var presenter: CreateTaskPresenter
    private var listId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        createPresenter()
        listId = intent.getLongExtra(LIST_ID_PARAMETER, -1)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title=R.string.activity_title_create_task.toString()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container.schedulerProvider
        val taskRepository = container.tasksRepository
        presenter = CreateTaskPresenter(schedulerProvider, taskRepository, this)
    }

    public override fun onStart() {
        super.onStart()
    }

    override fun onTaskInsertedError() {
        Toast.makeText(applicationContext, getString(R.string.error_task_create), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.check_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId.toLong()
        val taskNameInput = findViewById<EditText>(R.id.create_task_title_input)
        val taskDescriptionInput = findViewById<EditText>(R.id.create_task_description_input)
        val checkboxUrgencyInput = findViewById<CheckBox>(R.id.create_task_priority_checkbox)
        if (itemId == R.id.check_add_button.toLong()) {
            val taskName = taskNameInput.text.toString()
            val taskDescription = taskDescriptionInput.text.toString()
            val taskUrgency = checkboxUrgencyInput.isChecked
            val validForm = FormValidator.validate(applicationContext, createInputMap(taskName, taskNameInput))
            if (validForm) {
                presenter.insertTask(taskName, taskDescription, taskUrgency, listId!!)
                finish()
            }
        } else if (itemId == android.R.id.home.toLong()) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createInputMap(taskName: String, taskNameInput: EditText): Map<EditText, String?> {
        val map: MutableMap<EditText, String?> = HashMap()
        map[taskNameInput] = taskName
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