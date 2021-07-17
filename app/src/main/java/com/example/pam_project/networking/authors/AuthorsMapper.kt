package com.example.pam_project.networking.authors

import java.util.*

class AuthorsMapper {
    fun map(response: ListAuthorsResponse?): List<AuthorsModel> {
        val authors: MutableList<AuthorsModel> = ArrayList()
        for (i in response!!.allAuthors.indices) {
            val currentAuthor = response.allAuthors[i]
            requireNotNull(currentAuthor!!.lastName) { "Lastname was expected" }
            val author: AuthorsModel = if (currentAuthor.firstName == null) {
                AuthorsModel(currentAuthor.lastName)
            } else {
                AuthorsModel(currentAuthor.firstName, currentAuthor.lastName)
            }
            authors.add(author)
        }
        return authors
    }
}