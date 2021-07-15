package com.example.pam_project.dialogs

interface SelectedDialogItems {
    fun onSelectedItems(klass: Class<*>?, items: MutableList<Int>?)
}