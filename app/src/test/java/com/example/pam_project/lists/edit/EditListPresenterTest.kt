package com.example.pam_project.lists.edit

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.edit.EditListPresenter
import com.example.pam_project.features.lists.edit.EditListView
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class EditListPresenterTest {
    private lateinit var categoriesRepository: CategoriesRepository
    private lateinit var listsRepository: ListsRepository
    private lateinit var view: EditListView
    private lateinit var presenter: EditListPresenter
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        categoriesRepository = Mockito.mock(CategoriesRepository::class.java)
        listsRepository = Mockito.mock(ListsRepository::class.java)
        view = Mockito.mock(EditListView::class.java)
        presenter = EditListPresenter(provider, categoriesRepository, listsRepository, view)
    }

    @Test
    fun givenAViewWasAttachedWhenEverythingOkThenFetchTheCategories() {
        val id: Long = 3
        val categoriesArray = arrayOf(Mockito.mock(CategoryInformation::class.java),
                Mockito.mock(CategoryInformation::class.java))
        val categories = listOf(*categoriesArray)
        val categoriesFlowable = Flowable.just(categories)
        Mockito.doReturn(categoriesFlowable).`when`(categoriesRepository).categories
        val li = Mockito.mock(ListInformation::class.java)
        val flowable = Flowable.just(li)
        Mockito.`when`(listsRepository.getList(id)).thenReturn(flowable)
        presenter.onViewAttached(id)
        Mockito.verify(view).bindCategories(categories)
    }

    @Test
    fun givenAViewWasAttachedWhenEverythingOkThenFetchTheList() {
        val id: Long = 3
        val categoriesArray = arrayOf(Mockito.mock(CategoryInformation::class.java),
                Mockito.mock(CategoryInformation::class.java))
        val categories = Arrays.asList(*categoriesArray)
        val categoriesFlowable = Flowable.just(categories)
        Mockito.doReturn(categoriesFlowable).`when`(categoriesRepository).categories
        val li = Mockito.mock(ListInformation::class.java)
        val flowable = Flowable.just(li)
        Mockito.`when`(listsRepository.getList(id)).thenReturn(flowable)
        presenter.onViewAttached(id)
        Mockito.verify(view).bindCategories(categories)
        Mockito.verify(view).bindList(li)
    }

    @Test
    fun givenAViewWasAttachedWhenCategoriesFetchFailsThenHandleTheError() {
        val id: Long = 3
        Mockito.doReturn(
                Flowable.fromCallable<Any> { throw Exception("BOOM!") }
        ).`when`(categoriesRepository).categories
        presenter.onViewAttached(id)
        Mockito.verify(view).onCategoriesReceivedError()
    }

    @Test
    fun givenAViewWasAttachedWhenListFetchFailsThenHandleTheError() {
        val id: Long = 3
        val categoriesArray = arrayOf(Mockito.mock(CategoryInformation::class.java),
                Mockito.mock(CategoryInformation::class.java))
        val categories = listOf(*categoriesArray)
        val categoriesFlowable = Flowable.just(categories)
        Mockito.doReturn(categoriesFlowable).`when`(categoriesRepository).categories
        val listFlowable = Flowable
                .fromCallable<List<ListInformation>> { throw Exception("BOOM!") }
        Mockito.doReturn(listFlowable).`when`(listsRepository).getList(id)
        presenter.onViewAttached(id)
        Mockito.verify(view).bindCategories(categories)
        Mockito.verify(view).onListReceivedError()
    }

    @Test
    fun givenAListWasUpdatedWhenEverythingOkThenUpdateTheList() {
        val id: Long = 3
        val categoryId: Long = 2
        val name = "Name"
        Mockito.`when`(listsRepository.updateList(id, name, categoryId)).thenReturn(Completable.complete())
        presenter.updateList(id, name, categoryId)
        Mockito.verify(view, Mockito.never()).onListUpdatedError()
    }

    @Test
    fun givenAListWasUpdatedWhenThereIsAnErrorThenHandleTheError() {
        val id: Long = 3
        val categoryId: Long = 2
        val name = "Name"
        Mockito.`when`(listsRepository.updateList(id, name, categoryId)).thenReturn(
                Completable.fromAction { throw Exception("BOOM!") }
        )
        presenter.updateList(id, name, categoryId)
        Mockito.verify(view).onListUpdatedError()
    }

    @Test
    fun givenAListWasDeletedWhenEverythingOkThenDeleteTheList() {
        val id: Long = 4
        Mockito.`when`(listsRepository.deleteList(id)).thenReturn(Completable.complete())
        presenter.deleteList(id)
        Mockito.verify(view).onListDelete()
    }

    @Test
    fun givenAListWasDeletedWhenThereIsAnErrorThenHandleTheError() {
        val id: Long = 4
        Mockito.`when`(listsRepository.deleteList(id)).thenReturn(
                Completable.fromAction { throw Exception("BOOM!") }
        )
        presenter.deleteList(id)
        Mockito.verify(view).onListDeletedError()
    }

    @Test
    fun givenDeleteButtonWasClickedThenShowDeleteDialog() {
        presenter.onDeletePressed()
        Mockito.verify(view).showDeleteDialog()
    }
}