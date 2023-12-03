package com.astarconcepts.brandbanao.custom.poster.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CustomSquareFrameLayout extends FrameLayout {
    public CustomSquareFrameLayout(Context context) {
        super(context);
    }

    public CustomSquareFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(i, i);
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i, i3, i4);
    }
}
