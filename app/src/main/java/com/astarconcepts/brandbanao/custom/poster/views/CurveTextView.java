package com.astarconcepts.brandbanao.custom.poster.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;

import com.astarconcepts.brandbanao.custom.poster.activity.ThumbnailActivity;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;


public class CurveTextView extends View {
    private Paint mPaintText;
    private String message = "Android Developer";
    private Path textPath;

    public CurveTextView(Context context) {
        super(context);
    }


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = 21)
    @Override
    public void onDraw(Canvas canvas) {
        float f;
        int i;
        super.onDraw(canvas);
        this.textPath = new Path();
        Paint paint = new Paint();
        Paint paint2 = new Paint();
        float width = ((float) getWidth()) * 1.0f;
        paint.setAntiAlias(true);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setTextSize(50.0f);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader((Shader) null);
        paint2.setAntiAlias(true);
        paint2.setTextSize(paint.getTextSize());
        paint2.setTypeface(paint.getTypeface());
        paint2.setTextAlign(paint.getTextAlign());
        paint2.setColor(SupportMenu.CATEGORY_MASK);
        paint2.setStrokeWidth(2.0f);
        String replace = this.message.replace("\n", " ");
        int i2 = ThumbnailActivity.mRadius + 6;
        if (ThumbnailActivity.mRadius >= 360) {
            i2 = 359;
        } else if (ThumbnailActivity.mRadius <= -360) {
            i2 = -359;
        }
        float measureText = paint.measureText(replace) + (((float) (replace.length() - 1)) * (20.0f / ((float) (replace.length() - 1))));
        if (paint2.getStrokeWidth() > 0.0f) {
            measureText += paint2.getStrokeWidth() * 2.0f;
            f = paint2.getStrokeWidth() + 0.0f;
        } else {
            f = 0.0f;
        }
        double abs = (double) ((measureText * 360.0f) / ((float) Math.abs(i2)));
        Double.isNaN(abs);
        float f2 = (float) (abs / 3.141592653589793d);
        float width2 = ((float) (getWidth() - 1)) * 0.5f * 1.0f;
        float height = ((float) (getHeight() - 1)) * 0.5f * 1.0f;
        float f3 = ((float) (((int) width2) + 0)) + (((width - (width2 * 2.0f)) - f2) / 2.0f);
        float f4 = (float) (((int) height) + 0);
        if (i2 > 0) {
            i = VerticalSeekBar.ROTATION_ANGLE_CW_270;
        } else {
            f4 += (((((float) getHeight()) * 1.0f) - (height * 2.0f)) - f2) - 10.0f;
            if (paint2.getStrokeWidth() > 0.0f) {
                f4 -= paint2.getStrokeWidth();
            }
            i = 90;
        }
        this.textPath.reset();
        Paint paint3 = new Paint();
        paint3.setColor(0);
        float f5 = f3 + f2;
        float f6 = f2 + f4;
        canvas.drawRect(new RectF(f3, f4, f5, f6), paint3);
        this.textPath.addArc(new RectF(f3, f4, f5, f6), (float) (i - (i2 / 2)), (float) i2);
        canvas.drawTextOnPath(replace, this.textPath, f, 0.0f, paint);
    }
}
