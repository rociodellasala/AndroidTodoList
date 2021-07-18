package com.example.pam_project.features.categories.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.features.categories.create.CreateCategoryActivity
import com.example.pam_project.features.lists.list.OnListClickedListener
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class CategoryActivity : AppCompatActivity(), CategoryView, OnListClickedListener {
    private var presenter: CategoryPresenter? = null
    private var adapter: CategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        createPresenter()
        supportActionBar?.title=R.string.activity_title_category.toString()
        setUpView()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container.schedulerProvider
        val repository = container.categoriesRepository
        presenter = CategoryPresenter(schedulerProvider, repository, this)
    }

    override fun onStart() {
        super.onStart()
        presenter!!.onViewAttached()
    }

    private fun setUpView() {
        val recyclerView = findViewById<RecyclerView>(R.id.category)
        recyclerView.setHasFixedSize(true)
        val touchHelper = ItemTouchHelper(setDraggableItems())
        touchHelper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setExtendedFloatingButtonAction()
        adapter = CategoryAdapter()
        adapter!!.setOnClickedListener(this)
        recyclerView.adapter = adapter
    }

    private fun setExtendedFloatingButtonAction() {
        val addTaskFAB = findViewById<ExtendedFloatingActionButton>(R.id.extended_fab_add_category)
        addTaskFAB.setOnClickListener { presenter!!.onButtonAddClicked() }
    }

    override fun showAddCategory() {
        val activityIntent = Intent(applicationContext, CreateCategoryActivity::class.java)
        startActivity(activityIntent)
    }

    override fun bindCategories(model: List<CategoryInformation?>?) {
        adapter!!.update(model)
        presenter!!.onEmptyCategory()
    }

    override fun showEmptyMessage() {
        val text = findViewById<TextView>(R.id.empty_list_message)
        val emptyDataMessage = findViewById<View>(R.id.empty_category)
        if (adapter!!.itemCount == 0) {
            text.setText(R.string.empty_category_message)
            emptyDataMessage.visibility = View.VISIBLE
        } else {
            emptyDataMessage.visibility = View.GONE
        }
    }

    override fun onClick(id: Long) {
        presenter!!.onCategoryClicked(id)
    }

    override fun showCategoryForm(id: Long) {
        val uri = "pam://edit/category?id="
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri + id))
        startActivity(activityIntent)
    }

    override fun onCategoriesSwap(draggedPosition: Int, targetPosition: Int) {
        adapter!!.swapItems(draggedPosition, targetPosition)
    }

    override fun onCategoriesReceivedError() {
        Toast.makeText(applicationContext, getString(R.string.error_categories_fetch), Toast.LENGTH_LONG).show()
    }

    private fun setDraggableItems(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView1: RecyclerView, dragged: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val draggedPosition = dragged.adapterPosition
                val targetPosition = target.adapterPosition
                presenter!!.swapCategories(draggedPosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("SWIPE!", "onSwiped: ")
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return false
            }

            override fun getMovementFlags(recyclerView: RecyclerView,
                                          viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = 0
                return makeMovementFlags(dragFlags, swipeFlags)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        presenter!!.onViewDetached()
    }
}