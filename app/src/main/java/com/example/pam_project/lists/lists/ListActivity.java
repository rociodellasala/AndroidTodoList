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

import com.example.pam_project.DatabaseHelper;
import com.example.pam_project.R;

import com.example.pam_project.db.AppDatabase;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.relationships.CategoriesWithLists;
import com.example.pam_project.landing.WelcomeActivity;

import com.example.pam_project.lists.categories.CategoriesActivity;

import com.example.pam_project.lists.dialogs.FilterDialogFragment;
import com.example.pam_project.lists.dialogs.SelectedDialogItems;
import com.example.pam_project.lists.dialogs.SortByDialogFragment;
import com.example.pam_project.utils.AppColor;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ListActivity extends AppCompatActivity implements SelectedDialogItems, OnListClickedListener {
    private static final String FTU_KEY = "is_ftu";
    private static final String PAM_PREF = "app-pref";
    private static final String DIALOG_FRAGMENT_SHOW_TAG = "fragment_alert";

    private RecyclerView recyclerView;
    private final int CREATE_LIST_ACTIVITY_REGISTRY = 1;

    private Disposable disposable;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDatabase();

        final SharedPreferences sharedPref = getSharedPreferences(PAM_PREF, MODE_PRIVATE);

        // TODO: Lo comento por la base por ahora
//        if (sharedPref.getBoolean(FTU_KEY, true)) {
//            sharedPref.edit().putBoolean(FTU_KEY, false).apply();
//            startActivity(new Intent(this, WelcomeActivity.class));
//        }

        setContentView(R.layout.activity_list);
        setup();
    }

    private void setUpDatabase() {
        db = AppDatabase.getInstance(getApplicationContext());

        // Si quieren borrar toda su base descomentar esto
//        Completable.fromAction(() ->
//                AppDatabase.nukeDatabase()
//        ).onErrorComplete().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();

        // Si quieren llenar su base descomentar esto !!!!!
//        DatabaseHelper helper = new DatabaseHelper();
//        helper.createDB(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

        disposable = db.categoryDao().getAllCategoriesWithLists()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    final ListAdapter adapter = new ListAdapter(adaptModel(model));
                    adapter.setOnClickedListener(this);
                    recyclerView.setAdapter(adapter);
                });
    }

    private List<ListInformation> adaptModel(List<CategoriesWithLists> model) {
        final List<ListInformation> list = new ArrayList<>();

        for (final CategoriesWithLists entity : model) {
            for (final ListEntity listEntity : entity.lists) {
                AppColor color = findColor(entity.category.color);
                list.add(new ListInformation(listEntity.id, listEntity.name, /*10, */ color));
            }
        }

        return list;
    }

    public static AppColor findColor(String color) {
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for (int i = 0; i < colors.size(); i++) {
            if (color.equals(colors.get(i).toString())) {
                return colors.get(i);
            }
        }

        return null;
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
                long id = db.listDao().insertList(listEntity);
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
        disposable.dispose();
    }

    @Override
    public void onClick(final int id) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pam://detail/list?id=" + id)));
    }
}