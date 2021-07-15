package com.example.pam_project.networking.authors

import io.reactivex.Single

interface AuthorsRepository {
    val authors: Single<List<AuthorsModel?>?>
}