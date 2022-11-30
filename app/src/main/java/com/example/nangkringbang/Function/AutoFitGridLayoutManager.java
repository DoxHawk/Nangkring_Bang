package com.example.nangkringbang.Function;

import android.content.Context;
import android.content.res.Resources;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AutoFitGridLayoutManager extends GridLayoutManager {
    private int columnWidth;
    private boolean columnWidthChanged = true;

    public AutoFitGridLayoutManager(Context context) {
        super(context, 1);
        setColumnWidth();
    }

    public void setColumnWidth() {
        int newColumnWidth = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
        if (newColumnWidth > 0 && newColumnWidth != this.columnWidth) {
            this.columnWidth = newColumnWidth;
            this.columnWidthChanged = true;
        }
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int totalSpace;
        if (this.columnWidthChanged && this.columnWidth > 0) {
            if (getOrientation() == 1) {
                totalSpace = (getWidth() - getPaddingRight()) - getPaddingLeft();
            } else {
                totalSpace = (getHeight() - getPaddingTop()) - getPaddingBottom();
            }
            setSpanCount(Math.max(1, totalSpace / this.columnWidth));
            this.columnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}
