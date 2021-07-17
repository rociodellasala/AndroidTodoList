package com.example.pam_project.features.lists.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.dialogs.FilterDialogFragment
import com.example.pam_project.dialogs.SelectedDialogItems
import com.example.pam_project.dialogs.SortByDialogFragment
import com.example.pam_project.features.about.AboutActivity
import com.example.pam_project.features.categories.list.CategoryActivity
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.create.CreateListActivity
import com.example.pam_project.landing.WelcomeActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.*

class ListActivity : AppCompatActivity(), SelectedDialogItems, OnListClickedListener, ListView {
    private var topMenu: Menu? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private lateinit var presenter: ListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        createPresenter()
        setUpView()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val storage = container?.ftuStorage
        val schedulerProvider = container?.schedulerProvider
        val categoriesRepository = container?.categoriesRepository
        presenter = ListPresenter(storage, schedulerProvider, categoriesRepository, this)
    }

    private fun setUpView() {
        recyclerView = findViewById(R.id.list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setExtendedFloatingButtonAction()
        adapter = ListAdapter()
        adapter.setOnClickedListener(this)
        recyclerView.adapter = adapter
    }

    private fun setExtendedFloatingButtonAction() {
        val addListFAB = findViewById<ExtendedFloatingActionButton>(R.id.extended_fab_add_list)
        addListFAB.setOnClickListener { presenter.onButtonClicked() }
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached()
    }

    override fun bindCategories(model: List<CategoryInformation?>?) {
        adapter.updateCategories(model)
    }

    override fun launchFtu() {
        startActivity(Intent(this, WelcomeActivity::class.java))
    }

    override fun bindLists(model: List<ListInformation?>?) {
        adapter.update(model)
        adapter.setCompleteDataset(model)
        adapter.setPreviousSearchDataset()
        presenter.onEmptyList()
    }

    override fun showEmptyMessage() {
        val text = findViewById<TextView>(R.id.empty_list_message)
        val emptyDataMessage = findViewById<View>(R.id.empty_list)
        if (adapter.itemCount == 0) {
            text.setText(R.string.empty_list_message)
            emptyDataMessage.visibility = View.VISIBLE
        } else {
            emptyDataMessage.visibility = View.GONE
        }
    }

    override fun onListsReceivedError() {
        Toast.makeText(applicationContext, getString(R.string.error_lists_fetch), Toast.LENGTH_LONG).show()
    }

    override fun onCategoriesReceivedError() {
        Toast.makeText(applicationContext, getString(R.string.error_categories_fetch), Toast.LENGTH_LONG).show()
    }

    override fun showListContent(id: Long) {
        val uri = "pam://detail/list?id="
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri + id))
        startActivity(activityIntent)
    }

    override fun showAddList() {
        val activityIntent = Intent(applicationContext, CreateListActivity::class.java)
        startActivity(activityIntent)
    }

    override fun bindSearchedLists(searchQuery: String?) {
        adapter.filter.filter(searchQuery)
    }

    override fun unbindSearchedLists() {
        adapter.update(adapter.previousSearchDataset)
    }

    override fun showFilterDialog() {
        // TODO DESCOMENTAR Y ARREGLAR
        /* val fm = supportFragmentManager
        val filterDialog: FilterDialogFragment = FilterDialogFragment.Companion.newInstance(adapter!!.categories, adapter.filterSelection)
        showDialog(fm, filterDialog) */
    }

    override fun showSortByDialog() {
        val fm = supportFragmentManager
        val adapter = recyclerView.adapter as ListAdapter?
        val sortByDialog: SortByDialogFragment = SortByDialogFragment.newInstance(adapter?.sortIndex!!)
        showDialog(fm, sortByDialog)
    }

    private fun showDialog(fm: FragmentManager, dialog: DialogFragment) {
        dialog.show(fm, DIALOG_FRAGMENT_SHOW_TAG)
    }

    override fun showManageCategories() {
        val categoriesIntent = Intent(applicationContext, CategoryActivity::class.java)
        startActivity(categoriesIntent)
    }

    override fun showAboutSection() {
        val aboutIntent = Intent(applicationContext, AboutActivity::class.java)
        startActivity(aboutIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        print("ACAAAAAAAAAAAAA")
        val inflater = menuInflater
        inflater.inflate(R.menu.list_action_bar, menu)
        topMenu = menu
        setUpSearch(menu)
        return true
    }

    private fun setUpSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.list_action_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(searchQuery: String): Boolean {
                presenter.performSearch(searchQuery)
                return true
            }
        })
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                val emptyQuery: CharSequence = ""
                searchView.isIconified = false
                searchView.requestFocusFromTouch()
                searchView.setQuery(emptyQuery, false)
                adapter.setPreviousSearchDataset()
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                searchView.requestFocus()
                searchView.isIconifiedByDefault = true
                presenter.onSearchDetached()
                return true
            }
        })
    }

    override fun unFocusSearch() {
        val searchItem = topMenu?.findItem(R.id.list_action_bar_search)
        val searchView = searchItem?.actionView
        searchItem?.collapseActionView()
        searchView?.clearFocus()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.list_action_bar_filter) {
            presenter.onFilterDialog()
        } else if (itemId == R.id.list_action_bar_sort_by) {
            presenter.onSortByDialog()
        } else if (itemId == R.id.list_action_bar_manage_categories) {
            presenter.onManageCategories()
        } else if (itemId == R.id.list_action_bar_about) {
            presenter.onAboutSection()
        } else {
            return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onSelectedItems(klass: Class<*>?, items: MutableList<Int>?) {
        // TODO DESCOMENTAR Y ARREGLAR
        // if (klass == SortByDialogFragment::class.java) adapter.sortIndex = items!![0] else adapter.setFilterSelections(items)
    }

    override fun onClick(id: Long) {
        presenter.onListClicked(id)
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    companion object {
        private const val DIALOG_FRAGMENT_SHOW_TAG = "fragment_alert"
    }
}