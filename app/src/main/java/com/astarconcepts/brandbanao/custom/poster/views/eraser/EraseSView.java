package com.astarconcepts.brandbanao.custom.poster.views.eraser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.internal.view.SupportMenu;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.utils.ImageUtils;


public class EraseSView extends AppCompatImageView {
    int bitmappx;
    int canvaspx;
    Paint paint = new Paint();
    private Paint bPaint = null;
    private Bitmap bit2;
    private int brushSize;
    private Context context;
    private boolean isRectEnable = false;
    private boolean needToDraw = false;
    private boolean onLeft = false;
    private int screenWidth;
    private Paint shaderPaint = null;

    public EraseSView(Context context2) {
        super(context2);
        initVariables(context2);
    }

    public EraseSView(Context context2, @Nullable AttributeSet attributeSet) {
        super(context2, attributeSet);
        initVariables(context2);
    }

    public EraseSView(Context context2, @Nullable AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        initVariables(context2);
    }

    @SuppressLint("RestrictedApi")
    public void initVariables(Context context2) {
        this.context = context2;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context2).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = displayMetrics.widthPixels;
        this.bitmappx = ImageUtils.dpToPx(context2, 150.0f);
        this.canvaspx = ImageUtils.dpToPx(context2, 75.0f);
        this.paint.setColor(SupportMenu.CATEGORY_MASK);
        Paint paint = this.paint;
        double dpToPx = ImageUtils.dpToPx(getContext(), 1.0f);
        Double.isNaN(dpToPx);
        paint.setStrokeWidth((float) (dpToPx * 1.5d));
        this.bPaint = new Paint();
        this.bPaint.setAntiAlias(true);
        this.bPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.bPaint.setAntiAlias(true);
        this.bPaint.setStyle(Paint.Style.STROKE);
        this.bPaint.setStrokeJoin(Paint.Join.MITER);
        Paint paint2 = this.bPaint;
        double dpToPx2 = ImageUtils.dpToPx(getContext(), 2.0f);
        Double.isNaN(dpToPx2);
        paint2.setStrokeWidth((float) (dpToPx2 * 1.5d));
        this.bit2 = BitmapFactory.decodeResource(context2.getResources(), R.drawable.circle1);
        try {
            this.bit2 = Bitmap.createScaledBitmap(this.bit2, this.bitmappx, this.bitmappx, true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.needToDraw && this.shaderPaint != null) {
            if (this.onLeft) {
                canvas.drawBitmap(StickerRemoveActivity.bgCircleBit, 0.0f, 0.0f, null);
                int i = this.canvaspx;
                canvas.drawCircle((float) i, (float) i, (float) i, this.shaderPaint);
                if (EraseView.MODE == 2 || EraseView.MODE == 3) {
                    int i2 = this.canvaspx;
                    canvas.drawCircle((float) i2, (float) i2, (float) this.brushSize, this.bPaint);
                    int i3 = this.canvaspx;
                    int i4 = this.brushSize;
                    canvas.drawLine((float) (i3 - i4), (float) i3, (float) (i4 + i3), (float) i3, this.paint);
                    int i5 = this.canvaspx;
                    int i6 = this.brushSize;
                    canvas.drawLine((float) i5, (float) (i5 - i6), (float) i5, (float) (i5 + i6), this.paint);
                } else if (this.isRectEnable) {
                    int i7 = this.canvaspx;
                    int i8 = this.brushSize;
                    canvas.drawRect((float) (i7 - i8), (float) (i7 - i8), (float) (i7 + i8), (float) (i7 + i8), this.bPaint);
                } else {
                    int i9 = this.canvaspx;
                    canvas.drawCircle((float) i9, (float) i9, (float) this.brushSize, this.bPaint);
                }
                try {
                    canvas.drawBitmap(this.bit2, 0.0f, 0.0f, null);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                canvas.drawBitmap(StickerRemoveActivity.bgCircleBit, (float) (this.screenWidth - this.bitmappx), 0.0f, null);
                int i10 = this.screenWidth;
                int i11 = this.canvaspx;
                canvas.drawCircle((float) (i10 - i11), (float) i11, (float) i11, this.shaderPaint);
                if (EraseView.MODE == 2 || EraseView.MODE == 3) {
                    int i12 = this.screenWidth;
                    int i13 = this.canvaspx;
                    canvas.drawCircle((float) (i12 - i13), (float) i13, (float) this.brushSize, this.bPaint);
                    int i14 = this.screenWidth;
                    int i15 = this.canvaspx;
                    int i16 = this.brushSize;
                    canvas.drawLine((float) ((i14 - i15) - i16), (float) i15, (float) ((i14 - i15) + i16), (float) i15, this.paint);
                    int i17 = this.screenWidth;
                    int i18 = this.canvaspx;
                    int i19 = this.brushSize;
                    canvas.drawLine((float) (i17 - i18), (float) (i18 - i19), (float) (i17 - i18), (float) (i18 + i19), this.paint);
                } else if (this.isRectEnable) {
                    int i20 = this.screenWidth;
                    int i21 = this.canvaspx;
                    int i22 = this.brushSize;
                    canvas.drawRect((float) ((i20 - i21) - i22), (float) (i21 - i22), (float) ((i20 - i21) + i22), (float) (i21 + i22), this.bPaint);
                } else {
                    int i23 = this.screenWidth;
                    int i24 = this.canvaspx;
                    canvas.drawCircle((float) (i23 - i24), (float) i24, (float) this.brushSize, this.bPaint);
                }
                try {
                    canvas.drawBitmap(this.bit2, (float) (this.screenWidth - this.bitmappx), 0.0f, null);
                } catch (NullPointerException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void updateShaderView(Paint paint, int i, boolean z, boolean z2, boolean z3) {
        this.needToDraw = z;
        this.onLeft = z2;
        this.isRectEnable = z3;
        this.shaderPaint = paint;
        this.brushSize = i;
        invalidate();
    }
}
