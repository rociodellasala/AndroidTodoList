package com.example.pam_project.categories.edit;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.categories.edit.EditCategoryPresenter;
import com.example.pam_project.features.categories.edit.EditCategoryView;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.constants.AppColor;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditCategoryPresenterTest {

    private CategoriesRepository repository;
    private EditCategoryView view;
    private EditCategoryPresenter presenter;
    private long categoryId;

    @Before
    public void setup() {
        SchedulerProvider provider = new TestSchedulerProvider();

        repository = mock(CategoriesRepository.class);

        view = mock(EditCategoryView.class);

        categoryId = 2;

        presenter = new EditCategoryPresenter(categoryId, provider, repository, view);
    }

    @Test
    public void givenAViewWasAttachedWhenEverythingIsOkThenFetchTheCategory(){
        final String title = "categoryTitle";
        final String stringColor = AppColor.BLUE.getHexValue();

        final CategoryInformation categoryInformation = new CategoryInformation(categoryId,
                title, stringColor);
        final Flowable<CategoryInformation> flowable = Flowable.just(categoryInformation);

        when(repository.getCategory(categoryId)).thenReturn(flowable);

        presenter.onViewAttached();

        verify(view).bindCategory(categoryInformation);
    }

    @Test
    public void givenAViewWasAttachedWhenThereIsAnErrorThenHandleTheError(){
        final Flowable<CategoryInformation> flowable = Flowable.fromCallable(
                () -> {throw new Exception("BOOM!");}
        );
        when(repository.getCategory(categoryId)).thenReturn(flowable);

        presenter.onViewAttached();

        verify(view).onCategoryRetrievedError();
    }

    @Test
    public void givenACategoryIsUpdatedWhenEverythingIsOkThenUpdateTheCategory(){
        final String title = "categoryTitle";
        final String stringColor = AppColor.BLUE.getHexValue();

        when(repository.updateCategory(categoryId, title, stringColor))
                .thenReturn(Completable.complete());

        presenter.updateCategory(title, stringColor);
        verify(view, never()).onCategoryUpdateError();
    }

    @Test
    public void givenACategoryIsUpdatedWhenAnErrorOccursThenHandleTheError(){
        final String title = "categoryTitle";
        final String stringColor = AppColor.BLUE.getHexValue();

        when(repository.updateCategory(categoryId, title, stringColor))
                .thenReturn((Completable.fromAction(() -> {
                    throw new Exception("BOOM!");
                })));

        presenter.updateCategory(title, stringColor);
        verify(view).onCategoryUpdateError();
    }

    @Test
    public void givenACategoryIsDeletedWhenEverythingIsOkThenDeleteTheCategory(){
        when(repository.deleteCategory(categoryId))
                .thenReturn(Completable.complete());

        presenter.deleteCategory(categoryId);
        verify(view).onCategoryDelete();
        verify(view, never()).onCategoryDeletedError();
    }

    @Test
    public void givenACategoryIsDeletedWhenAnErrorOccursThenHandleTheError(){
        when(repository.deleteCategory(categoryId))
                .thenReturn((Completable.fromAction(() -> {
                    throw new Exception("BOOM!");
                })));

        presenter.deleteCategory(categoryId);
        verify(view).onCategoryDeletedError();
        verify(view, never()).onCategoryDelete();
    }

    @Test
    public void givenDeleteButtonWasClickedThenShowDeleteDialog() {
        presenter.onDeletePressed();

        verify(view).showDeleteDialog();
    }
}
