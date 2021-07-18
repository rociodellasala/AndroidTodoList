package com.example.pam_project.tasks.list

import com.example.pam_project.R
import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.features.tasks.list.TaskPresenter
import com.example.pam_project.features.tasks.list.TaskView
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.constants.TaskStatus
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class TaskPresenterTest {
    private var taskRepository: TaskRepository? = null
    private var listsRepository: ListsRepository? = null
    private var view: TaskView? = null
    private var listId: Long = 0
    private var presenter: TaskPresenter? = null
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        taskRepository = Mockito.mock(TaskRepository::class.java)
        listsRepository = Mockito.mock(ListsRepository::class.java)
        view = Mockito.mock(TaskView::class.java)
        listId = 3
        presenter = TaskPresenter(provider, taskRepository!!, listsRepository!!, view!!, listId)
    }

    @Test
    fun givenAViewWasAttachedWhenEverythingIsOkThenFetchTheTasks() {
        val categoryId: Long = 1
        val listTitle = "listTitle"
        val tasks: List<TaskInformation> = ArrayList()
        val li = ListInformation(listId, listTitle, categoryId, tasks)
        val listInformationObservable = Flowable.just(li)
        Mockito.doReturn(listInformationObservable).`when`(listsRepository)!!.getListWithTasks(listId)
        presenter!!.onViewAttached()
        Mockito.verify(view)!!.bindListName(listTitle)
        Mockito.verify(view)!!.bindTasks(tasks)
    }

    @Test
    fun givenAViewWasAttachedWhenItFailsThenHandleError() {
        val listInformationObservable = Flowable.fromCallable<ListInformation> { throw Exception("BOOM!") }
        Mockito.doReturn(listInformationObservable).`when`(listsRepository)!!.getListWithTasks(listId)
        presenter!!.onViewAttached()
        Mockito.verify(view)!!.onTasksReceivedError()
    }

    @Test
    fun givenATaskWasMovedFromPendingToDoneThenMoveTheTask() {
        swapStatusTest(TaskStatus.PENDING, TaskStatus.DONE)
    }

    @Test
    fun givenATaskWasMovedFromDoneToPendingThenMoveTheTask() {
        swapStatusTest(TaskStatus.DONE, TaskStatus.PENDING)
    }

    private fun swapStatusTest(status: TaskStatus, oppositeStatus: TaskStatus) {
        val taskId: Long = 14
        val title = "taskTitle"
        val description = "description"
        val isUrgent = false
        val ti = TaskInformation(taskId, title, description, isUrgent, status)
        val position = 2
        Mockito.`when`(taskRepository!!.updateTask(
                taskId, title, description, isUrgent, oppositeStatus, listId
        )).thenReturn(Completable.complete())
        presenter!!.onTaskChange(ti, position)
        val otherTi = TaskInformation(taskId, title, description, isUrgent,
                oppositeStatus)
        Mockito.verify(view)!!.onTaskStatusEdit(otherTi, position)
    }

    @Test
    fun givenATaskFailsToUpdateThenHandleTheError() {
        val taskId: Long = 14
        val title = "taskTitle"
        val description = "description"
        val isUrgent = false
        val status = TaskStatus.PENDING
        val oppositeStatus = TaskStatus.DONE
        val ti = TaskInformation(taskId, title, description, isUrgent, status)
        val position = 2
        Mockito.`when`(taskRepository!!.updateTask(
                taskId, title, description, isUrgent, oppositeStatus, listId
        )).thenReturn(Completable.fromAction { throw Exception("BOOM!") })
        presenter!!.onTaskChange(ti, position)
        Mockito.verify(view)!!.onTaskUpdatedError()
    }

    @Test
    fun givenATaskWasClickedThenLaunchTheDetailScreen() {
        val id: Long = 2
        presenter!!.onTaskClicked(id)
        Mockito.verify(view)!!.showTaskContent(id)
    }

    @Test
    fun givenAddButtonWasClickedThenLaunchTheAddTaskScreen() {
        presenter!!.onButtonAddClicked()
        Mockito.verify(view)!!.showAddTask()
    }

    @Test
    fun givenTaskEditOptionWasClickedThenLaunchTheEditTaskScreen() {
        presenter!!.onEditList()
        Mockito.verify(view)!!.showEditList()
    }

    @Test
    fun givenNoTasksWereAvailableThenShowNoItemsScreen() {
        presenter!!.onEmptyTask()
        Mockito.verify(view)!!.showEmptyMessage()
    }

    @Test
    fun givenThePresenterIsInstancedThenBindHeaders() {
        val headers = intArrayOf(R.string.pending_tasks, R.string.done_tasks)
        Mockito.verify(view)!!.bindHeaders(headers)
    }
}