package com.example.pam_project.lists.tasks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class CustomItemDecorator extends RecyclerView.ItemDecoration {
    private View[] mLayout;
    private TextView[] textView;
    private String[] headers;

    public CustomItemDecorator(final Context context, RecyclerView parent, @LayoutRes int resId, String[] headers) {
        this.headers = headers;
        this.initializeLayout(parent, context, resId, headers.length);
    }

    void initializeLayout(RecyclerView parent, Context context, int resId, int size) {
        mLayout = new View[size];
        textView = new TextView[size];

        for(int i = 0; i < size; i++) {
            mLayout[i] = LayoutInflater.from(context).inflate(resId, parent, false);
            textView[i] = mLayout[i].findViewById(R.id.recycler_header);
            textView[i].setText(headers[i]);
            textView[i].setVisibility(View.INVISIBLE);
            mLayout[i].measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int currentViewType = -1;

        for (int i = 0, j = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            int viewType = parent.getAdapter().getItemViewType(position);
            if (viewType != currentViewType) {
                mLayout[j].layout(parent.getLeft(), 0, parent.getRight(), mLayout[j].getMeasuredHeight());
                textView[j].setVisibility(View.VISIBLE);
                textView[j].setY(view.getTop() - mLayout[j].getMeasuredHeight());
                mLayout[j].draw(c);
                currentViewType = viewType;
                j++;
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int currentViewType = -1;

        for (int i = 0, j = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            int viewType = parent.getAdapter().getItemViewType(position);
            if (viewType != currentViewType) {
                outRect.set(0, mLayout[j].getMeasuredHeight(), 0, 0);
                currentViewType = viewType;
                j++;
            } else {
                outRect.setEmpty();
            }
        }
    }
}