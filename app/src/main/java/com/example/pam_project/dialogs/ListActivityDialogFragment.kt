package com.example.pam_project.dialogs

import android.content.Context
import android.util.Log
import androidx.fragment.app.DialogFragment

abstract class ListActivityDialogFragment : DialogFragment() {
    protected var callback: SelectedDialogItems? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = activity as SelectedDialogItems?
        } catch (e: ClassCastException) {
            Log.d(this.javaClass.name, "Activity doesn't implement the SelectedDialogItems interface")
        }
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }
}