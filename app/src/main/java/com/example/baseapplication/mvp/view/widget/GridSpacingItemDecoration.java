package com.example.baseapplication.mvp.view.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置RecyclerView GridLayoutManager or StaggeredGridLayoutManager spacing
 * Created by john on 17-1-5.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int verticalSpacing;
    private int horizontalSpacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int verticalSpacing, int horizontalSpacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.verticalSpacing = verticalSpacing;
        this.horizontalSpacing = horizontalSpacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = horizontalSpacing - column * horizontalSpacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * horizontalSpacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = verticalSpacing;
            }
            outRect.bottom = verticalSpacing; // item bottom
        } else {
            outRect.left = column * horizontalSpacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = horizontalSpacing - (column + 1) * horizontalSpacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = verticalSpacing; // item top
            }
        }
    }
}