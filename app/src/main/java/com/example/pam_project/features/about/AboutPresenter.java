package com.example.pam_project.features.about;

import com.example.pam_project.networking.authors.AuthorsModel;
import com.example.pam_project.networking.authors.AuthorsRepository;
import com.example.pam_project.networking.version.VersionModel;
import com.example.pam_project.networking.version.VersionRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class AboutPresenter {
    private final AuthorsRepository authorsRepository;
    private final VersionRepository versionRepository;
    private final WeakReference<AboutView> view;
    private Disposable authorDisposable;
    private Disposable versionDisposable;
    private final SchedulerProvider provider;

    public AboutPresenter(AuthorsRepository authorsRepository, VersionRepository versionRepository, SchedulerProvider schedulerProvider,
                          AboutView view) {
        this.authorsRepository = authorsRepository;
        this.versionRepository = versionRepository;
        this.provider = schedulerProvider;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (view.get() != null) {
            getAuthorsRepository();
            getVersionRepository();
        }
    }

    private void getAuthorsRepository() {
        authorDisposable = authorsRepository.getAuthors()
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onAuthorsReceived, this::onAuthorsReceivedError);
    }

    private void onAuthorsReceived(final List<AuthorsModel> model) {
        if (view.get() != null) {
            view.get().bindAuthors(model);
        }
    }

    private void onAuthorsReceivedError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onGeneralError();
        }
    }

    private void getVersionRepository() {
        versionDisposable = versionRepository.getVersion()
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onVersionReceived, this::onVersionReceivedError);
    }

    private void onVersionReceived(final VersionModel model) {
        if (view.get() != null) {
            view.get().bindVersion(model);
        }
    }

    private void onVersionReceivedError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onGeneralError();
        }
    }

    public void onViewDetached() {
        if (authorDisposable != null){
            authorDisposable.dispose();
        }
        if (versionDisposable != null){
            versionDisposable.dispose();
        }
    }
}
