package com.example.pam_project.lists.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.pam_project.R;

public class SortByDialogFragment extends DialogFragment {
    private int selectedItem = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_by)
               .setSingleChoiceItems(R.array.sort_by_criteria, selectedItem,
                       (DialogInterface.OnClickListener) (dialog, which) -> {
                   selectedItem = which;
                   showToast("Selected: " + selectedItem);
                   dialog.dismiss();
               });
        return builder.create();
    }

    private void showToast(String msg) {
        if (getContext() == null || getContext().getApplicationContext() == null)
            return;
        Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
