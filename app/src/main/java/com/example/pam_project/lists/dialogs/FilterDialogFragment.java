package com.example.pam_project.lists.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.pam_project.R;

import java.util.ArrayList;
import java.util.List;

public class FilterDialogFragment extends ListActivityDialogFragment {
    private static final String INITIAL_SELECTION_KEY = "selectedItems";
    public static final CharSequence[] FILTER_ITEMS = {
        /*"select_all",*/
        "Category 1",
        "Category 2",
        "Category 3"
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final List<Integer> selectedItems = getArguments().getIntegerArrayList(INITIAL_SELECTION_KEY);
        builder.setTitle(R.string.filter)
               .setMultiChoiceItems(FILTER_ITEMS, listToBooleanArray(selectedItems),
                       (dialog, which, isChecked) ->
                       {
                           if (isChecked) {
                               selectedItems.add(which);
                           }
                           else {
                               selectedItems.remove((Integer) which);
                           }
                       })
               .setPositiveButton(R.string.ok,
                       (dialog, which) -> {
                           callback.onSelectedItems(this.getClass(), selectedItems);
                           dialog.dismiss();
              })
               .setNegativeButton(R.string.cancel,
                       (dialog, which) -> dialog.cancel());
        return builder.create();
    }

    public static FilterDialogFragment newInstance() {
        return newInstance(null);
    }

    public static FilterDialogFragment newInstance(List<Integer> initialSelection) {
        final FilterDialogFragment frag = new FilterDialogFragment();
        final Bundle args = new Bundle();
        if (initialSelection == null) {
            initialSelection = new ArrayList<>(FILTER_ITEMS.length);
            // all items are selected initially
            for (int i = 0; i < FILTER_ITEMS.length; i++) {
                initialSelection.add(i);
            }
        }
        args.putIntegerArrayList(INITIAL_SELECTION_KEY, (ArrayList<Integer>) initialSelection);
        frag.setArguments(args);
        return frag;
    }

    private boolean[] listToBooleanArray(final List<Integer> selection) {
        boolean[] result = new boolean[FILTER_ITEMS.length];
        for (Integer index : selection)
            result[index] = true;
        return result;
    }
}
