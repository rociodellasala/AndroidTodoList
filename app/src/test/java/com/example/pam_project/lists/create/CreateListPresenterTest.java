package com.example.pam_project.lists.create;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.create.CreateListPresenter;
import com.example.pam_project.features.lists.create.CreateListView;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateListPresenterTest {

    private CategoriesRepository categoriesRepository;
    private ListsRepository listsRepository;
    private CreateListView view;

    private CreateListPresenter presenter;

    @Before
    public void setup() {
        SchedulerProvider provider = new TestSchedulerProvider();
        categoriesRepository = mock(CategoriesRepository.class);
        listsRepository = mock(ListsRepository.class);
        view = mock(CreateListView.class);

        presenter = new CreateListPresenter(provider, categoriesRepository, listsRepository, view);
    }

    @Test
    public void givenAViewIsAttachedWhenEverythingOkThenFetchTheCategories() {
        final CategoryInformation[] categoriesArray = { mock(CategoryInformation.class),
                mock(CategoryInformation.class) };
        final List<CategoryInformation> categories = Arrays.asList(categoriesArray);
        final Flowable<List<CategoryInformation>> categoriesFlowable = Flowable.just(categories);
        doReturn(categoriesFlowable).when(categoriesRepository).getCategories();

        presenter.onViewAttached();

        verify(view).bindCategories(categories);
    }

    @Test
    public void givenAViewIsAttachedWhenErrorThenHandleTheErrors() {
        final Flowable<List<CategoryInformation>> categoriesFlowable = Flowable.fromCallable(
                () -> {throw new Exception("BOOM!");}
        );
        doReturn(categoriesFlowable).when(categoriesRepository).getCategories();

        presenter.onViewAttached();

        verify(view).onCategoriesReceivedError();
    }

    @Test
    public void givenAListIsInsertedWhenEverythingOkThenInsertTheList() {
        final String name = "Name";
        final long categoryId = 2;

        when(listsRepository.insertList(name, categoryId)).thenReturn(Completable.complete());

        presenter.insertList(name, categoryId);

        verify(view, never()).onListInsertedError();
    }

    @Test
    public void givenAListIsInsertedWhenThereIsAnErrorThenHandleTheError() {
        final String name = "Name";
        final long categoryId = 2;

        when(listsRepository.insertList(name, categoryId)).thenReturn(
                Completable.fromAction(() -> {throw new Exception("BOOM!");}));

        presenter.insertList(name, categoryId);

        verify(view).onListInsertedError();
    }
}
