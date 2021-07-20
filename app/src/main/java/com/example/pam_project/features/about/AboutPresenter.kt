package com.example.pam_project.features.about

import com.example.pam_project.networking.authors.AuthorsModel
import com.example.pam_project.networking.authors.AuthorsRepository
import com.example.pam_project.networking.version.VersionModel
import com.example.pam_project.networking.version.VersionRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class AboutPresenter(private val authorsRepository: AuthorsRepository, private val versionRepository: VersionRepository,
                     private val provider: SchedulerProvider, view: AboutView) {
    private val view: WeakReference<AboutView> = WeakReference(view)
    private var fetchAuthorDisposable: Disposable? = null
    private var fetchVersionDisposable: Disposable? = null

    fun onViewAttached() {
        if (view.get() != null) {
            fetchAuthors()
            fetchVersion()
        }
    }

    private fun fetchAuthors() {
        fetchAuthorDisposable = authorsRepository.authors
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({ model: List<AuthorsModel?>? -> onAuthorsReceived(model) }) { throwable: Throwable
                    -> onAuthorsReceivedError(throwable) }
    }

    private fun onAuthorsReceived(model: List<AuthorsModel?>?) {
        if (view.get() != null) {
            view.get()!!.bindAuthors(concatAuthors(model))
        }
    }

    private fun concatAuthors(model: List<AuthorsModel?>?): String {
        val authors = StringBuilder()
        for (i in model!!.indices) {
            authors.append(model[i]?.name).append("\n")
        }
        return authors.toString()
    }

    private fun onAuthorsReceivedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onGeneralError()
        }
    }

    private fun fetchVersion() {
        fetchVersionDisposable = versionRepository.version
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({ model: VersionModel? -> onVersionReceived(model) }) { throwable: Throwable -> onVersionReceivedError(throwable) }
    }

    private fun onVersionReceived(model: VersionModel?) {
        if (view.get() != null) {
            view.get()!!.bindVersion(model)
        }
    }

    private fun onVersionReceivedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onGeneralError()
        }
    }

    fun onViewDetached() {
        if (fetchAuthorDisposable != null) {
            fetchAuthorDisposable!!.dispose()
        }
        if (fetchVersionDisposable != null) {
            fetchVersionDisposable!!.dispose()
        }
    }

}