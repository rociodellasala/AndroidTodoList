package com.example.pam_project.features.others.about.version;

import com.example.pam_project.features.others.about.APIService;
import com.example.pam_project.utils.networking.RetrofitUtils;

import io.reactivex.Single;

public class RestVersionRepository implements VersionRepository {
    private final APIService service;
    private final VersionMapper versionMapper;

    public RestVersionRepository(final APIService service, final VersionMapper versionMapper) {
        this.service = service;
        this.versionMapper = versionMapper;
    }

    @Override
    public Single<VersionModel> getVersion() {
        return RetrofitUtils.performRequest(service.getVersion()).map(versionMapper::map);
    }
}
