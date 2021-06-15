package com.example.pam_project.features.others.about.version;

import io.reactivex.Single;

public interface VersionRepository {

    Single<VersionModel> getVersion();
}
