package com.example.pam_project.tasks.list;

import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.features.tasks.list.TaskPresenter;
import com.example.pam_project.features.tasks.list.TaskView;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.TaskStatus;

import org.junit.Before;
import org.junit.Test;

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
    public void givenTheViewWasAttachedThenShowTheTasks() {
        presenter.onViewAttached();

        verify(view).showTasks();
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

    @Test
    public void givenATaskIsAppendedThenAppendTheTask() {
        final long id = 48;

        presenter.appendTask(id);

        verify(view).bindTask(taskRepository.getTask(id));
    }

    @Test
    public void givenATaskIsChangedWhenTaskWasDoneThenChangeTheTask() {
        final int position = 3;
        final long id = 49;
        final String name = "Name";
        final String description = "Description";
        final boolean priority = true;
        final TaskStatus status = TaskStatus.DONE;
        final TaskInformation ti = new TaskInformation(id, name, description, priority, status);

        presenter.onTaskChange(ti, position);

        verify(taskRepository).updateTask(id, name, description, priority, status, id);
        verify(view).onSuccessfulUpdate(ti, position);
    }

    @Test
    public void givenATaskIsChangedWhenTaskWasPendingThenChangeTheTask() {
        final int position = 3;
        final long id = 49;
        final String name = "Name";
        final String description = "Description";
        final boolean priority = true;
        final TaskStatus status = TaskStatus.PENDING;
        final TaskInformation ti = new TaskInformation(id, name, description, priority, status);

        presenter.onTaskChange(ti, position);

        verify(taskRepository).updateTask(id, name, description, priority, status, id);
        verify(view).onSuccessfulUpdate(ti, position);
    }
}
