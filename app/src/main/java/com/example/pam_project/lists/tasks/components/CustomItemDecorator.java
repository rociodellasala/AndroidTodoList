package com.example.pam_project.lists.tasks.components;

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
    private static final int TASK_PENDING = 0;
    private static final int TASK_DONE = 1;

    private View[] mLayout;
    private TextView[] textView;
    private String[] allHeaders;
    private Context context;
    private int resId;

    public CustomItemDecorator(final Context context, RecyclerView parent, @LayoutRes int resId, String[] allHeaders) {
        this.allHeaders = allHeaders;
        mLayout = new View[allHeaders.length];
        textView = new TextView[allHeaders.length];
        this.context = context;
        this.resId = resId;
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
        String[] headers = updatePendingHeaders(parent);
        updateLayout(parent, headers);
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

    private void updateLayout(RecyclerView parent, String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            mLayout[i] = LayoutInflater.from(context).inflate(resId, parent, false);
            textView[i] = mLayout[i].findViewById(R.id.recycler_header);
            textView[i].setText(headers[i]);
            textView[i].setVisibility(View.INVISIBLE);
            mLayout[i].measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        }
    }

    private String[] updatePendingHeaders(RecyclerView parent) {
        String[] updatedHeaders = new String[allHeaders.length];

        int currentViewType = -1;

        for (int i = 0, j = 0; i < parent.getChildCount(); i++) {
            View view1 = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view1);
            int viewType = parent.getAdapter().getItemViewType(position);
            if (viewType != currentViewType) {
                if (viewType == TASK_PENDING) {
                    updatedHeaders[j] = allHeaders[TASK_PENDING];
                } else {
                    updatedHeaders[j] = allHeaders[TASK_DONE];
                }
                j++;
                currentViewType = viewType;
            }
        }

        return updatedHeaders;
    }
}