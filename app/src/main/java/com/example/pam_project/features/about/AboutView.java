package com.example.pam_project.features.about;

import com.example.pam_project.networking.authors.AuthorsModel;
import com.example.pam_project.networking.version.VersionModel;

import java.util.List;

public interface AboutView {
    void bindAuthors(List<AuthorsModel> model);

    void onGeneralError();

    void bindVersion(VersionModel model);
}
