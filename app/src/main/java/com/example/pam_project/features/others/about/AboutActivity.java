package com.example.pam_project.features.others.about;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.features.others.about.authors.AuthorsModel;
import com.example.pam_project.features.others.about.authors.AuthorsRepository;
import com.example.pam_project.features.others.about.version.VersionModel;
import com.example.pam_project.features.others.about.version.VersionRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

public class AboutActivity extends AppCompatActivity implements AboutView {
    private AboutPresenter presenter;
    private TextView authorsView;
    private TextView versionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        createPresenter();
        setUpViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached();
    }

    private void createPresenter() {
        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final SchedulerProvider schedulerProvider = container.getSchedulerProvider();
        final AuthorsRepository repository = container.getAuthorsRepository();
        final VersionRepository versionRepo = container.getVersionRepository();
        presenter = new AboutPresenter(repository, versionRepo, schedulerProvider, this);
    }

    private void setUpViews() {
        authorsView = findViewById(R.id.about_authors);
        versionView = findViewById(R.id.about_version);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

    @Override
    public void bindAuthors(AuthorsModel model) {
        authorsView.setText(model.getName());
    }

    @Override
    public void onAuthorsReceivedError() {
        // Toast.makeText();
        // TODO
    }

    @Override
    public void bindVersion(VersionModel model) {
        versionView.setText(model.getVersion());
    }

    @Override
    public void onVersionReceivedError() {
        // Toast.makeText();
        // TODO
    }
}
