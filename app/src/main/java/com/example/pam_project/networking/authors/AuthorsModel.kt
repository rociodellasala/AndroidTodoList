package com.example.pam_project.networking.authors

class AuthorsModel {
    val name: String?

    constructor(surname: String?) {
        name = surname
    }

    constructor(name: String?, surname: String?) {
        this.name = "$surname, $name"
    }
}