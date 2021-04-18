package com.example.pam_project.lists.categories;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final CategoryInformation category) {
        final TextView categoryTitle = itemView.findViewById(R.id.category_title);
        categoryTitle.setText(category.getTitle());
        GradientDrawable drawable = (GradientDrawable) itemView.getBackground();
        drawable.setColor(Color.parseColor(category.getColor().getHexValue()));
    }
}
