package com.android.shop.shopapp.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.shop.shopapp.R;


/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration
{

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private final boolean mShowLastItemDivider;

    private Drawable mDivider;

    private int mOrientation;
    private int mDividerSize;

    public DividerItemDecoration(Context context, int orientation, boolean showLastItemDivider)
    {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
        mDividerSize = (int) context.getResources().getDimension(R.dimen.cdl_horizontal_stroke_height);
        if (mDividerSize < 1)
        {
            // ldpi == 0.5 so we make sure we set a minimum
            mDividerSize = 1;
        }
        mShowLastItemDivider = showLastItemDivider;
    }

    public DividerItemDecoration setDividerSize(int dividerSize)
    {
        this.mDividerSize = dividerSize;
        return this;
    }

    public void setOrientation(int orientation)
    {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)
        {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        if (mOrientation == VERTICAL_LIST)
        {
            drawVertical(c, parent);
        }
        else
        {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent)
    {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount() - ((mShowLastItemDivider) ? 0 : 1);
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent)
    {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount() - ((mShowLastItemDivider) ? 0 : 1);
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        if (!mShowLastItemDivider)
        {
            final int itemPosition = parent.getChildAdapterPosition(view);
            if (itemPosition == RecyclerView.NO_POSITION)
            {
                return;
            }

            final int itemCount = state.getItemCount();

            if (itemPosition == itemCount - 1)
            {
                return;
            }
        }
        if (mOrientation == VERTICAL_LIST)
        {
            outRect.set(0, 0, 0, mDividerSize);
        }
        else
        {
            outRect.set(0, 0, mDividerSize, 0);
        }
    }
}