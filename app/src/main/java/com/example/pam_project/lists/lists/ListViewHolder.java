package com.example.pam_project.lists.lists;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class ListViewHolder extends RecyclerView.ViewHolder {
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final String text) {
        final TextView textView = itemView.findViewById(R.id.text);
        textView.setText(text);
    }
}
