package com.example.pam_project.lists.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.pam_project.R;

import java.util.ArrayList;
import java.util.List;

public class SortByDialogFragment extends ListActivityDialogFragment {
    private static final String INITIAL_VALUE_KEY = "initialValue";


    public static SortByDialogFragment newInstance() {
        return newInstance(0);
    }

    public static SortByDialogFragment newInstance(final int initialValue) {
        SortByDialogFragment frag = new SortByDialogFragment();
        Bundle args = new Bundle();
        args.putInt(INITIAL_VALUE_KEY, initialValue);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        assert args != null;

        builder.setTitle(R.string.sort_by)
                .setSingleChoiceItems(R.array.sort_by_criteria, args.getInt(INITIAL_VALUE_KEY),
                        (dialog, which) -> {
                            final List<Integer> returnList = new ArrayList<>(1);
                            returnList.add(which);
                            callback.onSelectedItems(this.getClass(), returnList);
                            dialog.dismiss();
                        });
        return builder.create();
    }
}
