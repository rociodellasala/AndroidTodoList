package com.example.pam_project.features.about

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pam_project.R
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.networking.version.VersionModel

class AboutActivity : AppCompatActivity(), AboutView {
    private lateinit var presenter: AboutPresenter
    private lateinit var authorsView: TextView
    private lateinit var versionView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        createPresenter()
        setUpViews()
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached()
    }

    private fun createPresenter() {
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container.schedulerProvider
        val repository = container.authorsRepository
        val versionRepo = container.versionRepository
        presenter = AboutPresenter(repository, versionRepo, schedulerProvider, this)
    }

    private fun setUpViews() {
        val appName = supportActionBar?.title as String?
        val about = applicationContext.resources.getString(R.string.about)
        supportActionBar?.title = "$about $appName"
        authorsView = findViewById(R.id.about_authors)
        versionView = findViewById(R.id.about_version)
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    private fun hideLoader() {
        findViewById<View>(R.id.about_loader).visibility = View.GONE
    }

    override fun bindAuthors(model: String?) {
        findViewById<View>(R.id.authors).visibility = View.VISIBLE
        authorsView.text = model
        showData()
    }

    private fun showData() {
        if (findViewById<View>(R.id.authors).visibility == View.VISIBLE && findViewById<View>(R.id.version).visibility == View.VISIBLE) {
            hideLoader()
            findViewById<View>(R.id.about_information).visibility = View.VISIBLE
        }
    }

    override fun onGeneralError() {
        Toast.makeText(applicationContext, getString(R.string.error_general), Toast.LENGTH_LONG).show()
    }

    override fun bindVersion(model: VersionModel?) {
        findViewById<View>(R.id.version).visibility = View.VISIBLE
        versionView.text = model?.version
        showData()
    }
}