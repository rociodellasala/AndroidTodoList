package com.example.pam_project.networking.version;

import com.google.gson.annotations.SerializedName;

public class VersionResponse {
    @SerializedName("version")
    public final String version;


    public VersionResponse(String version) {
        this.version = version;
    }
}
