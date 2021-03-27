package com.example.pam_project.lists.lists;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class ListViewHolder extends RecyclerView.ViewHolder {
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final ListInformation list) {
        final TextView title = itemView.findViewById(R.id.title);
        final TextView numberOfTasks = itemView.findViewById(R.id.number_of_tasks);
        title.setText(list.getTitle());
        numberOfTasks.setText(list.getNumberOfTasks());
        title.setTextColor(Color.parseColor(list.getColor()));
        // Por ahora solo cambie el color del texto dinamicamente pq me parece q cambiar el borde
        // de border.xml es medio un quilombo, asique lo miramos dsp
    }
}
