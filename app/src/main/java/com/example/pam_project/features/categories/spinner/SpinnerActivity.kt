package com.example.pam_project.features.categories.spinner

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener

class SpinnerActivity : Activity(), OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                pos: Int, id: Long) {
        // Maybe put category color on spinner for user to check if category is ok
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Complete if categories can disappear while on view
    }
}