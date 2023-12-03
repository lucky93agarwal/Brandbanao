package com.astarconcepts.brandbanao.custom.poster.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.internal.view.SupportMenu;

import com.astarconcepts.brandbanao.utils.ImageUtils;

import java.util.Arrays;


public class GridLine extends AppCompatImageView {
    private final float[] bounds = new float[8];
    public Matrix matrix = new Matrix();
    boolean isInCenterX = false;
    boolean isInCenterY = false;
    boolean isInRotate = false;
    private Paint paint;
    private Paint paintLine;
    private Paint paintLine1;
    private Paint rotatePaint;
    private View rotation;

    public GridLine(Context context) {
        super(context);
        init(context);
    }

    public GridLine(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public GridLine(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    @Override
    public Matrix getMatrix() {
        return this.matrix;
    }

    public void setMatrix(Matrix matrix2) {
        this.matrix = matrix2;
    }

    @SuppressLint("RestrictedApi")
    private void init(Context context) {
        this.paint = new Paint();
        this.paint.setColor(-1);
        this.paint.setStrokeWidth((float) ImageUtils.dpToPx(context, 2.0f));
        this.paint.setPathEffect(new DashPathEffect(new float[]{5.0f, 5.0f}, 1.0f));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paintLine = new Paint();
        this.paintLine.setAntiAlias(true);
        this.paintLine.setStrokeWidth(3.2f);
        this.paintLine.setColor(SupportMenu.CATEGORY_MASK);
        this.paintLine.setAlpha(255);
        this.paintLine.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paintLine.setAntiAlias(true);
        this.paintLine1 = new Paint();
        this.paintLine1.setAlpha(255);
        this.paintLine1.setStrokeWidth(2.0f);
        this.paintLine1.setColor(Color.argb(50, 74, 255, 255));
        this.paintLine1.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paintLine1.setPathEffect(new DashPathEffect(new float[]{10.0f, 5.0f}, 0.0f));
        this.rotatePaint = new Paint();
        this.rotatePaint.setAlpha(255);
        this.rotatePaint.setStrokeWidth(2.0f);
        this.rotatePaint.setColor(SupportMenu.CATEGORY_MASK);
        this.rotatePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.rotatePaint.setPathEffect(new DashPathEffect(new float[]{10.0f, 5.0f}, 0.0f));
    }

    public View getViewRotation() {
        return this.rotation;
    }

    public void setViewRotation(View view) {
        this.rotation = view;
    }

    public void getBoundPoints(@NonNull float[] fArr) {
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        fArr[2] = (float) getViewRotation().getWidth();
        fArr[3] = 0.0f;
        fArr[4] = 0.0f;
        fArr[5] = (float) getViewRotation().getHeight();
        fArr[6] = (float) getViewRotation().getWidth();
        fArr[7] = (float) getViewRotation().getHeight();
    }

    public void getStickerPoints(@Nullable View view, @NonNull float[] fArr) {
        if (view == null) {
            Arrays.fill(fArr, 0.0f);
            return;
        }
        getBoundPoints(this.bounds);
        getMappedPoints(fArr, this.bounds);
    }

    public void getMappedPoints(@NonNull float[] fArr, @NonNull float[] fArr2) {
        this.matrix.mapPoints(fArr, fArr2);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getWidth();
        canvas.getHeight();
        drawLines(canvas);
    }

    private void drawLines(Canvas canvas) {
        int i = 0;
        if (this.isInCenterX) {
            canvas.drawLine((float) (canvas.getWidth() / 2), 0.0f, (float) (canvas.getWidth() / 2), (float) canvas.getHeight(), this.paintLine);
            this.isInCenterX = false;
        }
        if (this.isInCenterY) {
            canvas.drawLine(0.0f, (float) (canvas.getHeight() / 2), (float) canvas.getWidth(), (float) (canvas.getHeight() / 2), this.paintLine);
            this.isInCenterY = false;
        }
        float width = ((float) canvas.getWidth()) / 10.0f;
        float height = ((float) canvas.getHeight()) / 10.0f;
        int i2 = 0;
        while (true) {
            float f = (float) i2;
            if (f > 10.0f) {
                break;
            }
            float f2 = f * width;
            canvas.drawLine(f2, 0.0f, f2, (float) canvas.getHeight(), this.paintLine1);
            i2++;
        }
        while (true) {
            float f3 = (float) i;
            if (f3 <= 10.0f) {
                float f4 = f3 * height;
                canvas.drawLine(0.0f, f4, (float) canvas.getWidth(), f4, this.paintLine1);
                i++;
            } else {
                return;
            }
        }
    }

    public boolean isInRotate() {
        return this.isInRotate;
    }

    public void setInRotate(boolean z) {
        this.isInRotate = z;
    }

    public void setCenterValues(boolean z, boolean z2) {
        this.isInCenterX = z;
        this.isInCenterY = z2;
        invalidate();
    }

    private void drawLine(Canvas canvas, int i, int i2, int i3, int i4, Paint paint2) {
        Path path = new Path();
        path.moveTo((float) i, (float) i2);
        path.lineTo((float) i3, (float) i4);
        canvas.drawPath(path, paint2);
    }
}
