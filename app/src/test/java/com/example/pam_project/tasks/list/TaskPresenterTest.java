package com.example.pam_project.tasks.list;

import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.features.tasks.list.TaskPresenter;
import com.example.pam_project.features.tasks.list.TaskView;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.TaskStatus;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TaskPresenterTest {

    private TaskRepository taskRepository;
    private ListsRepository listsRepository;
    private TaskView view;
    private long listId;

    private TaskPresenter presenter;

    @Before
    public void setup() {
        taskRepository = mock(TaskRepository.class);
        listsRepository = mock(ListsRepository.class);
        view = mock(TaskView.class);
        listId = 3;

        presenter = new TaskPresenter(taskRepository, listsRepository, view, listId);
    }

    @Test
    public void givenAViewWasAttachedThenFetchTheTasks() {
        final long categoryId = 1;
        final String listTitle = "listTitle";
        final List<TaskInformation> tasks = new ArrayList<>();
        ListInformation li = new ListInformation(listId, listTitle, categoryId, tasks);

        presenter.onViewAttached();

        verify(view).bindListName(listTitle);
        verify(view).bindTasks(tasks);
    }

    @Test
    public void givenATaskWasClickedThenLaunchTheDetailScreen() {
        final long id = 2;

        presenter.onTaskClicked(id);

        verify(view).showTaskContent(id);
    }

    @Test
    public void givenAddButtonWasClickedThenLaunchTheAddTaskScreen() {
        presenter.onButtonAddClicked();

        verify(view).showAddTask();
    }

    @Test
    public void givenTaskEditOptionWasClickedThenLaunchTheEditTaskScreen() {
        presenter.onEditList();

        verify(view).showEditList();
    }

    @Test
    public void givenNoTasksWereAvailableThenShowNoItemsScreen() {
        presenter.onEmptyTask();

        verify(view).showEmptyMessage();
    }
}
