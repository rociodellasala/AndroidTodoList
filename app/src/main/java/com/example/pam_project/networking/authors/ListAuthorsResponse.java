package com.example.pam_project.networking.authors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListAuthorsResponse {
    @SerializedName("authors")
    public List<AuthorsResponse> allAuthors;

    public ListAuthorsResponse(List<AuthorsResponse> allAuthors) {
        this.allAuthors = allAuthors;
    }
}
