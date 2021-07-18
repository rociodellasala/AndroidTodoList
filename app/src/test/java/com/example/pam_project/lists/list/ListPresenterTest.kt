package com.example.pam_project.lists.list

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.features.lists.list.ListPresenter
import com.example.pam_project.features.lists.list.ListView
import com.example.pam_project.landing.FtuStorage
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.utils.constants.AppColor
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.util.*

class ListPresenterTest {
    private var view: ListView? = null
    private var storage: FtuStorage? = null
    private var categoriesRepository: CategoriesRepository? = null
    private var presenter: ListPresenter? = null
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        storage = Mockito.mock(FtuStorage::class.java)
        categoriesRepository = Mockito.mock(CategoriesRepository::class.java)
        view = Mockito.mock(ListView::class.java)
        presenter = ListPresenter(storage!!, provider, categoriesRepository!!, view)
    }

    @Test
    fun givenTheViewWasAttachedWhenTheFtuIsActiveThenLaunchTheFtu() {
        Mockito.`when`(storage!!.isActive).thenReturn(true)
        presenter!!.onViewAttached()
        Mockito.verify(view)!!.launchFtu()
    }

    @Test
    fun givenTheViewWasAttachedWhenTheFtuIsNotActiveThenFetchLists() {
        Mockito.`when`(storage!!.isActive).thenReturn(false)
        val categoriesArray = arrayOf(Mockito.mock(CategoryInformation::class.java),
                Mockito.mock(CategoryInformation::class.java))
        val categories = listOf(*categoriesArray)
        val categoriesFlowable = Flowable.just(categories)
        Mockito.doReturn(categoriesFlowable).`when`(categoriesRepository)!!.categories
        val listsArray = arrayOf(
                ListInformation(1, "name1", 2, AppColor.BLUE, ArrayList()),
                ListInformation(2, "name2", 2, AppColor.GREEN, ArrayList())
        )
        val lists: MutableMap<CategoryInformation, List<ListInformation>> = HashMap()
        val l1: MutableList<ListInformation> = ArrayList()
        l1.add(listsArray[0])
        val l2: MutableList<ListInformation> = ArrayList()
        l2.add(listsArray[1])
        lists[categoriesArray[0]] = l1
        lists[categoriesArray[1]] = l2
        val listFlowable = Flowable.just<Map<CategoryInformation, List<ListInformation>>>(lists)
        Mockito.doReturn(listFlowable).`when`(categoriesRepository)!!.categoriesWithLists
        presenter!!.onViewAttached()
        Mockito.verify(view)!!.bindCategories(categories)
        Mockito.verify(view)!!.bindLists(ArgumentMatchers.any())
    }

    @Test
    fun givenTheViewWasAttachedWhenErrorFetchingItemsThenHandleError() {
        Mockito.`when`(storage!!.isActive).thenReturn(false)
        val categoriesFlowable = Flowable.fromCallable<List<CategoryInformation>> { throw Exception("BOOM!") }
        Mockito.doReturn(categoriesFlowable).`when`(categoriesRepository)!!.categories
        val listsFlowable = Flowable.fromCallable<Map<CategoryInformation, List<CategoryInformation>>> { throw Exception("BOOM 2!") }
        Mockito.doReturn(listsFlowable).`when`(categoriesRepository)!!.categoriesWithLists
        presenter!!.onViewAttached()
        Mockito.verify(view)!!.onCategoriesReceivedError()
        Mockito.verify(view)!!.onListsReceivedError()
    }

    @Test
    fun givenASearchIsPerformedThenPerformTheSearch() {
        val searchQuery = "Hello world!"
        presenter!!.performSearch(searchQuery)
        Mockito.verify(view)!!.bindSearchedLists(searchQuery)
    }

    @Test
    fun givenTheSearchIsDetachedThenDetachTheSearch() {
        presenter!!.onSearchDetached()
        Mockito.verify(view)!!.unbindSearchedLists()
    }

    @Test
    fun givenTheListIsClickedThenShowTheListContent() {
        val id: Long = 1
        presenter!!.onListClicked(id)
        Mockito.verify(view)!!.showListContent(id)
    }

    @Test
    fun givenTheButtonIsClickedThenShowListAdd() {
        presenter!!.onButtonClicked()
        Mockito.verify(view)!!.showAddList()
    }

    @Test
    fun givenTheFilterButtonIsClickedThenOpenFilterDialog() {
        presenter!!.onFilterDialog()
        Mockito.verify(view)!!.showFilterDialog()
    }

    @Test
    fun givenTheSortButtonIsClickedThenOpenSortDialog() {
        presenter!!.onSortByDialog()
        Mockito.verify(view)!!.showSortByDialog()
    }

    @Test
    fun givenTheListIsEmptyThenShowEmptyMessage() {
        presenter!!.onEmptyList()
        Mockito.verify(view)!!.showEmptyMessage()
    }

    @Test
    fun givenTheCategoriesAreManagedThenShowManager() {
        presenter!!.onManageCategories()
        Mockito.verify(view)!!.showManageCategories()
    }

    @Test
    fun givenOnAboutButtonWasClickedThenOnAboutActivity() {
        presenter!!.onAboutSection()
        Mockito.verify(view)!!.showAboutSection()
    }
}