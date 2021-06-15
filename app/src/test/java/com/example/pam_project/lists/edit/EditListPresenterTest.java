package com.example.pam_project.lists.edit;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.edit.EditListPresenter;
import com.example.pam_project.features.lists.edit.EditListView;
import com.example.pam_project.features.lists.list.ListInformation;
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

public class EditListPresenterTest {

    private CategoriesRepository categoriesRepository;
    private ListsRepository listsRepository;
    private EditListView view;

    private EditListPresenter presenter;

    @Before
    public void setup() {
        SchedulerProvider provider = new TestSchedulerProvider();
        categoriesRepository = mock(CategoriesRepository.class);
        listsRepository = mock(ListsRepository.class);
        view = mock(EditListView.class);

        presenter = new EditListPresenter(provider, categoriesRepository, listsRepository, view);
    }

    @Test
    public void givenAViewWasAttachedWhenEverythingOkThenFetchTheCategories() {
        final long id = 3;

        final CategoryInformation[] categoriesArray = { mock(CategoryInformation.class),
                mock(CategoryInformation.class) };
        final List<CategoryInformation> categories = Arrays.asList(categoriesArray);
        final Flowable<List<CategoryInformation>> categoriesFlowable = Flowable.just(categories);
        doReturn(categoriesFlowable).when(categoriesRepository).getCategories();

        final ListInformation li = mock(ListInformation.class);
        when(listsRepository.getList(id)).thenReturn(li);

        presenter.onViewAttached(id);

        verify(view).bindCategories(categories);
        verify(view).bindList(li);
    }

    @Test
    public void givenAViewWasAttachedWhenAnErrorOccursThenHandleTheError() {
        final long id = 3;

        final Flowable<List<CategoryInformation>> categoriesFlowable = Flowable
                .fromCallable(() -> {throw new Exception("BOOM!");});
        doReturn(categoriesFlowable).when(categoriesRepository).getCategories();

        presenter.onViewAttached(id);

        verify(view).onCategoriesReceivedError();
    }

    @Test
    public void givenAListWasUpdatedWhenEverythingOkThenUpdateTheList() {
        final long id = 3;
        final long categoryId = 2;
        final String name = "Name";

        when(listsRepository.updateList(id, name, categoryId)).thenReturn(Completable.complete());

        presenter.updateList(id, name, categoryId);

        verify(view, never()).onListUpdatedError();
    }

    @Test
    public void givenAListWasUpdatedWhenThereIsAnErrorThenHandleTheError() {
        final long id = 3;
        final long categoryId = 2;
        final String name = "Name";

        when(listsRepository.updateList(id, name, categoryId)).thenReturn(
            Completable.fromAction(() -> {throw new Exception("BOOM!");})
        );

        presenter.updateList(id, name, categoryId);

        verify(view).onListUpdatedError();
    }

    @Test
    public void givenAListWasDeletedWhenEverythingOkThenDeleteTheList() {
        final long id = 4;
        when(listsRepository.deleteList(id)).thenReturn(Completable.complete());

        presenter.deleteList(id);

        verify(view).onListDelete();
    }

    @Test
    public void givenAListWasDeletedWhenThereIsAnErrorThenHandleTheError() {
        final long id = 4;
        when(listsRepository.deleteList(id)).thenReturn(
            Completable.fromAction(() -> {throw new Exception("BOOM!");})
        );

        presenter.deleteList(id);

        verify(view).onListDeletedError();
    }
}
