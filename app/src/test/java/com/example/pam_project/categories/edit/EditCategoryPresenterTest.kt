package com.example.pam_project.categories.edit

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.categories.edit.EditCategoryPresenter
import com.example.pam_project.features.categories.edit.EditCategoryView
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.utils.constants.AppColor
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class EditCategoryPresenterTest {
    private var repository: CategoriesRepository? = null
    private var view: EditCategoryView? = null
    private var presenter: EditCategoryPresenter? = null
    private var categoryId: Long = 0
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        repository = Mockito.mock(CategoriesRepository::class.java)
        view = Mockito.mock(EditCategoryView::class.java)
        categoryId = 2
        presenter = EditCategoryPresenter(categoryId, provider, repository!!, view!!)
    }

    @Test
    fun givenAViewWasAttachedWhenEverythingIsOkThenFetchTheCategory() {
        val title = "categoryTitle"
        val stringColor = AppColor.BLUE.hexValue
        val categoryInformation = CategoryInformation(categoryId,
                title, stringColor)
        val flowable = Flowable.just(categoryInformation)
        Mockito.`when`(repository!!.getCategory(categoryId)).thenReturn(flowable)
        presenter!!.onViewAttached()
        Mockito.verify(view)!!.bindCategory(categoryInformation)
    }

    @Test
    fun givenAViewWasAttachedWhenThereIsAnErrorThenHandleTheError() {
        val flowable = Flowable.fromCallable<CategoryInformation> { throw Exception("BOOM!") }
        Mockito.`when`(repository!!.getCategory(categoryId)).thenReturn(flowable)
        presenter!!.onViewAttached()
        Mockito.verify(view)!!.onCategoryRetrievedError()
    }

    @Test
    fun givenACategoryIsUpdatedWhenEverythingIsOkThenUpdateTheCategory() {
        val title = "categoryTitle"
        val stringColor = AppColor.BLUE.hexValue
        Mockito.`when`(repository!!.updateCategory(categoryId, title, stringColor))
                .thenReturn(Completable.complete())
        presenter!!.updateCategory(title, stringColor)
        Mockito.verify(view, Mockito.never())!!.onCategoryUpdateError()
    }

    @Test
    fun givenACategoryIsUpdatedWhenAnErrorOccursThenHandleTheError() {
        val title = "categoryTitle"
        val stringColor = AppColor.BLUE.hexValue
        Mockito.`when`(repository!!.updateCategory(categoryId, title, stringColor))
                .thenReturn(Completable.fromAction { throw Exception("BOOM!") })
        presenter!!.updateCategory(title, stringColor)
        Mockito.verify(view)!!.onCategoryUpdateError()
    }

    @Test
    fun givenACategoryIsDeletedWhenEverythingIsOkThenDeleteTheCategory() {
        Mockito.`when`(repository!!.deleteCategory(categoryId))
                .thenReturn(Completable.complete())
        presenter!!.deleteCategory(categoryId)
        Mockito.verify(view)!!.onCategoryDelete()
        Mockito.verify(view, Mockito.never())!!.onCategoryDeletedError()
    }

    @Test
    fun givenACategoryIsDeletedWhenAnErrorOccursThenHandleTheError() {
        Mockito.`when`(repository!!.deleteCategory(categoryId))
                .thenReturn(Completable.fromAction { throw Exception("BOOM!") })
        presenter!!.deleteCategory(categoryId)
        Mockito.verify(view)!!.onCategoryDeletedError()
        Mockito.verify(view, Mockito.never())!!.onCategoryDelete()
    }

    @Test
    fun givenDeleteButtonWasClickedThenShowDeleteDialog() {
        presenter!!.onDeletePressed()
        Mockito.verify(view)!!.showDeleteDialog()
    }
}