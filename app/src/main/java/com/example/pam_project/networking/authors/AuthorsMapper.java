package com.example.pam_project.networking.authors;

import java.util.ArrayList;
import java.util.List;

public class AuthorsMapper {

    public List<AuthorsModel> map(final ListAuthorsResponse response) {
        List<AuthorsModel> authors = new ArrayList<>();

        for (int i = 0; i < response.allAuthors.size(); i++) {
            AuthorsResponse currentAuthor = response.allAuthors.get(i);
            if (currentAuthor.lastName == null) {
                throw new IllegalArgumentException("Lastname was expected");
            }

            AuthorsModel author;

            if (currentAuthor.firstName == null) {
                author = new AuthorsModel(currentAuthor.lastName);
            } else {
                author = new AuthorsModel(currentAuthor.firstName, currentAuthor.lastName);
            }

            authors.add(author);
        }

        return authors;
    }
}
