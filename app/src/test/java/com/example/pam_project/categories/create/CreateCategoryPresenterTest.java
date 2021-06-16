package com.example.pam_project.categories.create;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.categories.create.CreateCategoryPresenter;
import com.example.pam_project.features.categories.create.CreateCategoryView;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.constants.AppColor;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateCategoryPresenterTest {

    private CategoriesRepository repository;
    private CreateCategoryView view;
    private CreateCategoryPresenter presenter;

    @Before
    public void setup() {
        SchedulerProvider provider = new TestSchedulerProvider();

        repository = mock(CategoriesRepository.class);

        view = mock(CreateCategoryView.class);

        presenter = new CreateCategoryPresenter(provider, repository, view);
    }

    @Test
    public void givenACategoryIsCreatedWhenEverythingIsOkThenCreateTheCategory(){
        final String title = "categoryTitle";
        final String stringColor = AppColor.BLUE.getHexValue();

        when(repository.insertCategory(title, stringColor))
                .thenReturn(Completable.complete());

        presenter.insertCategory(title, stringColor);
        verify(view, never()).onCategoryInsertedError();
    }

    @Test
    public void givenACategoryIsCreatedWhenAnErrorOccursThenHandleTheError(){
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
