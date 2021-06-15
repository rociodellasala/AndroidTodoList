package com.example.pam_project.categories.edit;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.categories.create.CreateCategoryPresenter;
import com.example.pam_project.features.categories.create.CreateCategoryView;
import com.example.pam_project.features.categories.edit.EditCategoryPresenter;
import com.example.pam_project.features.categories.edit.EditCategoryView;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.constants.AppColor;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;

public class EditCategoryPresenterTest {

    private SchedulerProvider provider;
    private CategoriesRepository repository;
    private EditCategoryView view;
    private EditCategoryPresenter presenter;
    private long categoryId;

    @Before
    public void setup() {
        provider = new TestSchedulerProvider();

        repository = mock(CategoriesRepository.class);

        view = mock(EditCategoryView.class);

        presenter = new EditCategoryPresenter(categoryId, provider, repository, view);
    }

    @Test
    public void givenACategoryWasCreatedThenCreateTheCategory(){
        final String title = "categoryTitle";
        final String stringColor = AppColor.BLUE.getHexValue();

        when(repository.insertCategory(title, stringColor))
                .thenReturn(Completable.complete());

        presenter.insertCategory(title, stringColor);
        verify(view, never()).onCategoryInsertedError();
    }

    @Test
    public void givenACategoryFailsToCreateThenHandleTheError(){
        final String title = "categoryTitle";
        final String stringColor = AppColor.BLUE.getHexValue();

        when(repository.insertCategory(title, stringColor))
                .thenReturn((Completable.fromAction(() -> {
                    throw new Exception("BOOM!");
                })));

        presenter.insertCategory(title, stringColor);
        verify(view).onCategoryInsertedError();
    }
}
