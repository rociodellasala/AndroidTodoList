package com.example.pam_project.tasks.list;

import com.example.pam_project.R;
import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.features.tasks.list.TaskPresenter;
import com.example.pam_project.features.tasks.list.TaskView;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.constants.TaskStatus;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void givenAViewWasAttachedWhenEverythingIsOkThenFetchTheTasks() {
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

    @Test
    public void givenAViewWasAttachedWhenItFailsThenHandleError() {
        Flowable<ListInformation> listInformationObservable = Flowable.fromCallable(
                () -> {throw new Exception("BOOM!");}
        );
        doReturn(listInformationObservable).when(listsRepository).getListWithTasks(listId);

        presenter.onViewAttached();

        verify(view).onTasksReceivedError();
    }

    @Test
    public void givenATaskWasMovedFromPendingToDoneThenMoveTheTask() {
        swapStatusTest(TaskStatus.PENDING, TaskStatus.DONE);
    }

    @Test
    public void givenATaskWasMovedFromDoneToPendingThenMoveTheTask() {
        swapStatusTest(TaskStatus.DONE, TaskStatus.PENDING);
    }

    private void swapStatusTest(TaskStatus status, TaskStatus oppositeStatus) {
        final long taskId = 14;
        final String title = "taskTitle";
        final String description = "description";
        final boolean isUrgent = false;
        TaskInformation ti = new TaskInformation(taskId, title, description, isUrgent, status);

        int position = 2;

        when(taskRepository.updateTask(
                taskId, title, description, isUrgent, oppositeStatus, listId
        )).thenReturn(Completable.complete());

        presenter.onTaskChange(ti, position);

        TaskInformation otherTi = new TaskInformation(taskId, title, description, isUrgent,
                oppositeStatus);
        verify(view).onTaskStatusEdit(otherTi, position);
    }

    @Test
    public void givenATaskFailsToUpdateThenHandleTheError() {
        final long taskId = 14;
        final String title = "taskTitle";
        final String description = "description";
        final boolean isUrgent = false;
        final TaskStatus status = TaskStatus.PENDING;
        final TaskStatus oppositeStatus = TaskStatus.DONE;
        TaskInformation ti = new TaskInformation(taskId, title, description, isUrgent, status);

        int position = 2;

        when(taskRepository.updateTask(
                taskId, title, description, isUrgent, oppositeStatus, listId
        )).thenReturn(Completable.fromAction(() -> {
            throw new Exception("BOOM!");
        }));

        presenter.onTaskChange(ti, position);

        verify(view).onTaskUpdatedError();
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
    public void givenThePresenterIsInstancedThenBindHeaders() {

        int[] headers = {R.string.pending_tasks, R.string.done_tasks};
        verify(view).bindHeaders(headers);
    }
}
