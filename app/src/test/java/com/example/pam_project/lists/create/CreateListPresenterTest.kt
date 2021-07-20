package com.example.pam_project.lists.create

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.create.CreateListPresenter
import com.example.pam_project.features.lists.create.CreateListView
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class CreateListPresenterTest {
    private lateinit var categoriesRepository: CategoriesRepository
    private lateinit var listsRepository: ListsRepository
    private lateinit var view: CreateListView
    private lateinit var presenter: CreateListPresenter
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        categoriesRepository = Mockito.mock(CategoriesRepository::class.java)
        listsRepository = Mockito.mock(ListsRepository::class.java)
        view = Mockito.mock(CreateListView::class.java)
        presenter = CreateListPresenter(provider, categoriesRepository, listsRepository, view)
    }

    @Test
    fun givenAViewIsAttachedWhenEverythingOkThenFetchTheCategories() {
        val categoriesArray = arrayOf(Mockito.mock(CategoryInformation::class.java),
                Mockito.mock(CategoryInformation::class.java))
        val categories = listOf(*categoriesArray)
        val categoriesFlowable = Flowable.just(categories)
        Mockito.doReturn(categoriesFlowable).`when`(categoriesRepository).categories
        presenter.onViewAttached()
        Mockito.verify(view).bindCategories(categories)
    }

    @Test
    fun givenAViewIsAttachedWhenErrorThenHandleTheErrors() {
        val categoriesFlowable = Flowable.fromCallable<List<CategoryInformation>> { throw Exception("BOOM!") }
        Mockito.doReturn(categoriesFlowable).`when`(categoriesRepository).categories
        presenter.onViewAttached()
        Mockito.verify(view).onCategoriesReceivedError()
    }

    @Test
    fun givenAListIsInsertedWhenEverythingOkThenInsertTheList() {
        val name = "Name"
        val categoryId: Long = 2
        Mockito.`when`(listsRepository.insertList(name, categoryId)).thenReturn(Completable.complete())
        presenter.insertList(name, categoryId)
        Mockito.verify(view, Mockito.never()).onListInsertedError()
    }

    @Test
    fun givenAListIsInsertedWhenThereIsAnErrorThenHandleTheError() {
        val name = "Name"
        val categoryId: Long = 2
        Mockito.`when`(listsRepository.insertList(name, categoryId)).thenReturn(
                Completable.fromAction { throw Exception("BOOM!") })
        presenter.insertList(name, categoryId)
        Mockito.verify(view).onListInsertedError()
    }
}