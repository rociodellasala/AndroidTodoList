package com.example.pam_project.lists.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.pam_project.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterDialogFragment extends ListActivityDialogFragment {
    public static final CharSequence[] FILTER_ITEMS = {/*"select_all",*/ "Category 1", "Category 2", "Category 3"};
    private static final boolean[] INITIALLY_SELECTED = new boolean[FILTER_ITEMS.length];
    private final List<Integer> selectedItems = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Arrays.fill(INITIALLY_SELECTED, true);
        selectAll(true);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_by)
               .setMultiChoiceItems(FILTER_ITEMS, INITIALLY_SELECTED,
                        (DialogInterface.OnMultiChoiceClickListener) (dialog, which, isChecked) ->
                        {
                            /*if (item.equals("select_all")) {
                                selectAll(isChecked);
                            }
                            else*/
                            if (isChecked) {
                                selectedItems.add(which);
                            }
                            else
                                selectedItems.remove(which);
                        })
               .setPositiveButton(R.string.ok,
                        (DialogInterface.OnClickListener) (dialog, which) -> {
                            // showToast("Selected: " + selectedItems.size());
                            callback.onSelectedItems(this.getClass(), selectedItems);
                            dialog.dismiss();
               })
               .setNegativeButton(R.string.cancel,
                        (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
        return builder.create();
    }

    public static FilterDialogFragment newInstance() {
        final FilterDialogFragment frag = new FilterDialogFragment();
        final Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    private void selectAll(boolean doSelectAll) {
        if (doSelectAll) {
            for (int i = 0; i < FILTER_ITEMS.length; i++)
                selectedItems.add(i);
        }
        else
            selectedItems.clear();

        /*AlertDialog dialog = ((AlertDialog)getDialog());
        if (dialog == null) {
            // es NULL lamentablemente
            return;
        }
        ListView items = dialog.getListView();
        for(int i = 0; i < items.getAdapter().getCount(); i++) {
            items.setItemChecked(i, doSelectAll);
        }*/
    }
}
