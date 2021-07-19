package com.example.pam_project.categories.create

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.categories.create.CreateCategoryPresenter
import com.example.pam_project.features.categories.create.CreateCategoryView
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.utils.constants.AppColor
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class CreateCategoryPresenterTest {
    private lateinit var repository: CategoriesRepository
    private lateinit var view: CreateCategoryView
    private lateinit var presenter: CreateCategoryPresenter
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        repository = Mockito.mock(CategoriesRepository::class.java)
        view = Mockito.mock(CreateCategoryView::class.java)
        presenter = CreateCategoryPresenter(provider, repository, view)
    }

    @Test
    fun givenACategoryIsCreatedWhenEverythingIsOkThenCreateTheCategory() {
        val title = "categoryTitle"
        val stringColor = AppColor.BLUE.hexValue
        Mockito.`when`(repository.insertCategory(title, stringColor))
                .thenReturn(Completable.complete())
        presenter.insertCategory(title, stringColor)
        Mockito.verify(view, Mockito.never()).onCategoryInsertedError()
    }

    @Test
    fun givenACategoryIsCreatedWhenAnErrorOccursThenHandleTheError() {
        val title = "categoryTitle"
        val stringColor = AppColor.BLUE.hexValue
        Mockito.`when`(repository.insertCategory(title, stringColor))
                .thenReturn(Completable.fromAction { throw Exception("BOOM!") })
        presenter.insertCategory(title, stringColor)
        Mockito.verify(view).onCategoryInsertedError()
    }
}