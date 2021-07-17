package com.example.pam_project.features.tasks.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.features.lists.edit.EditListActivity
import com.example.pam_project.features.lists.list.OnListClickedListener
import com.example.pam_project.features.tasks.create.CreateTaskActivity
import com.example.pam_project.utils.constants.ActivityResultCode
import com.example.pam_project.utils.constants.TaskStatus
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class TaskActivity : AppCompatActivity(), TaskView, OnListClickedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var presenter: TaskPresenter
    private var listId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        val id = intent.data!!.getQueryParameter("id")
        listId = id!!.toLong()
        setUpView()
        createPresenter()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container?.schedulerProvider
        val taskRepository = container?.tasksRepository
        val listsRepository = container?.listsRepository
        presenter = TaskPresenter(schedulerProvider, taskRepository, listsRepository,
                this, listId)
    }

    private fun setUpView() {
        recyclerView = findViewById(R.id.allTasks)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val touchHelper = ItemTouchHelper(setSwippableItems())
        touchHelper.attachToRecyclerView(recyclerView)
        setExtendedFloatingButtonAction()
        adapter = TaskAdapter()
        adapter.setOnClickedListener(this)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.edit_action_bar, menu)
        return true
    }

    private fun setExtendedFloatingButtonAction() {
        val addTaskFAB = findViewById<ExtendedFloatingActionButton>(R.id.extended_fab_add_task)
        addTaskFAB.setOnClickListener { presenter.onButtonAddClicked() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_LIST_ACTIVITY) {
            if (resultCode == ActivityResultCode.DELETE_LIST_CODE.ordinal) {
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId.toLong()
        if (id == R.id.edit_list_button.toLong()) {
            presenter.onEditList()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun bindTasks(model: List<TaskInformation?>?) {
        adapter.update(model)
        presenter.onEmptyTask()
    }

    override fun bindHeaders(headers: IntArray) {
        val titleHeaders = arrayOf(getString(headers[0]), getString(headers[1]))
        recyclerView.addItemDecoration(CustomItemDecorator(this, R.layout.text_header, titleHeaders))
    }

    override fun showEmptyMessage() {
        val text = findViewById<TextView>(R.id.empty_list_message)
        val emptyDataMessage = findViewById<View>(R.id.empty_task)
        if (adapter.itemCount == 0) {
            text.setText(R.string.empty_task_message)
            emptyDataMessage.visibility = View.VISIBLE
        } else {
            emptyDataMessage.visibility = View.GONE
        }
    }

    override fun showEditList() {
        val activityIntent = Intent(applicationContext, EditListActivity::class.java)
        activityIntent.putExtra("id", listId)
        startActivityForResult(activityIntent, EDIT_LIST_ACTIVITY)
    }

    override fun onTasksReceivedError() {
        Toast.makeText(applicationContext, getString(R.string.error_tasks_fetch), Toast.LENGTH_LONG).show()
    }

    override fun onTaskUpdatedError() {
        Toast.makeText(applicationContext, getString(R.string.error_task_update), Toast.LENGTH_LONG).show()
    }

    override fun bindListName(name: String?) {
        supportActionBar?.title = name
    }

    override fun showAddTask() {
        val activityIntent = Intent(applicationContext, CreateTaskActivity::class.java)
        activityIntent.putExtra("id", listId)
        startActivity(activityIntent)
    }

    override fun showTaskContent(id: Long) {
        val uri = "pam://edit/task?id="
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri + id))
        startActivity(activityIntent)
    }

    override fun onTaskStatusEdit(model: TaskInformation?, adapterPosition: Int) {
        adapter.update(model, adapterPosition)
        if (adapter.areAllComplete()) finish()
    }

    private fun setSwippableItems(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView1: RecyclerView,
                                dragged: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(swippedItem: RecyclerView.ViewHolder, direction: Int) {
                val taskInformation = adapter.getItem(swippedItem.adapterPosition)
                presenter.onTaskChange(taskInformation, swippedItem.adapterPosition)
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }

            override fun getMovementFlags(recyclerView: RecyclerView,
                                          swippedItem: RecyclerView.ViewHolder): Int {
                val dragFlags = 0
                val leftSwipeFlag = ItemTouchHelper.LEFT
                val rightSwipeFlag = ItemTouchHelper.RIGHT
                val taskInformation = adapter.getItem(swippedItem.adapterPosition)
                return if (taskInformation!!.status == TaskStatus.DONE) makeMovementFlags(dragFlags, rightSwipeFlag) else makeMovementFlags(dragFlags, leftSwipeFlag)
            }
        }
    }

    override fun onClick(id: Long) {
        presenter.onTaskClicked(id)
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    companion object {
        private const val EDIT_LIST_ACTIVITY = 1
    }
}