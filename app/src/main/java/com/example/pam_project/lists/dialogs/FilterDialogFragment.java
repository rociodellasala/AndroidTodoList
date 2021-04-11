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
    private static final CharSequence[] FILTER_ITEMS = {/*"select_all",*/ "Category 1", "Category 2", "Category 3"};
    private final List<CharSequence> selectedItems = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean[] initiallySelected = new boolean[FILTER_ITEMS.length];
        Arrays.fill(initiallySelected, true);
        selectAll(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_by)
               .setMultiChoiceItems(FILTER_ITEMS, initiallySelected,
                        (DialogInterface.OnMultiChoiceClickListener) (dialog, which, isChecked) ->
                        {
                            CharSequence item = FILTER_ITEMS[which];
                            /*if (item.equals("select_all")) {
                                selectAll(isChecked);
                            }
                            else*/ if (isChecked) {
                                selectedItems.add(item);
                            }
                            else
                                selectedItems.remove(item);
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
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    private void selectAll(boolean doSelectAll) {
        if (doSelectAll)
            selectedItems.addAll(Arrays.asList(FILTER_ITEMS));
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
