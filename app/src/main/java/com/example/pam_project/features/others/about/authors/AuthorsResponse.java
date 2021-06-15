package com.example.pam_project.features.others.about.authors;

import com.google.gson.annotations.SerializedName;

public class AuthorsResponse {
    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    public AuthorsResponse(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
