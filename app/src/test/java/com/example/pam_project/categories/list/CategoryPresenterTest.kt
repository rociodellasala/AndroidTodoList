package com.example.pam_project.categories.list

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.categories.list.CategoryPresenter
import com.example.pam_project.features.categories.list.CategoryView
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class CategoryPresenterTest {
    private lateinit var repository: CategoriesRepository
    private lateinit var view: CategoryView
    private lateinit var presenter: CategoryPresenter
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        repository = Mockito.mock(CategoriesRepository::class.java)
        view = Mockito.mock(CategoryView::class.java)
        presenter = CategoryPresenter(provider, repository, view)
    }

    @Test
    fun givenAViewWasAttachedWhenEverythingIsOkThenFetchTheCategories() {
        val categories: List<CategoryInformation?> = ArrayList()
        val categoryInformationObservable = Flowable.just(categories)
        Mockito.doReturn(categoryInformationObservable).`when`(repository).categories
        presenter.onViewAttached()
        Mockito.verify(view).bindCategories(categories)
    }

    @Test
    fun givenAViewWasAttachedWhenAnErrorOccursThenHandleError() {
        val categoryInformationObservable = Flowable.fromCallable<CategoryInformation> { throw Exception("BOOM!") }
        Mockito.doReturn(categoryInformationObservable).`when`(repository).categories
        presenter.onViewAttached()
        Mockito.verify(view).onCategoriesReceivedError()
    }

    @Test
    fun givenACategoryWasClickedThenLaunchTheDetailScreen() {
        val id: Long = 2
        presenter.onCategoryClicked(id)
        Mockito.verify(view).showCategoryForm(id)
    }

    @Test
    fun givenNoCategoriesWereAvailableThenShowNoItemsScreen() {
        presenter.onEmptyCategory()
        Mockito.verify(view).showEmptyMessage()
    }

    @Test
    fun givenAddButtonWasClickedThenLaunchTheAddCategoryScreen() {
        presenter.onButtonAddClicked()
        Mockito.verify(view).showAddCategory()
    }

    @Test
    fun givenTwoCategoriesThenMoveSwapThem() {
        val draggedPosition = 0
        val targetPosition = 1
        presenter.swapCategories(draggedPosition, targetPosition)
        Mockito.verify(view).onCategoriesSwap(draggedPosition, targetPosition)
    }
}