package com.example.pam_project.networking.authors

import com.google.gson.annotations.SerializedName

class ListAuthorsResponse(@field:SerializedName("authors") val allAuthors: List<AuthorsResponse?>)