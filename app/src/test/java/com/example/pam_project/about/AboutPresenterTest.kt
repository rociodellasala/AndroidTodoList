package com.example.pam_project.about

import com.example.pam_project.TestSchedulerProvider
import com.example.pam_project.features.about.AboutPresenter
import com.example.pam_project.features.about.AboutView
import com.example.pam_project.networking.authors.AuthorsModel
import com.example.pam_project.networking.authors.AuthorsRepository
import com.example.pam_project.networking.version.VersionModel
import com.example.pam_project.networking.version.VersionRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class AboutPresenterTest {
    private lateinit var view: AboutView
    private lateinit var presenter: AboutPresenter
    private lateinit var authorsRepository: AuthorsRepository
    private lateinit var versionRepository: VersionRepository
    @Before
    fun setup() {
        val provider: SchedulerProvider = TestSchedulerProvider()
        view = Mockito.mock(AboutView::class.java)
        authorsRepository = Mockito.mock(AuthorsRepository::class.java)
        versionRepository = Mockito.mock(VersionRepository::class.java)
        presenter = AboutPresenter(authorsRepository, versionRepository, provider, view)
    }

    @Test
    fun givenAViewWasAttachedWhenEverythingIsOkThenFetchAuthorsAndVersion() {
        val authors: List<AuthorsModel?> = ArrayList()
        val concatAuthors = ""
        val versionModel = VersionModel("version")
        Mockito.`when`(authorsRepository.authors)
                .thenReturn(Single.just(authors))
        Mockito.`when`(versionRepository.version)
                .thenReturn(Single.just(versionModel))
        presenter.onViewAttached()
        Mockito.verify(view).bindAuthors(concatAuthors)
        Mockito.verify(view).bindVersion(versionModel)
    }

    @Test
    fun givenAViewWasAttachedWhenVersionFetchFailsThenHandleTheError() {
        val authors: List<AuthorsModel?> = ArrayList()
        val concatAuthors = ""
        Mockito.`when`(authorsRepository.authors)
                .thenReturn(Single.just(authors))
        Mockito.`when`(versionRepository.version)
                .thenReturn(Single.fromCallable { throw Exception("BOOM!") })
        presenter.onViewAttached()
        Mockito.verify(view).bindAuthors(concatAuthors)
        Mockito.verify(view).onGeneralError()
    }

    @Test
    fun givenAViewWasAttachedWhenAuthorsFetchFailsThenHandleTheError() {
        val versionModel = VersionModel("version")
        Mockito.`when`(authorsRepository.authors)
                .thenReturn(Single.fromCallable { throw Exception("BOOM!") })
        Mockito.`when`(versionRepository.version)
                .thenReturn(Single.just(versionModel))
        presenter.onViewAttached()
        Mockito.verify(view).bindVersion(versionModel)
        Mockito.verify(view).onGeneralError()
    }

    @Test
    fun givenAViewWasAttachedWhenAuthorsFetchAndVersionFailsThenHandleTheError() {
        val concatAuthors = ""
        val versionModel = VersionModel("version")
        Mockito.`when`(authorsRepository.authors)
                .thenReturn(Single.fromCallable { throw Exception("BOOM!") })
        Mockito.`when`(versionRepository.version)
                .thenReturn(Single.fromCallable { throw Exception("BOOM!") })
        presenter.onViewAttached()
        Mockito.verify(view, Mockito.times(2)).onGeneralError()
        Mockito.verify(view, Mockito.never()).bindVersion(versionModel)
        Mockito.verify(view, Mockito.never()).bindAuthors(concatAuthors)
    }
}