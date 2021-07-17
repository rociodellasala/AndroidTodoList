package com.example.pam_project.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import com.example.pam_project.R
import com.example.pam_project.features.categories.list.CategoryInformation
import java.util.*

class FilterDialogFragment : ListActivityDialogFragment() {
    private var filterItems: Array<CharSequence>? = null
    private var selectedItems: MutableList<Int>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val args = arguments!!
        filterItems = args.getCharSequenceArray(ITEMS_KEY)
        selectedItems = args.getIntegerArrayList(INITIAL_SELECTION_KEY)
        builder.setTitle(R.string.filter)
                .setMultiChoiceItems(filterItems, listToBooleanArray(selectedItems)
                ) { _: DialogInterface?, which: Int, isChecked: Boolean ->
                    if (isChecked) {
                        selectedItems!!.add(which)
                    } else {
                        selectedItems!!.remove(which)
                    }
                }
                .setPositiveButton(R.string.ok
                ) { dialog: DialogInterface, _: Int ->
                    callback!!.onSelectedItems(this.javaClass, selectedItems)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel
                ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        return builder.create()
    }

    private fun listToBooleanArray(selection: List<Int>?): BooleanArray {
        val result = BooleanArray(filterItems!!.size)
        for (index in selection!!) result[index] = true
        return result
    }

    companion object {
        private const val INITIAL_SELECTION_KEY = "selectedItems"
        private const val ITEMS_KEY = "items"

        fun newInstance(items: List<CategoryInformation>, initialSelection: MutableList<Int?>?): FilterDialogFragment {
            var selection = initialSelection
            val frag = FilterDialogFragment()
            val args = Bundle()
            val itemsCharSeq = arrayOfNulls<CharSequence>(items.size)
            for (i in items.indices) itemsCharSeq[i] = items[i].title
            if (initialSelection == null) {
                selection = ArrayList(items.size)
                // all items are selected initially
                for (i in items.indices) {
                    selection.add(i)
                }
            }
            args.putCharSequenceArray(ITEMS_KEY, itemsCharSeq)
            args.putIntegerArrayList(INITIAL_SELECTION_KEY, selection as ArrayList<Int?>?)
            frag.arguments = args
            return frag
        }
    }
}