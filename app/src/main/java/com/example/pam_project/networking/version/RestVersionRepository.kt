package com.example.pam_project.networking.version

import com.example.pam_project.networking.APIService
import com.example.pam_project.networking.RetrofitUtils
import io.reactivex.Single

class RestVersionRepository(private val service: APIService?, private val versionMapper: VersionMapper?) : VersionRepository {
    override val version: Single<VersionModel?>
        get() = RetrofitUtils.performRequest(service.getVersion()).map { response: VersionResponse? -> versionMapper!!.map(response) }
}