package com.example.pam_project.features.others.about.version;

import com.google.gson.annotations.SerializedName;

public class VersionResponse {
    @SerializedName("version")
    public String version;


    public VersionResponse(String version) {
        this.version = version;
    }

//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
}
