package com.example.pam_project.networking.version

class VersionMapper {
    fun map(response: VersionResponse?): VersionModel {
        requireNotNull(response!!.version) { "Version was expected" }
        return VersionModel(response.version)
    }
}