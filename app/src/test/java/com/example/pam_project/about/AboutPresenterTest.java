package com.example.pam_project.about;

import com.example.pam_project.TestSchedulerProvider;
import com.example.pam_project.features.about.AboutPresenter;
import com.example.pam_project.features.about.AboutView;
import com.example.pam_project.networking.authors.AuthorsModel;
import com.example.pam_project.networking.authors.AuthorsRepository;
import com.example.pam_project.networking.version.VersionModel;
import com.example.pam_project.networking.version.VersionRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AboutPresenterTest {

    private AboutView view;
    private AboutPresenter presenter;
    private AuthorsRepository authorsRepository;
    private VersionRepository versionRepository;

    @Before
    public void setup() {
        SchedulerProvider provider = new TestSchedulerProvider();

        view = mock(AboutView.class);

        authorsRepository = mock(AuthorsRepository.class);

        versionRepository = mock(VersionRepository.class);

        presenter = new AboutPresenter(authorsRepository,versionRepository, provider, view);
    }

    @Test
    public void givenAViewWasAttachedWhenEverythingIsOkThenFetchAuthorsAndVersion(){
        List<AuthorsModel> authors = new ArrayList<>();
        String concatAuthors = "";
        VersionModel versionModel = new VersionModel("version");


        when(authorsRepository.getAuthors())
                .thenReturn(Single.just(authors));

        when(versionRepository.getVersion())
                .thenReturn(Single.just(versionModel));

        presenter.onViewAttached();

        verify(view).bindAuthors(concatAuthors);
        verify(view).bindVersion(versionModel);
    }

    @Test
    public void givenAViewWasAttachedWhenVersionFetchFailsThenHandleTheError(){
        List<AuthorsModel> authors = new ArrayList<>();
        String concatAuthors = "";

        when(authorsRepository.getAuthors())
                .thenReturn(Single.just(authors));

        when(versionRepository.getVersion())
                .thenReturn((Single.fromCallable(() -> {
                    throw new Exception("BOOM!");
                })));

        presenter.onViewAttached();

        verify(view).bindAuthors(concatAuthors);
        verify(view).onGeneralError();
    }

    @Test
    public void givenAViewWasAttachedWhenAuthorsFetchFailsThenHandleTheError(){
        VersionModel versionModel = new VersionModel("version");

        when(authorsRepository.getAuthors())
                .thenReturn((Single.fromCallable(() -> {
                    throw new Exception("BOOM!");
                })));

        when(versionRepository.getVersion())
                .thenReturn(Single.just(versionModel));

        presenter.onViewAttached();

        verify(view).bindVersion(versionModel);
        verify(view).onGeneralError();
    }

    @Test
    public void givenAViewWasAttachedWhenAuthorsFetchAndVersionFailsThenHandleTheError(){
        String concatAuthors = "";
        VersionModel versionModel = new VersionModel("version");

        when(authorsRepository.getAuthors())
                .thenReturn((Single.fromCallable(() -> {
                    throw new Exception("BOOM!");
                })));

        when(versionRepository.getVersion())
                .thenReturn((Single.fromCallable(() -> {
                    throw new Exception("BOOM!");
                })));

        presenter.onViewAttached();

        verify(view, times(2)).onGeneralError();
        verify(view, never()).bindVersion(versionModel);
        verify(view, never()).bindAuthors(concatAuthors);
    }
}
