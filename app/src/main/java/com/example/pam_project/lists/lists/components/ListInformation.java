package com.example.pam_project.lists.lists.components;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.components.comparators.ListAlphabeticalComparator;
import com.example.pam_project.lists.lists.components.comparators.ListDateAddedComparator;
import com.example.pam_project.lists.lists.components.comparators.ListInformationComparator;
import com.example.pam_project.lists.lists.components.comparators.ListTaskNumberComparator;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.AppColor;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class ListInformation implements Serializable, Comparable<ListInformation> {
    private final String title;
    private long id;
    private long categoryId;
    private AppColor color;
    private List<TaskInformation> tasks;

    // based on R.array.sort_by_criteria
    private static final ListInformationComparator NATURAL_COMPARATOR =
            new ListDateAddedComparator();
    private static final ListInformationComparator[] COMPARATORS = {
            NATURAL_COMPARATOR,
            new ListAlphabeticalComparator(), // TODO: sort_by_category_alphabetically
            new ListAlphabeticalComparator(), // TODO: sort_by_category_priority
            new ListAlphabeticalComparator(),
            new ListTaskNumberComparator(),
    };

    public ListInformation(final long id, final String title, final long categoryId) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
    }

    public ListInformation(final long id, final String title, final long categoryId, AppColor color, List<TaskInformation> tasks) {
        this(id, title, categoryId);
        this.color = color;
        this.tasks = tasks;
    }

    public ListInformation(final long id, final String title, final long categoryId, AppColor color) {
        this(id, title, categoryId, color, null);
    }

    public ListInformation(final long id, final String title, final long categoryId, List<TaskInformation> tasks) {
        this(id, title, categoryId, null, tasks);
    }

    public ListInformation(final String title, AppColor color) {
        this.title = title;
        this.color = color;
    }

    public static Comparator<ListInformation> getComparator(int index) {
        if (index >= COMPARATORS.length || index < 0)
            return null;
        return COMPARATORS[index];
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public AppColor getColor() {
        return color;
    }

    public List<TaskInformation> getTasks() {
        return tasks;
    }

    @Override
    public int compareTo(ListInformation o) {
        return NATURAL_COMPARATOR.compare(this, o);
    }
}