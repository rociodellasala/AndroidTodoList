package com.example.pam_project.lists.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.pam_project.R;
import com.example.pam_project.lists.categories.components.CategoryInformation;

import java.util.ArrayList;
import java.util.List;

public class FilterDialogFragment extends ListActivityDialogFragment {
    private static final String INITIAL_SELECTION_KEY = "selectedItems";
    private static final String ITEMS_KEY = "items";
    protected CharSequence[] filterItems;
    private List<Integer> selectedItems;

    public static FilterDialogFragment newInstance(final List<CategoryInformation> items) {
        return newInstance(items, null);
    }

    public static FilterDialogFragment newInstance(final List<CategoryInformation> items,
                                                   List<Integer> initialSelection) {
        final FilterDialogFragment frag = new FilterDialogFragment();
        final Bundle args = new Bundle();

        final CharSequence[] itemsCharSeq = new CharSequence[items.size()];
        for (int i = 0; i < items.size(); i++)
            itemsCharSeq[i] = items.get(i).getTitle();

        if (initialSelection == null) {
            initialSelection = new ArrayList<>(items.size());
            // all items are selected initially
            for (int i = 0; i < items.size(); i++) {
                initialSelection.add(i);
            }
        }

        args.putCharSequenceArray(ITEMS_KEY, itemsCharSeq);
        args.putIntegerArrayList(INITIAL_SELECTION_KEY, (ArrayList<Integer>) initialSelection);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        assert args != null;
        filterItems = args.getCharSequenceArray(ITEMS_KEY);
        selectedItems = args.getIntegerArrayList(INITIAL_SELECTION_KEY);

        builder.setTitle(R.string.filter)
                .setMultiChoiceItems(filterItems, listToBooleanArray(selectedItems),
                        (dialog, which, isChecked) ->
                        {
                            if (isChecked) {
                                selectedItems.add(which);
                            } else {
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

    private boolean[] listToBooleanArray(final List<Integer> selection) {
        boolean[] result = new boolean[filterItems.length];
        for (Integer index : selection)
            result[index] = true;
        return result;
    }
}
