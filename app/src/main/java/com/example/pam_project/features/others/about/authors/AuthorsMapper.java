package com.example.pam_project.features.others.about.authors;

public class AuthorsMapper {

    public AuthorsModel map(final AuthorsResponse response) {
        if (response.lastName == null) {
            throw new IllegalArgumentException("Lastname was expected");
        }

        if (response.firstName == null) {
            return new AuthorsModel(response.lastName);
        }

        return new AuthorsModel(response.firstName, response.lastName);
    }
}
