package com.example.pam_project.features.tasks.edit

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.validators.FormValidator
import java.util.*

class EditTaskActivity : AppCompatActivity(), EditTaskView {
    private lateinit var presenter: EditTaskPresenter
    private var taskId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        taskId = intent.data?.getQueryParameter(TASK_ID_PARAMETER)!!.toLong()
        createPresenter()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.activity_title_edit_task)
        setDeleteButton()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container.schedulerProvider
        val taskRepository = container.tasksRepository
        presenter = EditTaskPresenter(taskId, schedulerProvider, taskRepository, this)
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached()
    }

    override fun bindTask(model: TaskInformation?) {
        val name = findViewById<EditText>(R.id.edit_task_title_input)
        val description = findViewById<EditText>(R.id.edit_task_description_input)
        val urgency = findViewById<CheckBox>(R.id.edit_task_priority_checkbox)
        name.setText(model?.title)
        description.setText(model?.description)
        urgency.isChecked = model?.urgency == true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId.toLong()
        val taskNameInput = findViewById<EditText>(R.id.edit_task_title_input)
        val taskDescriptionInput = findViewById<EditText>(R.id.edit_task_description_input)
        val checkboxUrgencyInput = findViewById<CheckBox>(R.id.edit_task_priority_checkbox)
        if (itemId == R.id.check_add_button.toLong()) {
            val taskName = taskNameInput.text.toString()
            val taskDescription = taskDescriptionInput.text.toString()
            val taskUrgency = checkboxUrgencyInput.isChecked
            val validForm = FormValidator.validate(applicationContext, createInputMap(taskName, taskNameInput))
            if (validForm) {
                presenter.updateTask(taskName, taskDescription, taskUrgency)
                finish()
            }
        } else if (itemId == android.R.id.home.toLong()) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createInputMap(taskName: String, listNameInput: EditText): Map<EditText, String?> {
        val map: MutableMap<EditText, String?> = HashMap()
        map[listNameInput] = taskName
        return map
    }

    private fun setDeleteButton() {
        val deleteButton = findViewById<Button>(R.id.delete_task_button)
        deleteButton.setOnClickListener { v: View? -> presenter.onDeletePressed() }
    }

    override fun showDeleteDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.confirm_task_delete)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_dialog) { dialog: DialogInterface?, id: Int -> presenter.deleteTask(taskId) }
                .setNegativeButton(R.string.cancel_dialog, null)
                .show()
    }

    override fun onTaskDelete() {
        finish()
    }

    override fun onTaskUpdatedError() {
        Toast.makeText(applicationContext, getString(R.string.error_task_update), Toast.LENGTH_LONG).show()
    }

    override fun onTaskDeletedError() {
        Toast.makeText(applicationContext, getString(R.string.error_task_delete), Toast.LENGTH_LONG).show()
    }

    override fun onTaskRetrievedError() {
        Toast.makeText(applicationContext, getString(R.string.error_task_fetch), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.check_action_bar, menu)
        return true
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    companion object {
        private const val TASK_ID_PARAMETER = "id"
    }
}