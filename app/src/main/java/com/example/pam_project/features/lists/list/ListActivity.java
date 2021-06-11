package com.example.pam_project.features.lists.list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.example.pam_project.features.categories.list.CategoryActivity;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.create.CreateListActivity;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.landing.WelcomeActivity;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity implements SelectedDialogItems, OnListClickedListener, ListView {
    private static final String DIALOG_FRAGMENT_SHOW_TAG = "fragment_alert";
    private static final int CREATE_LIST_ACTIVITY_REGISTRY = 0;
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
        presenter = (ListPresenter) getLastNonConfigurationInstance();

        if (presenter == null) {
            final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);

            final FtuStorage storage = container.getFtuStorage();
            final CategoriesRepository categoriesRepository = container.getCategoriesRepository();

            presenter = new ListPresenter(storage, categoriesRepository, this);
        }
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
    public void bindCategories(final List<CategoryInformation> model) {
        adapter.updateCategories(model);
    }

    @Override
    public void launchFtu() {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    @Override
    public void bindLists(final List<ListInformation> model) {
        adapter.update(model);
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
    public void showListContent(final long id) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pam://detail/list?id=" + id)));
    }

    @Override
    public void showAddList() {
        Intent activityIntent = new Intent(getApplicationContext(), CreateListActivity.class);
        startActivityForResult(activityIntent, CREATE_LIST_ACTIVITY_REGISTRY);
    }

    @Override
    public void showSearchBar() {
        // TODO search
    }

    @Override
    public void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialog = FilterDialogFragment
                .newInstance(adapter.getCategories(), adapter.getFilterSelections());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.list_action_bar_search) {
            presenter.onSearchBar();
        } else if (itemId == R.id.list_action_bar_filter) {
            presenter.onFilterDialog();
        } else if (itemId == R.id.list_action_bar_sort_by) {
            presenter.onSortByDialog();
        } else if (itemId == R.id.list_action_bar_manage_categories) {
            presenter.onManageCategories();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    // TODO: remove toast
    @Override
    public void onSelectedItems(Class<?> klass, List<Integer> items) {
        CharSequence value = "No selection";
        if (klass.equals(SortByDialogFragment.class)) {
            final CharSequence[] vals = getResources().getStringArray(R.array.sort_by_criteria);
            value = vals[items.get(0)];
            adapter.setSortIndex(items.get(0));
        } else {
            if (items.size() > 0)
                value = items.toString();
            adapter.setFilterSelections(items);
        }
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
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