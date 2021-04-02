package com.example.pam_project.lists.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.ListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilterDialogFragment extends DialogFragment {
    private static final CharSequence[] FILTER_ITEMS = {"Category 1", "Category 2", "Category 3"};
    private List<CharSequence> selectedItems = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean[] initiallySelected = new boolean[FILTER_ITEMS.length];
        Arrays.fill(initiallySelected, true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_by)
                .setMultiChoiceItems(FILTER_ITEMS, initiallySelected,
                        (DialogInterface.OnMultiChoiceClickListener) (dialog, which, isChecked) ->
                        {
                            CharSequence item = FILTER_ITEMS[which];
                            if (isChecked)
                                selectedItems.add(item);
                            else
                                selectedItems.remove(item);
                        })
                .setPositiveButton(R.string.ok,
                        (DialogInterface.OnClickListener) (dialog, which) -> {
                            showToast("Selected: " + which);
                            dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel,
                        (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
        return builder.create();
    }

    private void showToast(String msg) {
        if (getContext() == null || getContext().getApplicationContext() == null)
            return;
        Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
