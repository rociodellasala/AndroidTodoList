package com.example.pam_project.lists.list;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.features.lists.list.ListPresenter;
import com.example.pam_project.features.lists.list.ListView;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.constants.AppColor;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListPresenterTest {

    private ListView view;
    private FtuStorage storage;
    private CategoriesRepository categoriesRepository;

    private ListPresenter presenter;

    @Before
    public void setup() {
        SchedulerProvider provider = new TestSchedulerProvider();
        storage = mock(FtuStorage.class);
        categoriesRepository = mock(CategoriesRepository.class);

        view = mock(ListView.class);

        presenter = new ListPresenter(storage, provider, categoriesRepository, view);
    }

    @Test
    public void givenTheViewWasAttachedWhenTheFtuIsActiveThenLaunchTheFtu() {
        when(storage.isActive()).thenReturn(true);

        presenter.onViewAttached();

        verify(view).launchFtu();
    }

    @Test
    public void givenTheViewWasAttachedWhenTheFtuIsNotActiveThenFetchLists() {
        when(storage.isActive()).thenReturn(false);

        final CategoryInformation[] categoriesArray = { mock(CategoryInformation.class),
                mock(CategoryInformation.class) };
        final List<CategoryInformation> categories = Arrays.asList(categoriesArray);
        final Flowable<List<CategoryInformation>> categoriesFlowable = Flowable.just(categories);
        doReturn(categoriesFlowable).when(categoriesRepository).getCategories();

        final ListInformation[] listsArray = {
            new ListInformation(1, "name1", 2, AppColor.BLUE, new ArrayList<>()),
            new ListInformation(2, "name2", 2, AppColor.GREEN, new ArrayList<>())
        };
        final Map<CategoryInformation, List<ListInformation>> lists = new HashMap<>();
        final List<ListInformation> l1 = new ArrayList<>();
        l1.add(listsArray[0]);
        final List<ListInformation> l2 = new ArrayList<>();
        l2.add(listsArray[1]);
        lists.put(categoriesArray[0], l1);
        lists.put(categoriesArray[1], l2);
        final Flowable<Map<CategoryInformation, List<ListInformation>>> listFlowable = Flowable.just(lists);
        doReturn(listFlowable).when(categoriesRepository).getCategoriesWithLists();

        presenter.onViewAttached();

        verify(view).bindCategories(categories);
        verify(view).bindLists(any());
    }

    @Test
    public void givenTheViewWasAttachedWhenErrorFetchingItemsThenHandleError() {
        when(storage.isActive()).thenReturn(false);

        final Flowable<List<CategoryInformation>> categoriesFlowable = Flowable.fromCallable(
                () -> {throw new Exception("BOOM!");}
        );
        doReturn(categoriesFlowable).when(categoriesRepository).getCategories();
        final Flowable<Map<CategoryInformation, List<CategoryInformation>>> listsFlowable = Flowable.fromCallable(
                () -> {throw new Exception("BOOM 2!");}
        );
        doReturn(listsFlowable).when(categoriesRepository).getCategoriesWithLists();

        presenter.onViewAttached();

        verify(view).onCategoriesReceivedError();
        verify(view).onListsReceivedError();
    }

    @Test
    public void givenASearchIsPerformedThenPerformTheSearch() {
        final String searchQuery = "Hello world!";
        presenter.performSearch(searchQuery);

        verify(view).bindSearchedLists(searchQuery);
    }

    @Test
    public void givenTheSearchIsDetachedThenDetachTheSearch() {
        presenter.onSearchDetached();

        verify(view).unbindSearchedLists();
    }

    @Test
    public void givenTheListIsClickedThenShowTheListContent() {
        final long id = 1;
        presenter.onListClicked(id);

        verify(view).showListContent(id);
    }

    @Test
    public void givenTheButtonIsClickedThenShowListAdd() {
        presenter.onButtonClicked();

        verify(view).showAddList();
    }

    @Test
    public void givenTheFilterButtonIsClickedThenOpenFilterDialog() {
        presenter.onFilterDialog();

        verify(view).showFilterDialog();
    }

    @Test
    public void givenTheSortButtonIsClickedThenOpenSortDialog() {
        presenter.onSortByDialog();

        verify(view).showSortByDialog();
    }

    @Test
    public void givenTheListIsEmptyThenShowEmptyMessage() {
        presenter.onEmptyList();

        verify(view).showEmptyMessage();
    }

    @Test
    public void givenTheCategoriesAreManagedThenShowManager() {
        presenter.onManageCategories();

        verify(view).showManageCategories();
    }

    @Test
    public void givenOnAboutButtonWasClickedThenOnAboutActivity() {
        presenter.onAboutSection();

        verify(view).showAboutSection();
    }
}
