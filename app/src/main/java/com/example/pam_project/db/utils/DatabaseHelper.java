package com.example.pam_project.db.utils;

import android.content.Context;

import com.example.pam_project.R;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.utils.AppColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DatabaseHelper {

    public static long getRandomFromArray(long[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public void createDB(Context context) {
        Completable.fromAction(() -> {
            Context appContext = context.getApplicationContext();
            AppDatabase db = AppDatabase.getInstance(appContext);
            final long[] categoriesIds = db.categoryDao().insertAllCategories(createCategoriesDataSet(appContext));
            final long[] listIds = db.listDao().insertAllLists(createListsDataSet(categoriesIds));
            db.taskDao().insertAllTasks(createTasksDataSet(listIds));
        }).onErrorComplete().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();

    }

    private List<CategoryEntity> createCategoriesDataSet(Context context) {
        List<CategoryEntity> listOfCategories = new ArrayList<>();
        String defaultCategory = context.getResources().getString(R.string.default_category);
        String[] names = {defaultCategory, "Supermarket", "Grocery", "PAM"};
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for (int i = 0; i < names.length && i < colors.size(); i++) {
            CategoryEntity category = new CategoryEntity(names[i], colors.get(i).toString());
            listOfCategories.add(category);
        }

        return listOfCategories;
    }

    private List<ListEntity> createListsDataSet(final long[] categoriesIds) {
        List<ListEntity> listofLists = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            double indexId = Math.floor(Math.random() * categoriesIds.length) + 1;
            long categoryId = getRandomFromArray(categoriesIds);
            ListEntity list = new ListEntity("List " + i, categoryId);
            listofLists.add(list);
        }

        return listofLists;
    }

    private List<TaskEntity> createTasksDataSet(final long[] listIds) {
        List<TaskEntity> listOfTasks = new ArrayList<>();

        for (long listId : listIds) {
            int numberOfTasks = getRandom(1, 15);
            for (int j = 0; j < numberOfTasks; j++) {
                Random random = new Random();
                boolean randomBoolean = random.nextBoolean();
                String status = randomBoolean ? "pending" : "done";
                TaskEntity task = new TaskEntity("Task " + j, "Description " + j, randomBoolean,
                        status, (int) listId);
                listOfTasks.add(task);
            }
        }

        return listOfTasks;
    }
}