package com.example.pam_project.features.about

import com.example.pam_project.networking.version.VersionModel

interface AboutView {
    fun bindAuthors(model: String?)
    fun onGeneralError()
    fun bindVersion(model: VersionModel?)
}