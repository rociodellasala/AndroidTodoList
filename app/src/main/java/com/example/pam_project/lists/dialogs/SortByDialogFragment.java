package com.example.pam_project.lists.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.pam_project.R;

import java.util.ArrayList;
import java.util.List;

public class SortByDialogFragment extends ListActivityDialogFragment {
    private static final int INITIAL_VALUE = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_by)
               .setSingleChoiceItems(R.array.sort_by_criteria, INITIAL_VALUE,
                       (DialogInterface.OnClickListener) (dialog, which) -> {
                   // showToast("Selected: " + which);
                   final List<Integer> returnList = new ArrayList<>(1);
                   returnList.add(which);
                   callback.onSelectedItems(this.getClass(), returnList);
                   dialog.dismiss();
               });
        return builder.create();
    }

    public static SortByDialogFragment newInstance() {
        SortByDialogFragment frag = new SortByDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }
}
