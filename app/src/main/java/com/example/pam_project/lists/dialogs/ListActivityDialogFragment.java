package com.example.pam_project.lists.dialogs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public abstract class ListActivityDialogFragment extends DialogFragment {
    protected SelectedDialogItems callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (SelectedDialogItems)getActivity();
        }
        catch (ClassCastException e) {
            Log.d(this.getClass().getName(), "Activity doesn't implement the SelectedDialogItems interface");
        }
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }

    protected void showToast(String msg) {
        if (getContext() == null || getContext().getApplicationContext() == null)
            return;
        Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
