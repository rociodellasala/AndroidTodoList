package com.example.pam_project.lists.lists;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class ListViewHolder extends RecyclerView.ViewHolder {
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bind(final ListInformation list) {
        final TextView title = itemView.findViewById(R.id.title);
        final TextView numberOfTasks = itemView.findViewById(R.id.number_of_tasks);
        title.setText(list.getTitle());
        numberOfTasks.setText(list.getNumberOfTasks());
        GradientDrawable drawable = (GradientDrawable) itemView.getBackground();
        drawable.setColor(Color.parseColor(list.getColor().getHexValue()));
    }
}
