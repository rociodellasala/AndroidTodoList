package com.example.pam_project.features.others.about;

import com.example.pam_project.features.others.about.authors.AuthorsModel;
import com.example.pam_project.features.others.about.authors.AuthorsRepository;
import com.example.pam_project.features.others.about.version.VersionModel;
import com.example.pam_project.features.others.about.version.VersionRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

public class AboutPresenter {
    private final AuthorsRepository authorsRepository;
    private final VersionRepository versionRepository;
    private final WeakReference<AboutView> view;
    private Disposable getConfigDisposable;
    private final SchedulerProvider provider;

    public AboutPresenter(AuthorsRepository authorsRepository, VersionRepository versionRepository, SchedulerProvider schedulerProvider, AboutView view) {
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
        getConfigDisposable = authorsRepository.getAuthors()
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onAuthorsReceived, this::onAuthorsReceivedError);
    }

    private void onAuthorsReceived(final AuthorsModel model) {
        if (view.get() != null) {
            view.get().bindAuthors(model);
        }
    }

    private void onAuthorsReceivedError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onAuthorsReceivedError();
        }
    }

    private void getVersionRepository() {
        getConfigDisposable = versionRepository.getVersion()
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
            view.get().onVersionReceivedError();
        }
    }

    public void onViewDetached() {
        if (getConfigDisposable != null){
            getConfigDisposable.dispose();
        }
    }
}
