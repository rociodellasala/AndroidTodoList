package com.example.pam_project.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import com.example.pam_project.R
import java.util.*

class SortByDialogFragment : ListActivityDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val args = arguments!!
        builder.setTitle(R.string.sort_by)
                .setSingleChoiceItems(R.array.sort_by_criteria, args.getInt(INITIAL_VALUE_KEY)
                ) { dialog: DialogInterface, which: Int ->
                    val returnList: MutableList<Int> = ArrayList(1)
                    returnList.add(which)
                    callback!!.onSelectedItems(this.javaClass, returnList)
                    dialog.dismiss()
                }
        return builder.create()
    }

    companion object {
        private const val INITIAL_VALUE_KEY = "initialValue"

        fun newInstance(initialValue: Int): SortByDialogFragment {
            val frag = SortByDialogFragment()
            val args = Bundle()
            args.putInt(INITIAL_VALUE_KEY, initialValue)
            frag.arguments = args
            return frag
        }
    }
}