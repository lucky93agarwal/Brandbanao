package com.astarconcepts.brandbanao.ui.stickers;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MultiTouchListener implements View.OnTouchListener {

    public boolean isRotateEnabled = true;
    public boolean isRotationEnabled = false;
    public boolean isTranslateEnabled = true;
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;
    GestureDetector f164gd = null;
    private TouchCallbackListener listener = null;
    private int mActivePointerId = -1;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());

    private static float adjustAngle(float f) {
        return f > 180.0f ? f - 360.0f : f < -180.0f ? f + 360.0f : f;
    }

    private static void adjustTranslation(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }

    public MultiTouchListener setOnTouchCallbackListener(TouchCallbackListener touchCallbackListener) {
        this.listener = touchCallbackListener;
        return this;
    }

    public MultiTouchListener enableRotation(boolean z) {
        this.isRotationEnabled = z;
        return this;
    }

    public void move(View view, TransformInfo transformInfo) {
        if (this.isRotationEnabled) {
            view.setRotation(adjustAngle(view.getRotation() + transformInfo.deltaAngle));
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mScaleGestureDetector.onTouchEvent(view, motionEvent);
        FrameLayout relativeLayout = (FrameLayout) view.getParent();
        if (this.f164gd != null) {
            this.f164gd.onTouchEvent(motionEvent);
        }
        if (this.isTranslateEnabled) {
            int action = motionEvent.getAction();
            int actionMasked = motionEvent.getActionMasked() & action;
            int i = 0;
            if (actionMasked != 6) {
                switch (actionMasked) {
                    case 0:
                        if (relativeLayout != null) {
                            relativeLayout.requestDisallowInterceptTouchEvent(true);
                        }
                        if (this.listener != null) {
                            this.listener.onTouchCallback(view);
                        }




                        this.mPrevX = motionEvent.getX();
                        this.mPrevY = motionEvent.getY();
                        this.mActivePointerId = motionEvent.getPointerId(0);
                        break;
                    case 1:
                        this.mActivePointerId = -1;
                        if (this.listener != null) {
                            this.listener.onTouchUpCallback(view);
                        }
                        float rotation = view.getRotation();
                        if (Math.abs(90.0f - Math.abs(rotation)) <= 5.0f) {
                            rotation = rotation > 0.0f ? 90.0f : -90.0f;
                        }
                        if (Math.abs(0.0f - Math.abs(rotation)) <= 5.0f) {
                            rotation = rotation > 0.0f ? 0.0f : -0.0f;
                        }
                        if (Math.abs(180.0f - Math.abs(rotation)) <= 5.0f) {
                            rotation = rotation > 0.0f ? 180.0f : -180.0f;
                        }
                        view.setRotation(rotation);
                        Log.i("testing", "Final Rotation : " + rotation);
                        break;
                    case 2:
                        if (relativeLayout != null) {
                            relativeLayout.requestDisallowInterceptTouchEvent(true);
                        }
                        if (this.listener != null) {
                            this.listener.onTouchMoveCallback(view);
                        }
                        int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex != -1) {
                            float x = motionEvent.getX(findPointerIndex);
                            float y = motionEvent.getY(findPointerIndex);
                            if (!this.mScaleGestureDetector.isInProgress()) {
                                adjustTranslation(view, x - this.mPrevX, y - this.mPrevY);
                                break;
                            }
                        }
                        break;
                    case 3:
                        this.mActivePointerId = -1;
                        break;
                }
            } else {
                int i2 = (65280 & action) >> 8;
                if (motionEvent.getPointerId(i2) == this.mActivePointerId) {
                    if (i2 == 0) {
                        i = 1;
                    }
                    this.mPrevX = motionEvent.getX(i);
                    this.mPrevY = motionEvent.getY(i);
                    this.mActivePointerId = motionEvent.getPointerId(i);
                }
            }
        }
        return true;
    }

    public interface TouchCallbackListener {
        void onTouchCallback(View view);

        void onTouchMoveCallback(View view);

        void onTouchUpCallback(View view);
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D();
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector scaleGestureDetector) {
            this.mPivotX = scaleGestureDetector.getFocusX();
            this.mPivotY = scaleGestureDetector.getFocusY();
            this.mPrevSpanVector.set(scaleGestureDetector.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, ScaleGestureDetector scaleGestureDetector) {
            TransformInfo transformInfo = new TransformInfo();
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
