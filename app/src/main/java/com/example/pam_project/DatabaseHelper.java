package com.example.pam_project;

import android.content.Context;

import com.example.pam_project.db.AppDatabase;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.utils.AppColor;
import com.example.pam_project.utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class DatabaseHelper {

    public void createDB(Context context) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                final long[] categoriesIds = AppDatabase.getInstance(context.getApplicationContext()).categoryDao().insertAllCategories(createCategoriesDataSet());
                final long[] listIds = AppDatabase.getInstance(context.getApplicationContext()).listDao().insertAllLists(createListsDataSet(categoriesIds));
                AppDatabase.getInstance(context.getApplicationContext()).taskDao().insertAllTasks(createTasksDataSet(listIds));
            }
        }).onErrorComplete().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();

    }

    private List<CategoryEntity> createCategoriesDataSet() {
        List<CategoryEntity> listOfCategories = new ArrayList<>();
        String[] names = {"Supermercado", "Verduleria", "PAM"};
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for (int i = 0; i < names.length; i++) {
            CategoryEntity category = new CategoryEntity(names[i], colors.get(i).toString());
            listOfCategories.add(category);
        }

        return listOfCategories;
    }

    private List<ListEntity> createListsDataSet(final long[] categoriesIds) {
        List<ListEntity> listofLists = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            double indexId = Math.floor(Math.random() * categoriesIds.length) + 1;
            int categoryId = Helper.getRandomFromArray(categoriesIds);
            ListEntity list = new ListEntity("Lista " + i, categoryId);
            listofLists.add(list);
        }

        return listofLists;
    }

    private List<TaskEntity> createTasksDataSet(final long[] listIds) {
        List<TaskEntity> listOfTasks = new ArrayList<>();

        for(int i = 0; i < listIds.length; i++) {
            int numberOfTasks = Helper.getRandom(1, 15);
            for(int j = 0; j < numberOfTasks; j++) {
                Random random = new Random();
                boolean randomBoolean = random.nextBoolean();
                String status = (randomBoolean == true) ? "pending" : "done";
                TaskEntity task = new TaskEntity("Tarea " + j, "Descripcion " + j, randomBoolean,
                         status, (int) listIds[i]);
                listOfTasks.add(task);
            }
        }

        return listOfTasks;
    }
}
