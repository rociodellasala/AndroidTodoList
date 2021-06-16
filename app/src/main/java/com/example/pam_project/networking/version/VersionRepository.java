package com.example.pam_project.networking.version;

import io.reactivex.Single;

public interface VersionRepository {

    Single<VersionModel> getVersion();
}
