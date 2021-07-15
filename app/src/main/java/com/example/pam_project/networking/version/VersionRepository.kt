package com.example.pam_project.networking.version

import io.reactivex.Single

interface VersionRepository {
    val version: Single<VersionModel?>
}