package com.astarconcepts.brandbanao.custom.poster.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;

public class DrawView extends View {
    private final RectF mPathBounds = new RectF();
    private final PathMeasure measure = new PathMeasure();
    public boolean isTouchable = false;
    public Bitmap mFillCache;
    Paint circlePaint;
    Paint circlePaintOffset;
    int eragerRedius = 20;
    Paint mNullPaint;
    Path mNullPath;
    int ofsetHieght = 120;
    float startX;
    float startY;
    float strokeWidth = 30.0f;
    int touchCircleRedius = 10;
    float xRedius;
    float yRedius;
    private int height;
    private Context mContext;
    private Canvas mFillCanvas;
    private OnScratchCallback mScratchCallback = null;
    private Drawable mScratchSurface;
    private float mViewArea = 0.0f;
    private float mX;
    private float mY;
    private ArrayList<Pair<Path, Paint>> paths = new ArrayList<>();
    private ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<>();
    private int width;

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        setFocusableInTouchMode(true);
        this.mNullPaint = new Paint();
        this.mNullPaint.setAntiAlias(true);
        this.mNullPaint.setColor(0);
        this.mNullPaint.setStyle(Paint.Style.STROKE);
        this.mNullPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mNullPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mNullPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mNullPath = new Path();
        invalidate();
        this.circlePaint = new Paint();
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setColor(Color.parseColor("#009688"));
        this.circlePaintOffset = new Paint();
        this.circlePaintOffset.setColor(Color.parseColor("#FF4081"));
        this.circlePaintOffset.setAntiAlias(true);
        this.circlePaintOffset.setStyle(Paint.Style.STROKE);
        this.circlePaintOffset.setStrokeJoin(Paint.Join.ROUND);
        this.circlePaintOffset.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setNewImage(Drawable drawable) {
        setScratchSurfaceDrawable(drawable);
    }

    public void setScratchSurfaceDrawable(Drawable drawable) {
        if (drawable != null) {
            this.mScratchSurface = drawable;
            this.mScratchSurface.setBounds(getPaddingLeft(), getPaddingTop(), 512 - getPaddingRight(), 512 - getPaddingBottom());
            this.mViewArea = ((float) 262144) + 0.1f;
            postInvalidate();
            return;
        }
        throw new NullPointerException("Scratch Surface can not be null");
    }

    public void setScratchWidth(float f) {
        this.strokeWidth = f;
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        Drawable drawable = this.mScratchSurface;
        if (drawable != null) {
            drawable.setBounds(5, 5, 512 - getPaddingRight(), 512 - getPaddingBottom());
        }
        this.mViewArea = (float) 262144;
        if (this.mFillCanvas != null) {
            this.mFillCache.recycle();
        }
        this.mFillCache = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
        this.mFillCanvas = new Canvas(this.mFillCache);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.isTouchable) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY() - ((float) this.ofsetHieght);
        Log.e("drawX==" + x, "      drawY==" + y);
        int action = motionEvent.getAction();
        if (action == 0) {
            touch_start(x, y);
            invalidate();
        } else if (action == 1) {
            touch_up();
            invalidate();
        } else if (action != 2) {
            Log.e("d", "Wrong element choosen: " + motionEvent.getAction());
        } else {
            this.xRedius = motionEvent.getX();
            this.yRedius = motionEvent.getY();
            touch_move(x, y);
            this.mNullPaint.setMaskFilter(new BlurMaskFilter(5.0f, BlurMaskFilter.Blur.NORMAL));
            invalidate();
        }
        OnScratchCallback onScratchCallback = this.mScratchCallback;
        if (onScratchCallback != null) {
            this.mNullPath.computeBounds(this.mPathBounds, false);
            float width2 = this.mPathBounds.width() * this.mPathBounds.height();
            this.measure.setPath(this.mNullPath, false);
            onScratchCallback.onScratch(width2 / this.mViewArea, (this.measure.getLength() * this.mNullPaint.getStrokeWidth()) / width2);
        }
        return true;
    }

    private void touch_start(float f, float f2) {
        this.undonePaths.clear();
        this.mNullPaint.setColor(0);
        this.mNullPaint.setStrokeWidth(this.strokeWidth);
        if (!(this.paths.size() == 0 && this.mFillCache == null)) {
            this.paths.add(new Pair(this.mNullPath, new Paint(this.mNullPaint)));
        }
        this.mNullPath.reset();
        this.mNullPath.moveTo(f, f2);
        this.mX = f;
        this.mY = f2;
    }

    private void touch_move(float f, float f2) {
        Path path = this.mNullPath;
        float f3 = this.mX;
        float f4 = this.mY;
        path.quadTo(f3, f4, (f + f3) / 2.0f, (f2 + f4) / 2.0f);
        this.mX = f;
        this.mY = f2;
    }

    private void touch_up() {
        this.mNullPath.lineTo(this.mX, this.mY);
        if (!(this.paths.size() == 0 && this.mFillCache == null)) {
            this.paths.add(new Pair(this.mNullPath, new Paint(this.mNullPaint)));
        }
        this.mNullPath = new Path();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mFillCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        Drawable drawable = this.mScratchSurface;
        if (drawable != null) {
            drawable.draw(this.mFillCanvas);
        }
        Bitmap bitmap = this.mFillCache;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        }
        Iterator<Pair<Path, Paint>> it = this.paths.iterator();
        while (it.hasNext()) {
            Pair next = it.next();
            this.mFillCanvas.drawPath((Path) next.first, (Paint) next.second);
        }
        canvas.drawCircle(this.xRedius, this.yRedius, (float) this.touchCircleRedius, this.circlePaint);
        canvas.drawCircle(this.xRedius, this.yRedius - ((float) this.ofsetHieght), (float) this.eragerRedius, this.circlePaintOffset);
    }

    public Bitmap getBitmapFromView(View view) {
        Drawable background = view.getBackground();
        if (background != null) {
            background.draw(this.mFillCanvas);
        } else {
            view.draw(this.mFillCanvas);
        }
        return this.mFillCache;
    }

    public void onClickUndo() {
        if (this.paths.size() > 0) {
            ArrayList<Pair<Path, Paint>> arrayList = this.undonePaths;
            ArrayList<Pair<Path, Paint>> arrayList2 = this.paths;
            arrayList.add(arrayList2.remove(arrayList2.size() - 1));
            ArrayList<Pair<Path, Paint>> arrayList3 = this.undonePaths;
            ArrayList<Pair<Path, Paint>> arrayList4 = this.paths;
            arrayList3.add(arrayList4.remove(arrayList4.size() - 1));
            invalidate();
        }
    }

    public void onClickRedo() {
        if (this.undonePaths.size() > 0) {
            ArrayList<Pair<Path, Paint>> arrayList = this.paths;
            ArrayList<Pair<Path, Paint>> arrayList2 = this.undonePaths;
            arrayList.add(arrayList2.remove(arrayList2.size() - 1));
            ArrayList<Pair<Path, Paint>> arrayList3 = this.paths;
            ArrayList<Pair<Path, Paint>> arrayList4 = this.undonePaths;
            arrayList3.add(arrayList4.remove(arrayList4.size() - 1));
            invalidate();
        }
    }

    public void setOfsetSize(int i) {
        this.eragerRedius = i - 10;
        invalidate();
    }

    public void setOfsetHieght(int i) {
        this.ofsetHieght = i;
        invalidate();
    }

    public void onClickOkHide() {
        this.touchCircleRedius = 0;
        this.eragerRedius = 0;
        invalidate();
    }

    public void onClickBack() {
        this.touchCircleRedius = 10;
        this.eragerRedius = 20;
        this.strokeWidth = 30.0f;
        invalidate();
    }

    public void onClickErageSelected(boolean z) {
        if (z) {
            this.touchCircleRedius = 10;
            this.eragerRedius = 20;
            return;
        }
        this.touchCircleRedius = 0;
        this.eragerRedius = 0;
    }

    public interface OnScratchCallback {
        void onScratch(float f, float f2);
    }
}
