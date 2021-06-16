package com.example.pam_project.features.lists.list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.dialogs.FilterDialogFragment;
import com.example.pam_project.dialogs.SelectedDialogItems;
import com.example.pam_project.dialogs.SortByDialogFragment;
import com.example.pam_project.features.about.AboutActivity;
import com.example.pam_project.features.categories.list.CategoryActivity;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.create.CreateListActivity;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.landing.WelcomeActivity;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity implements SelectedDialogItems, OnListClickedListener, ListView {
    private static final String DIALOG_FRAGMENT_SHOW_TAG = "fragment_alert";
    private Menu topMenu;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private ListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        createPresenter();
        setUpView();
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final FtuStorage storage = container.getFtuStorage();
        final SchedulerProvider schedulerProvider = container.getSchedulerProvider();
        final CategoriesRepository categoriesRepository = container.getCategoriesRepository();
        presenter = new ListPresenter(storage, schedulerProvider, categoriesRepository, this);
    }

    private void setUpView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setExtendedFloatingButtonAction();

        adapter = new ListAdapter();
        adapter.setOnClickedListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setExtendedFloatingButtonAction() {
        ExtendedFloatingActionButton addListFAB = findViewById(R.id.extended_fab_add_list);
        addListFAB.setOnClickListener(view -> presenter.onButtonClicked());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached();
    }

    @Override
    public void bindCategories(List<CategoryInformation> model) {
        adapter.updateCategories(model);
    }

    @Override
    public void launchFtu() {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    @Override
    public void bindLists(final List<ListInformation> model) {
        adapter.update(model);
        adapter.setCompleteDataset(model);
        adapter.setPreviousSearchDataset();
        presenter.onEmptyList();
    }

    @Override
    public void showEmptyMessage() {
        TextView text = findViewById(R.id.empty_list_message);
        View emptyDataMessage = findViewById(R.id.empty_list);

        if (adapter.getItemCount() == 0) {
            text.setText(R.string.empty_list_message);
            emptyDataMessage.setVisibility(View.VISIBLE);
        } else {
            emptyDataMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onListsReceivedError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_lists_fetch), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCategoriesReceivedError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_categories_fetch), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showListContent(final long id) {
        String uri = "pam://detail/list?id=";
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri + id));
        startActivity(activityIntent);
    }

    @Override
    public void showAddList() {
        Intent activityIntent = new Intent(getApplicationContext(), CreateListActivity.class);
        startActivity(activityIntent);
    }

    @Override
    public void bindSearchedLists(String searchQuery) {
        adapter.getFilter().filter(searchQuery);
    }

    @Override
    public void unbindSearchedLists(){
        adapter.update(adapter.getPreviousSearchDataset());
    }

    @Override
    public void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialog = FilterDialogFragment.newInstance(adapter.getCategories(), adapter.getFilterSelections());
        showDialog(fm, filterDialog);
    }

    @Override
    public void showSortByDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
        SortByDialogFragment sortByDialog = SortByDialogFragment
                .newInstance(Objects.requireNonNull(adapter).getSortIndex());
        showDialog(fm, sortByDialog);
    }

    private void showDialog(FragmentManager fm, DialogFragment dialog) {
        dialog.show(fm, DIALOG_FRAGMENT_SHOW_TAG);
    }

    @Override
    public void showManageCategories() {
        Intent categoriesIntent = new Intent(getApplicationContext(), CategoryActivity.class);
        startActivity(categoriesIntent);
    }

    @Override
    public void showAboutSection() {
        Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(aboutIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_action_bar, menu);
        topMenu = menu;
        setUpSearch(menu);
        return true;
    }

    private void setUpSearch(Menu menu){
        MenuItem searchItem = menu.findItem(R.id.list_action_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                presenter.performSearch(searchQuery);
                return true;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                CharSequence emptyQuery = "";
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                searchView.setQuery(emptyQuery, false);
                adapter.setPreviousSearchDataset();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                searchView.requestFocus();
                searchView.setIconifiedByDefault(true);
                presenter.onSearchDetached();
                return true;
            }
        });
    }

    @Override
    public void unFocusSearch(){
        if(topMenu == null)
            return;
        MenuItem searchItem = topMenu.findItem(R.id.list_action_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchItem.collapseActionView();
        searchView.clearFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.list_action_bar_filter) {
            presenter.onFilterDialog();
        } else if (itemId == R.id.list_action_bar_sort_by) {
            presenter.onSortByDialog();
        } else if (itemId == R.id.list_action_bar_manage_categories) {
            presenter.onManageCategories();
        } else if (itemId == R.id.list_action_bar_about) {
            presenter.onAboutSection();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onSelectedItems(Class<?> klass, List<Integer> items) {
        if (klass.equals(SortByDialogFragment.class))
            adapter.setSortIndex(items.get(0));
        else
            adapter.setFilterSelections(items);
    }

    @Override
    public void onClick(final long id) {
        presenter.onListClicked(id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }
}