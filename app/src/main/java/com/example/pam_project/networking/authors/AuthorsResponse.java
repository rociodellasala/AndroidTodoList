package com.example.pam_project.networking.authors;

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
}
