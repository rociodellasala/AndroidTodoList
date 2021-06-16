package com.example.pam_project.tasks.edit;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.tasks.edit.EditTaskPresenter;
import com.example.pam_project.features.tasks.edit.EditTaskView;
import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditTaskPresenterTest {
    private TaskRepository taskRepository;
    private EditTaskView view;
    private final long taskId = 32;

    private EditTaskPresenter presenter;

    @Before
    public void setup() {
        SchedulerProvider provider = new TestSchedulerProvider();

        taskRepository = mock(TaskRepository.class);

        view = mock(EditTaskView.class);

        presenter = new EditTaskPresenter(taskId, provider, taskRepository, view);
    }

    @Test
    public void givenATaskIsEditedWhenEverythingIsOkThenEditTheTask() {
        final String title = "taskTitle";
        final String description = "taskDescription";
        final boolean priority = false;

        when(taskRepository.updateTask(taskId, title, description, priority))
                .thenReturn(Completable.complete());

        presenter.updateTask(title, description, priority);

        verify(view, never()).onTaskUpdatedError();
    }

    @Test
    public void givenATaskIsEditedWhenThereIsAnErrorThenHandleTheError() {
        final String title = "taskTitle";
        final String description = "taskDescription";
        final boolean priority = false;

        when(taskRepository.updateTask(taskId, title, description, priority))
                .thenReturn(Completable.fromAction(() -> {throw new Exception("BOOM!");}));

        presenter.updateTask(title, description, priority);

        verify(view).onTaskUpdatedError();
    }

    @Test
    public void givenATaskIsDeletedWhenEverythingIsOkThenDeleteTheTask() {
        when(taskRepository.deleteTask(taskId))
                .thenReturn(Completable.complete());

        presenter.deleteTask(taskId);

        verify(view).onTaskDelete();
    }

    @Test
    public void givenATaskIsDeletedWhenThereIsAnErrorThenHandleTheError() {
        when(taskRepository.deleteTask(taskId))
                .thenReturn(Completable.fromAction(() -> {throw new Exception("BOOM!");}));

        presenter.deleteTask(taskId);

        verify(view).onTaskDeletedError();
    }

    @Test
    public void givenAViewWasAttachedWhenEverythingIsOkThenBindTheTask() {
        final TaskInformation ti = mock(TaskInformation.class);
        final Flowable<TaskInformation> flowable = Flowable.just(ti);
        when(taskRepository.getTask(taskId)).thenReturn(flowable);

        presenter.onViewAttached();

        verify(view).bindTask(ti);
    }

    @Test
    public void givenAViewWasAttachedWhenThereIsAnErrorThenHandleTheError() {
        final Flowable<TaskInformation> flowable = Flowable.fromCallable(
                () -> {throw new Exception("BOOM!");}
        );
        when(taskRepository.getTask(taskId)).thenReturn(flowable);

        presenter.onViewAttached();

        verify(view).onTaskRetrievedError();
    }

    @Test
    public void givenDeleteButtonWasClickedThenShowDeleteDialog() {
        presenter.onDeletePressed();

        verify(view).showDeleteDialog();
    }
}
