package com.example.pam_project.features.others.about.authors;

public class AuthorsModel {
    private final String name;

    public AuthorsModel(final String surname) { this.name = surname; }
    public AuthorsModel(final String name, final String surname) {
        this.name = surname + ", " + name;
    }

    public String getName() {
        return name;
    }
}
