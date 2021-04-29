package com.example.pam_project.lists.lists;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.utils.Database;
import com.example.pam_project.db.utils.Storage;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.landing.SharedPreferencesFtuStorage;
import com.example.pam_project.landing.WelcomeActivity;

import com.example.pam_project.lists.categories.CategoriesActivity;

import com.example.pam_project.lists.dialogs.FilterDialogFragment;
import com.example.pam_project.lists.dialogs.SelectedDialogItems;
import com.example.pam_project.lists.dialogs.SortByDialogFragment;
import com.example.pam_project.utils.AppColor;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ListActivity extends AppCompatActivity implements SelectedDialogItems, OnListClickedListener, ListView {
    private static final String PAM_PREF = "app-pref";
    private static final String DIALOG_FRAGMENT_SHOW_TAG = "fragment_alert";

    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private ListPresenter listPresenter;
    private final int CREATE_LIST_ACTIVITY_REGISTRY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Storage mainStorage = new Database(this.getApplicationContext());
        mainStorage.setUpStorage();
        mainStorage.populateStorage();

        final SharedPreferences sharedPref = getSharedPreferences(PAM_PREF, MODE_PRIVATE);
        final FtuStorage storage = new SharedPreferencesFtuStorage(sharedPref);
        final CategoriesRepository categoriesRepository = new RoomCategoriesRepository(mainStorage.getStorage().categoryDao());

        listPresenter = new ListPresenter(storage, categoriesRepository, this);

        setContentView(R.layout.activity_list);
        setup();
    }


    @Override
    protected void onStart() {
        super.onStart();
        listPresenter.onViewAttached();
    }

    @Override
    public void showLists(){
        adapter = new ListAdapter();
        adapter.setOnClickedListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void launchFtu() {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    @Override
    public void bindLists(final List<ListInformation> model) {
        adapter.update(model);
    }

    @Override
    public void showListContent(final long id){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pam://detail/list?id=" + id)));
    }

    private void setup() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setExtendedFloatingButtonAction();
    }

    private void setExtendedFloatingButtonAction() {
        ExtendedFloatingActionButton addListFAB = findViewById(R.id.extended_fab_add_list);
        addListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(getApplicationContext(), CreateListActivity.class);
                startActivityForResult(activityIntent, CREATE_LIST_ACTIVITY_REGISTRY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final List<AppColor> colors = Arrays.asList(AppColor.values()); // must be removed

        if (requestCode == CREATE_LIST_ACTIVITY_REGISTRY) {
            if (resultCode == Activity.RESULT_OK) {
                String newListTile = data.getStringExtra("listTile");
                String categoryId = data.getStringExtra("categoryId");
                ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
                this.insertNewList(newListTile, Integer.valueOf(categoryId));
                /* Color harcodeado */
                adapter.addItem(new ListInformation(newListTile, colors.get(0)));
                adapter.notifyDataSetChanged();
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    private void insertNewList(String name, int categoryId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                ListEntity listEntity = new ListEntity(name, categoryId);
                //long id = db.listDao().insertList(listEntity);
            }
        }).onErrorComplete().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
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
            // show search bar
            return true;
        }

        // cannot create static abstract method, so some code has to be repeated
        else if (itemId == R.id.list_action_bar_filter) {
            FragmentManager fm = getSupportFragmentManager();
            ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
            FilterDialogFragment filterDialog = FilterDialogFragment
                    .newInstance(adapter.getFilterSelections());
            showDialog(fm, filterDialog);
            return true;
        } else if (itemId == R.id.list_action_bar_sort_by) {
            FragmentManager fm = getSupportFragmentManager();
            ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
            SortByDialogFragment sortByDialog = SortByDialogFragment
                    .newInstance(adapter.getSortIndex());
            showDialog(fm, sortByDialog);
            return true;
        } else if (itemId == R.id.list_action_bar_manage_categories) {
            Intent categoriesIntent = new Intent(getApplicationContext(), CategoriesActivity.class);
            startActivity(categoriesIntent);
            return true;
        } else {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSelectedItems(Class<?> klass, List<Integer> items) {
        // do something with the selected items

        CharSequence value = "No selection";
        if (klass.equals(SortByDialogFragment.class)) {
            final CharSequence[] vals = getResources().getStringArray(R.array.sort_by_criteria);
            value = vals[items.get(0)];
            ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
            adapter.setSortIndex(items.get(0));
        } else {
            if (items.size() > 0)
                value = FilterDialogFragment.FILTER_ITEMS[items.get(0)];
            ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
            adapter.setFilterSelections(items);
        }
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
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

}