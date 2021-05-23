package com.example.pam_project.lists.lists.listActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.db.mappers.ListMapper;
import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.db.repositories.RoomCategoriesRepository;
import com.example.pam_project.db.repositories.RoomListsRepository;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.landing.SharedPreferencesFtuStorage;
import com.example.pam_project.landing.WelcomeActivity;
import com.example.pam_project.lists.categories.categoryActivity.CategoryActivity;
import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.lists.dialogs.FilterDialogFragment;
import com.example.pam_project.lists.dialogs.SelectedDialogItems;
import com.example.pam_project.lists.dialogs.SortByDialogFragment;
import com.example.pam_project.lists.lists.components.ListAdapter;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.lists.lists.createListActivity.CreateListActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity implements SelectedDialogItems, OnListClickedListener, ListView {
    private static final String PAM_PREF = "app-pref";
    private static final String DIALOG_FRAGMENT_SHOW_TAG = "fragment_alert";
    private final int CREATE_LIST_ACTIVITY_REGISTRY = 1;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private ListPresenter listPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();

        final SharedPreferences sharedPref = getSharedPreferences(PAM_PREF, MODE_PRIVATE);
        final FtuStorage storage = new SharedPreferencesFtuStorage(sharedPref);
        final CategoryMapper categoryMapper = new CategoryMapper();
        final ListMapper listMapper = new ListMapper();
        final CategoriesRepository categoriesRepository = new RoomCategoriesRepository(mainStorage.getStorage().categoryDao(),
                categoryMapper);
        final ListsRepository listsRepository = new RoomListsRepository(mainStorage.getStorage().listDao(),
                mainStorage.getStorage().categoryDao(), listMapper);

        listPresenter = new ListPresenter(storage, categoriesRepository, listsRepository, this);

        setContentView(R.layout.activity_list);
        setup();
    }

    private void setup() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setExtendedFloatingButtonAction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listPresenter.onViewAttached();
    }

    @Override
    public void showLists() {
        adapter = new ListAdapter();
        adapter.setOnClickedListener(this);
        recyclerView.setAdapter(adapter);
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
        listPresenter.onEmptyList();
    }

    @Override
    public void showEmptyMessage() {
        TextView text = findViewById(R.id.empty_list_message);
        View emptyDataMessage = findViewById(R.id.empty_list);

        if(adapter.getItemCount() == 0) {
            text.setText(R.string.empty_list_message);
            emptyDataMessage.setVisibility(View.VISIBLE);
        } else {
            emptyDataMessage.setVisibility(View.GONE);
        }
    }


    @Override
    public void bindList(final ListInformation model) {
        adapter.addItem(model);
        adapter.notifyDataSetChanged();
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

    private void showDialog(FragmentManager fm, DialogFragment dialog) {
        dialog.show(fm, DIALOG_FRAGMENT_SHOW_TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.list_action_bar_search) {
            listPresenter.onSearchBar();
        } else if (itemId == R.id.list_action_bar_filter) {
            listPresenter.onFilterDialog();
        } else if (itemId == R.id.list_action_bar_sort_by) {
            listPresenter.onSortByDialog();
        } else if (itemId == R.id.list_action_bar_manage_categories) {
            listPresenter.onManageCategories();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_LIST_ACTIVITY_REGISTRY) {
            if (resultCode == Activity.RESULT_OK) {
                String listId = data.getStringExtra("listId");
                listPresenter.appendList(Long.parseLong(listId));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        listPresenter.onViewDetached();
    }

    @Override
    public void onClick(final long id) {
        listPresenter.onListClicked(id);
    }


    private void setExtendedFloatingButtonAction() {
        ExtendedFloatingActionButton addListFAB = findViewById(R.id.extended_fab_add_list);
        addListFAB.setOnClickListener(view -> listPresenter.onButtonClicked());
    }
}