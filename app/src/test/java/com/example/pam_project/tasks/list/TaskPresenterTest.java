package com.example.pam_project.tasks.list;

import com.example.pam_project.features.tasks.list.TaskPresenter;
import com.example.pam_project.features.tasks.list.TaskView;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;

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
    public void givenATaskWasClickedThenLaunchTheDetailScreen() {
        final long id = 2;
        presenter.onTaskClicked(id);
        verify(view).showTaskContent(id);
    }
}
