package com.example.pam_project.networking.version;

public class VersionMapper {

    public VersionModel map(final VersionResponse response) {
        if (response.version == null) {
            throw new IllegalArgumentException("Version was expected");
        }

        return new VersionModel(response.version);
    }
}
