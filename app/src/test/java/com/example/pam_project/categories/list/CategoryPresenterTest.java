package com.example.pam_project.categories.list;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.categories.list.CategoryPresenter;
import com.example.pam_project.features.categories.list.CategoryView;
import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.constants.AppColor;
import com.example.pam_project.utils.constants.TaskStatus;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CategoryPresenterTest {

    private SchedulerProvider provider;
    private CategoriesRepository repository;
    private CategoryView view;
    private CategoryPresenter presenter;

    @Before
    public void setup() {
        provider = new TestSchedulerProvider();

        repository = mock(CategoriesRepository.class);

        view = mock(CategoryView.class);

        presenter = new CategoryPresenter(provider, repository, view);
    }

    @Test
    public void givenAViewWasAttachedThenFetchTheCategories(){
        final List<CategoryInformation> categories = new ArrayList<>();

        Flowable<List<CategoryInformation>> categoryInformationObservable = Flowable.just(categories);
        doReturn(categoryInformationObservable).when(repository).getCategories();

        presenter.onViewAttached();
        verify(view).showCategories();
        verify(view).bindCategories(categories);
    }

    @Test
    public void givenAViewWasAttachedWhenItFailsThenHandleError(){
        Flowable<CategoryInformation> categoryInformationObservable = Flowable.fromCallable(
                () -> {throw new Exception("BOOM!");}
        );
        doReturn(categoryInformationObservable).when(repository).getCategories();

        presenter.onViewAttached();

        verify(view).onCategoriesReceivedError();
    }

    @Test
    public void givenACategoryWasClickedThenLaunchTheDetailScreen() {
        final long id = 2;

        presenter.onCategoryClicked(id);

        verify(view).showCategoryForm(id);
    }

    @Test
    public void givenNoCategoriesWereAvailableThenShowNoItemsScreen() {
        presenter.onEmptyCategory();

        verify(view).showEmptyMessage();
    }

    @Test
    public void givenAddButtonWasClickedThenLaunchTheAddCategoryScreen() {
        presenter.onButtonAddClicked();

        verify(view).showAddCategory();
    }

    @Test
    public void givenTwoCategoriesThenMoveSwapThem() {
        int draggedPosition = 0;
        int targetPosition = 1;

        presenter.swapCategories(draggedPosition, targetPosition);

        verify(view).onCategoriesSwap(draggedPosition, targetPosition);
    }

}
