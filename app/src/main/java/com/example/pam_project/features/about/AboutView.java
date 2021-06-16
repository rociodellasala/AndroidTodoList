package com.example.pam_project.features.about;

import com.example.pam_project.networking.version.VersionModel;

public interface AboutView {
    void bindAuthors(String model);

    void onGeneralError();

    void bindVersion(VersionModel model);
}
