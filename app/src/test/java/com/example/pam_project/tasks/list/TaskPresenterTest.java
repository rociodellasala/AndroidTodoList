package com.example.pam_project.tasks.list;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.features.tasks.list.TaskPresenter;
import com.example.pam_project.features.tasks.list.TaskView;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TaskPresenterTest {

    private SchedulerProvider provider;
    private TaskRepository taskRepository;
    private ListsRepository listsRepository;
    private TaskView view;
    private long listId;

    private TaskPresenter presenter;

    @Before
    public void setup() {
        provider = new TestSchedulerProvider();

        taskRepository = mock(TaskRepository.class);
        listsRepository = mock(ListsRepository.class);

        view = mock(TaskView.class);
        listId = 3;

        presenter = new TaskPresenter(provider, taskRepository, listsRepository, view, listId);
    }

    @Test
    public void givenAViewWasAttachedThenFetchTheTasks() {
        final long categoryId = 1;
        final String listTitle = "listTitle";
        final List<TaskInformation> tasks = new ArrayList<>();
        ListInformation li = new ListInformation(listId, listTitle, categoryId, tasks);

        Flowable<ListInformation> listInformationObservable = Flowable.just(li);
        doReturn(listInformationObservable).when(listsRepository).getListWithTasks(listId);

        presenter.onViewAttached();

        verify(view).bindListName(listTitle);
        verify(view).bindTasks(tasks);
    }

    /*@Test
    public void givenATaskWasUpdatedThenUpdateTheTask() {
        final long taskId = 14;
        final String title = "taskTitle";
        final String description = "description";
        final boolean isUrgent = false;
        final TaskStatus status = TaskStatus.PENDING;
        TaskInformation ti = new TaskInformation(taskId, title, description, isUrgent, status);

        int position = 2;

        doReturn(Completable.fromAction(() -> {int a = 1;})).when(taskRepository).updateTask(
                taskId, title, description, isUrgent, status, listId
        );

        verify(view).onTaskStatusEdit(ti, position);
    }*/

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
