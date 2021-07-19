package com.example.pam_project.tasks.edit

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.tasks.edit.EditTaskPresenter
import com.example.pam_project.features.tasks.edit.EditTaskView
import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class EditTaskPresenterTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var view: EditTaskView
    private val taskId: Long = 32
    private lateinit var presenter: EditTaskPresenter
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        taskRepository = Mockito.mock(TaskRepository::class.java)
        view = Mockito.mock(EditTaskView::class.java)
        presenter = EditTaskPresenter(taskId, provider, taskRepository, view)
    }

    @Test
    fun givenATaskIsEditedWhenEverythingIsOkThenEditTheTask() {
        val title = "taskTitle"
        val description = "taskDescription"
        val priority = false
        Mockito.`when`(taskRepository.updateTask(taskId, title, description, priority))
                .thenReturn(Completable.complete())
        presenter.updateTask(title, description, priority)
        Mockito.verify(view, Mockito.never()).onTaskUpdatedError()
    }

    @Test
    fun givenATaskIsEditedWhenThereIsAnErrorThenHandleTheError() {
        val title = "taskTitle"
        val description = "taskDescription"
        val priority = false
        Mockito.`when`(taskRepository.updateTask(taskId, title, description, priority))
                .thenReturn(Completable.fromAction { throw Exception("BOOM!") })
        presenter.updateTask(title, description, priority)
        Mockito.verify(view).onTaskUpdatedError()
    }

    @Test
    fun givenATaskIsDeletedWhenEverythingIsOkThenDeleteTheTask() {
        Mockito.`when`(taskRepository.deleteTask(taskId))
                .thenReturn(Completable.complete())
        presenter.deleteTask(taskId)
        Mockito.verify(view).onTaskDelete()
    }

    @Test
    fun givenATaskIsDeletedWhenThereIsAnErrorThenHandleTheError() {
        Mockito.`when`(taskRepository.deleteTask(taskId))
                .thenReturn(Completable.fromAction { throw Exception("BOOM!") })
        presenter.deleteTask(taskId)
        Mockito.verify(view).onTaskDeletedError()
    }

    @Test
    fun givenAViewWasAttachedWhenEverythingIsOkThenBindTheTask() {
        val ti = Mockito.mock(TaskInformation::class.java)
        val flowable = Flowable.just(ti)
        Mockito.`when`(taskRepository.getTask(taskId)).thenReturn(flowable)
        presenter.onViewAttached()
        Mockito.verify(view).bindTask(ti)
    }

    @Test
    fun givenAViewWasAttachedWhenThereIsAnErrorThenHandleTheError() {
        val flowable = Flowable.fromCallable<TaskInformation> { throw Exception("BOOM!") }
        Mockito.`when`(taskRepository.getTask(taskId)).thenReturn(flowable)
        presenter.onViewAttached()
        Mockito.verify(view).onTaskRetrievedError()
    }

    @Test
    fun givenDeleteButtonWasClickedThenShowDeleteDialog() {
        presenter.onDeletePressed()
        Mockito.verify(view).showDeleteDialog()
    }
}