package com.example.pam_project.features.about;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_project.R;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.networking.authors.AuthorsModel;
import com.example.pam_project.networking.authors.AuthorsRepository;
import com.example.pam_project.networking.version.VersionModel;
import com.example.pam_project.networking.version.VersionRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.util.List;
import java.util.Objects;

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
        String appName = (String) Objects.requireNonNull(getSupportActionBar()).getTitle();
        String about = getApplicationContext().getResources().getString(R.string.about);
        Objects.requireNonNull(getSupportActionBar()).setTitle(about + " " + appName);

        authorsView = findViewById(R.id.about_authors);
        versionView = findViewById(R.id.about_version);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }
    
    private void hideLoader() {
        findViewById(R.id.about_loader).setVisibility(View.GONE);
    }

    @Override
    public void bindAuthors(List<AuthorsModel> model) {
        findViewById(R.id.authors).setVisibility(View.VISIBLE);
        authorsView.setText(concatAuthors(model));
        showData();
    }

    private void showData() {
        if(findViewById(R.id.authors).getVisibility() == View.VISIBLE && findViewById(R.id.version).getVisibility() == View.VISIBLE) {
            hideLoader();
            findViewById(R.id.about_information).setVisibility(View.VISIBLE);
        }
    }

    private String concatAuthors(List<AuthorsModel> model) {
        StringBuilder authors = new StringBuilder();
        for (int i = 0; i < model.size(); i++) {
            authors.append(model.get(i).getName()).append("\n");
        }

        return authors.toString();
    }

    @Override
    public void onGeneralError() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_general), Toast.LENGTH_LONG).show();
    }

    @Override
    public void bindVersion(VersionModel model) {
        findViewById(R.id.version).setVisibility(View.VISIBLE);
        versionView.setText(model.getVersion());
        showData();
    }
}
