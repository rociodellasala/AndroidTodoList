package com.example.pam_project.features.others.about;

import com.example.pam_project.features.others.about.authors.AuthorsModel;
import com.example.pam_project.features.others.about.version.VersionModel;

public interface AboutView {
    void bindAuthors(AuthorsModel model);

    void onAuthorsReceivedError();

    void bindVersion(VersionModel model);

    void onVersionReceivedError();
}
