package com.example.pam_project.tasks.create;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.tasks.create.CreateTaskPresenter;
import com.example.pam_project.features.tasks.create.CreateTaskView;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.constants.TaskStatus;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateTaskPresenterTest {
    private TaskRepository taskRepository;
    private CreateTaskView view;

    private CreateTaskPresenter presenter;

    @Before
    public void setup() {
        SchedulerProvider provider = new TestSchedulerProvider();

        taskRepository = mock(TaskRepository.class);

        view = mock(CreateTaskView.class);

        presenter = new CreateTaskPresenter(provider, taskRepository, view);
    }

    @Test
    public void givenATaskThenCreateTheTask() {
        final String title = "taskTitle";
        final String description = "taskDescription";
        final boolean priority = false;
        final TaskStatus status = TaskStatus.PENDING;
        final long listId = 2;

        when(taskRepository.insertTask(title, description, priority, status, listId))
                .thenReturn(Completable.complete());

        presenter.insertTask(title, description, priority, listId);
        verify(view, never()).onTaskInsertedError();
    }

    @Test
    public void givenATaskFailsToCreateThenHandleTheError() {
        final String title = "taskTitle";
        final String description = "taskDescription";
        final boolean priority = false;
        final TaskStatus status = TaskStatus.PENDING;
        final long listId = 2;

        when(taskRepository.insertTask(title, description, priority, status, listId))
                .thenReturn((Completable.fromAction(() -> {
                    throw new Exception("BOOM!");
                })));

        presenter.insertTask(title, description, priority, listId);
        verify(view).onTaskInsertedError();
    }
}
