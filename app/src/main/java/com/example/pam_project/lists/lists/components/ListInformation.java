package com.example.pam_project.lists.lists.components;

import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.lists.lists.components.comparators.CategoryAlphabeticalComparator;
import com.example.pam_project.lists.lists.components.comparators.CategoryPriorityComparator;
import com.example.pam_project.lists.lists.components.comparators.ListAlphabeticalComparator;
import com.example.pam_project.lists.lists.components.comparators.ListCategoryComparator;
import com.example.pam_project.lists.lists.components.comparators.ListDateAddedComparator;
import com.example.pam_project.lists.lists.components.comparators.ListInformationComparator;
import com.example.pam_project.lists.lists.components.comparators.ListTaskNumberComparator;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.AppColor;
import com.example.pam_project.utils.TaskStatus;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class ListInformation implements Serializable, Comparable<ListInformation> {
    private final String title;
    private long id;
    private long categoryId;
    private AppColor color;
    private List<TaskInformation> tasks;
    private CategoryInformation category;

    // based on R.array.sort_by_criteria
    private static final ListInformationComparator NATURAL_COMPARATOR =
            new ListTaskNumberComparator();
    private static final ListInformationComparator[] COMPARATORS = {
            NATURAL_COMPARATOR,
            new ListCategoryComparator(new CategoryAlphabeticalComparator()),
            new ListCategoryComparator(new CategoryPriorityComparator()),
            new ListAlphabeticalComparator(),
            new ListDateAddedComparator()
    };

    public ListInformation(final long id, final String title, final long categoryId) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
    }

    public ListInformation(final long id, final String title, final long categoryId,
                           final AppColor color, final List<TaskInformation> tasks) {
        this(id, title, categoryId);
        this.color = color;
        this.tasks = tasks;
    }

    public ListInformation(final long id, final String title, final long categoryId,
                           final String color, final List<TaskInformation> tasks) {
        this(id, title, categoryId, AppColor.fromName(color), tasks);
    }

    public ListInformation(final long id, final String title, final long categoryId,
                           final String color) {
        this(id, title, categoryId, AppColor.fromName(color), null);
    }

    public ListInformation(final long id, final String title, final long categoryId,
                           final List<TaskInformation> tasks) {
        this(id, title, categoryId, (AppColor) null, tasks);
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

    public CategoryInformation getCategory() {
        return category;
    }

    public void setCategory(final CategoryInformation category) {
        this.category = category;
    }

    public boolean hasUrgentTask() {
        for (TaskInformation task : tasks){
            if (task.getUrgency() && task.getStatus().equals(TaskStatus.PENDING))
                return true;
        }

        return false;
    }

    public int getPendingTaskCount() {
        int completed = 0;

        for (TaskInformation task : tasks) {
            if (task.getStatus().equals(TaskStatus.PENDING))
                completed++;
        }

        return completed;
    }

    public int getUrgentPendingTaskCount() {
        int urgent = 0;
        for (TaskInformation task : tasks) {
            if (task.getUrgency() && task.getStatus().equals(TaskStatus.PENDING))
                urgent++;
        }

        return urgent;
    }

    @Override
    public int compareTo(ListInformation o) {
        return NATURAL_COMPARATOR.compare(this, o);
    }
}