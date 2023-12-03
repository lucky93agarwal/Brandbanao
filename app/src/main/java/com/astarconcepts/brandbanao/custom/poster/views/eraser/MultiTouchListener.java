package com.astarconcepts.brandbanao.custom.poster.views.eraser;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.astarconcepts.brandbanao.custom.poster.views.text.AutofitTextRel;


public class MultiTouchListener implements View.OnTouchListener {

    Bitmap bitmap;

    boolean f1307bt = false;
    GestureDetector f1308gd = null;
    private boolean disContinueHandleTransparecy = true;
    private boolean handleTransparecy = false;
    private boolean isRotateEnabled = true;
    private boolean isRotationEnabled = false;
    private boolean isScaleEnabled = true;
    private boolean isTranslateEnabled = true;
    private TouchCallbackListener listener = null;
    private int mActivePointerId = -1;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
    private float maximumScale = 8.0f;
    private float minimumScale = 0.5f;

    private static float adjustAngle(float f) {
        return f > 180.0f ? f - 360.0f : f < -180.0f ? f + 360.0f : f;
    }

    private static void adjustTranslation(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }

    private static void computeRenderOffset(View view, float f, float f2) {
        if (view.getPivotX() != f || view.getPivotY() != f2) {
            float[] fArr = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr);
            view.setPivotX(f);
            view.setPivotY(f2);
            float[] fArr2 = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr2);
            float f3 = fArr2[1] - fArr[1];
            view.setTranslationX(view.getTranslationX() - (fArr2[0] - fArr[0]));
            view.setTranslationY(view.getTranslationY() - f3);
        }
    }

    public MultiTouchListener setHandleTransparecy(boolean z) {
        this.handleTransparecy = z;
        return this;
    }

    public MultiTouchListener setGestureListener(GestureDetector gestureDetector) {
        this.f1308gd = gestureDetector;
        return this;
    }

    public MultiTouchListener setOnTouchCallbackListener(TouchCallbackListener touchCallbackListener) {
        this.listener = touchCallbackListener;
        return this;
    }

    public MultiTouchListener enableRotation(boolean z) {
        this.isRotationEnabled = z;
        return this;
    }

    public MultiTouchListener setMinScale(float f) {
        this.minimumScale = f;
        return this;
    }

    public void move(View view, TransformInfo transformInfo) {
        computeRenderOffset(view, transformInfo.pivotX, transformInfo.pivotY);
        adjustTranslation(view, transformInfo.deltaX, transformInfo.deltaY);
        float max = Math.max(transformInfo.minimumScale, Math.min(transformInfo.maximumScale, view.getScaleX() * transformInfo.deltaScale));
        view.setScaleX(max);
        view.setScaleY(max);
        if (this.isRotationEnabled) {
            view.setRotation(adjustAngle(view.getRotation() + transformInfo.deltaAngle));
        }
    }

    public boolean handleTransparency(View view, MotionEvent motionEvent) {
        try {
            boolean z = true;
            if (motionEvent.getAction() == 2) {
                Log.i("MOVE_TESTs", "ACTION_MOVE");
                if (this.f1307bt) {
                    return true;
                }
            }
            if (motionEvent.getAction() == 1) {
                Log.i("MOVE_TESTs", "ACTION_UP");
                if (this.f1307bt) {
                    this.f1307bt = false;
                    if (this.bitmap != null) {
                        this.bitmap.recycle();
                    }
                    return true;
                }
            }
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int rawY = (int) (motionEvent.getRawY() - ((float) iArr[1]));
            float rotation = view.getRotation();
            Matrix matrix = new Matrix();
            matrix.postRotate(-rotation);
            float[] fArr = {(float) ((int) (motionEvent.getRawX() - ((float) iArr[0]))), (float) rawY};
            matrix.mapPoints(fArr);
            int i = (int) fArr[0];
            int i2 = (int) fArr[1];
            if (motionEvent.getAction() == 0) {
                Log.i("MOVE_TESTs", "View Width/height " + view.getWidth() + " / " + view.getHeight());
                this.f1307bt = false;
                view.setDrawingCacheEnabled(true);
                this.bitmap = Bitmap.createBitmap(view.getDrawingCache());
                i = (int) (((float) i) * (((float) this.bitmap.getWidth()) / (((float) this.bitmap.getWidth()) * view.getScaleX())));
                i2 = (int) (((float) i2) * (((float) this.bitmap.getHeight()) / (((float) this.bitmap.getHeight()) * view.getScaleX())));
                view.setDrawingCacheEnabled(false);
            }
            if (i >= 0 && i2 >= 0 && i <= this.bitmap.getWidth()) {
                if (i2 <= this.bitmap.getHeight()) {
                    if (this.bitmap.getPixel(i, i2) != 0) {
                        z = false;
                    }
                    if (motionEvent.getAction() != 0) {
                        return z;
                    }
                    this.f1307bt = z;
                    return z;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mScaleGestureDetector.onTouchEvent(view, motionEvent);
        int i = 0;
        if (this.handleTransparecy && this.disContinueHandleTransparecy) {
            if (handleTransparency(view, motionEvent)) {
                return false;
            }
            this.disContinueHandleTransparecy = false;
        }
        GestureDetector gestureDetector = this.f1308gd;
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(motionEvent);
        }
        if (!this.isTranslateEnabled) {
            return true;
        }
        int action = motionEvent.getAction();
        int actionMasked = motionEvent.getActionMasked() & action;
        if (actionMasked == 0) {
            TouchCallbackListener touchCallbackListener = this.listener;
            if (touchCallbackListener != null) {
                touchCallbackListener.onTouchCallback(view);
            }
            if (view instanceof AutofitTextRel) {
                ((AutofitTextRel) view).setBorderVisibility(true);
            }
            this.mPrevX = motionEvent.getX();
            this.mPrevY = motionEvent.getY();
            this.mActivePointerId = motionEvent.getPointerId(0);
            return true;
        } else if (actionMasked == 1) {
            this.mActivePointerId = -1;
            this.disContinueHandleTransparecy = true;
            TouchCallbackListener touchCallbackListener2 = this.listener;
            if (touchCallbackListener2 == null) {
                return true;
            }
            touchCallbackListener2.onTouchUpCallback(view);
            return true;
        } else if (actionMasked == 2) {
            int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
            if (findPointerIndex == -1) {
                return true;
            }
            float x = motionEvent.getX(findPointerIndex);
            float y = motionEvent.getY(findPointerIndex);
            if (this.mScaleGestureDetector.isInProgress()) {
                return true;
            }
            adjustTranslation(view, x - this.mPrevX, y - this.mPrevY);
            return true;
        } else if (actionMasked == 3) {
            this.mActivePointerId = -1;
            return true;
        } else if (actionMasked != 6) {
            return true;
        } else {
            int i2 = (65280 & action) >> 8;
            if (motionEvent.getPointerId(i2) != this.mActivePointerId) {
                return true;
            }
            if (i2 == 0) {
                i = 1;
            }
            this.mPrevX = motionEvent.getX(i);
            this.mPrevY = motionEvent.getY(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            return true;
        }
    }

    public interface TouchCallbackListener {
        void onTouchCallback(View view);

        void onTouchUpCallback(View view);
    }

    private static class TransformInfo {
        private float deltaAngle;
        private float deltaScale;
        private float deltaX;
        private float deltaY;
        private float maximumScale;
        private float minimumScale;
        private float pivotX;
        private float pivotY;

        private TransformInfo() {
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private final Vector2D mPrevSpanVector;
        private float mPivotX;
        private float mPivotY;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D();
        }

        @Override
        public boolean onScaleBegin(View view, ScaleGestureDetector scaleGestureDetector) {
            this.mPivotX = scaleGestureDetector.getFocusX();
            this.mPivotY = scaleGestureDetector.getFocusY();
            this.mPrevSpanVector.set(scaleGestureDetector.getCurrentSpanVector());
            return true;
        }

        @Override
        public boolean onScale(View view, ScaleGestureDetector scaleGestureDetector) {
            TransformInfo transformInfo = new TransformInfo();
            transformInfo.deltaScale = MultiTouchListener.this.isScaleEnabled ? scaleGestureDetector.getScaleFactor() : 1.0f;
            float f = 0.0f;
            transformInfo.deltaAngle = MultiTouchListener.this.isRotateEnabled ? Vector2D.getAngle(this.mPrevSpanVector, scaleGestureDetector.getCurrentSpanVector()) : 0.0f;
            transformInfo.deltaX = MultiTouchListener.this.isTranslateEnabled ? scaleGestureDetector.getFocusX() - this.mPivotX : 0.0f;
            if (MultiTouchListener.this.isTranslateEnabled) {
                f = scaleGestureDetector.getFocusY() - this.mPivotY;
            }
            transformInfo.deltaY = f;
            transformInfo.pivotX = this.mPivotX;
            transformInfo.pivotY = this.mPivotY;
            transformInfo.minimumScale = MultiTouchListener.this.minimumScale;
            transformInfo.maximumScale = MultiTouchListener.this.maximumScale;
            MultiTouchListener.this.move(view, transformInfo);
            return false;
        }
    }
}
