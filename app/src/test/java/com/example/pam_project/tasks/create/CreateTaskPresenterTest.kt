package com.example.pam_project.tasks.create

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.tasks.create.CreateTaskPresenter
import com.example.pam_project.features.tasks.create.CreateTaskView
import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.constants.TaskStatus
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class CreateTaskPresenterTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var view: CreateTaskView
    private lateinit var presenter: CreateTaskPresenter
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        taskRepository = Mockito.mock(TaskRepository::class.java)
        view = Mockito.mock(CreateTaskView::class.java)
        presenter = CreateTaskPresenter(provider, taskRepository, view)
    }

    @Test
    fun givenATaskThenCreateTheTask() {
        val title = "taskTitle"
        val description = "taskDescription"
        val priority = false
        val status = TaskStatus.PENDING
        val listId: Long = 2
        Mockito.`when`(taskRepository.insertTask(title, description, priority, status, listId))
                .thenReturn(Completable.complete())
        presenter.insertTask(title, description, priority, listId)
        Mockito.verify(view, Mockito.never()).onTaskInsertedError()
    }

    @Test
    fun givenATaskFailsToCreateThenHandleTheError() {
        val title = "taskTitle"
        val description = "taskDescription"
        val priority = false
        val status = TaskStatus.PENDING
        val listId: Long = 2
        Mockito.`when`(taskRepository.insertTask(title, description, priority, status, listId))
                .thenReturn(Completable.fromAction { throw Exception("BOOM!") })
        presenter.insertTask(title, description, priority, listId)
        Mockito.verify(view).onTaskInsertedError()
    }
}