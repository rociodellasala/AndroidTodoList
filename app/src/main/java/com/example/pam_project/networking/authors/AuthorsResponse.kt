package com.example.pam_project.networking.authors

import com.google.gson.annotations.SerializedName

class AuthorsResponse(@field:SerializedName("firstName") val firstName: String?,
                      @field:SerializedName("lastName") val lastName: String?)