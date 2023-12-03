package com.astarconcepts.brandbanao.custom.poster.views.eraser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.internal.view.SupportMenu;

import com.astarconcepts.brandbanao.utils.ImageUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;


public class EraseView extends AppCompatImageView implements View.OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    public static int MODE = 1;
    public static float scale = 1.0f;
    private static int pc = 0;
    public int TARGET = 2;
    public int TOLERANCE = 30;
    public ActionListener actionListener;
    public ArrayList<Integer> brushIndx = new ArrayList<>();
    public int brushSize = 18;
    public ArrayList<Boolean> brushTypeIndx = new ArrayList<>();
    public ArrayList<Path> changesIndx = new ArrayList<>();
    public int curIndx = -1;
    public boolean insidCutEnable = true;
    public boolean isAutoRunning = false;
    public boolean isRectBrushEnable = false;
    public boolean isRotateEnabled = true;
    public boolean isScaleEnabled = true;
    public boolean isTranslateEnabled = true;
    public ArrayList<Boolean> lassoIndx = new ArrayList<>();
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;
    public ArrayList<Integer> modeIndx = new ArrayList<>();
    public ProgressDialog pd = null;
    public Point point;
    public UndoRedoListener undoRedoListener;
    public boolean updateOnly = false;
    public ArrayList<Vector<Point>> vectorPoints = new ArrayList<>();
    Bitmap Bitmap2 = null;
    Bitmap Bitmap3 = null;
    Bitmap Bitmap4 = null;
    float f1298X = 100.0f;
    float f1299Y = 100.0f;
    Canvas c2;
    Context ctx;
    Path drawPath = new Path();
    Paint erPaint = new Paint();
    Paint erPaint1 = new Paint();
    int erps = ImageUtils.dpToPx(getContext(), 2.0f);
    int height;
    boolean isMoved = false;
    Path lPath = new Path();
    Paint p = new Paint();
    Paint paint = new Paint();
    BitmapShader patternBMPshader;
    float sX;
    float sY;
    Path tPath = new Path();
    int width;
    private int ERASE = 1;
    private int LASSO = 3;
    private int NONE = 0;
    private int REDRAW = 4;
    private int brushSize1 = 18;
    private boolean drawLasso = false;
    private boolean drawOnLasso = true;
    private EraseSView eraseSView = null;
    private boolean isNewPath = false;
    private boolean isSelected = true;
    private boolean isTouched = false;
    private ScaleGestureDetector mScaleGestureDetector;
    private int offset = 200;
    private int offset1 = 200;
    private boolean onLeft = true;
    private Bitmap orgBit;
    private int screenWidth;
    private int targetBrushSize = 18;
    private int targetBrushSize1 = 18;

    public EraseView(Context context) {
        super(context);
        initPaint(context);
    }

    public EraseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initPaint(context);
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

    public int getIndex(int i, int i2, int i3) {
        return i2 == 0 ? i : i + ((i2 - 1) * i3);
    }

    public float updatebrushsize(int i, float f) {
        return ((float) i) / f;
    }

    public void setUndoRedoListener(UndoRedoListener undoRedoListener2) {
        this.undoRedoListener = undoRedoListener2;
    }

    public void setActionListener(ActionListener actionListener2) {
        this.actionListener = actionListener2;
    }

    private void initPaint(Context context) {
        MODE = 1;
        this.mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
        this.ctx = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = displayMetrics.widthPixels;
        this.brushSize = ImageUtils.dpToPx(getContext(), (float) this.brushSize);
        this.brushSize1 = ImageUtils.dpToPx(getContext(), (float) this.brushSize);
        this.targetBrushSize = ImageUtils.dpToPx(getContext(), 50.0f);
        this.targetBrushSize1 = ImageUtils.dpToPx(getContext(), 50.0f);
        this.paint.setAlpha(0);
        this.paint.setColor(0);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(updatebrushsize(this.brushSize1, scale));
        this.erPaint = new Paint();
        this.erPaint.setAntiAlias(true);
        this.erPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.erPaint.setAntiAlias(true);
        this.erPaint.setStyle(Paint.Style.STROKE);
        this.erPaint.setStrokeJoin(Paint.Join.MITER);
        this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
        this.erPaint1 = new Paint();
        this.erPaint1.setAntiAlias(true);
        this.erPaint1.setColor(SupportMenu.CATEGORY_MASK);
        this.erPaint1.setAntiAlias(true);
        this.erPaint1.setStyle(Paint.Style.STROKE);
        this.erPaint1.setStrokeJoin(Paint.Join.MITER);
        this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, scale));
        this.erPaint1.setPathEffect(new DashPathEffect(new float[]{10.0f, 20.0f}, 0.0f));
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            if (this.orgBit == null) {
                this.orgBit = bitmap.copy(bitmap.getConfig(), true);
            }
            this.width = bitmap.getWidth();
            this.height = bitmap.getHeight();
            this.Bitmap2 = Bitmap.createBitmap(this.width, this.height, bitmap.getConfig());
            this.c2 = new Canvas();
            this.c2.setBitmap(this.Bitmap2);
            this.c2.drawBitmap(bitmap, 0.0f, 0.0f, null);
            boolean z = this.isSelected;
            if (z) {
                enableTouchClear(z);
            }
            super.setImageBitmap(this.Bitmap2);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.c2 != null) {
            if (!this.updateOnly && this.isTouched) {
                this.paint = getPaintByMode(MODE, this.brushSize, this.isRectBrushEnable);
                Path path = this.tPath;
                if (path != null) {
                    this.c2.drawPath(path, this.paint);
                }
                this.isTouched = false;
            }
            if (MODE == this.TARGET) {
                this.p = new Paint();
                this.p.setColor(SupportMenu.CATEGORY_MASK);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
                canvas.drawCircle(this.f1298X, this.f1299Y, (float) (this.targetBrushSize / 2), this.erPaint);
                canvas.drawCircle(this.f1298X, this.f1299Y + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7.0f), scale), this.p);
                this.p.setStrokeWidth(updatebrushsize(ImageUtils.dpToPx(getContext(), 1.0f), scale));
                float f = this.f1298X;
                int i = this.targetBrushSize;
                float f2 = f - ((float) (i / 2));
                float f3 = this.f1299Y;
                canvas.drawLine(f2, f3, ((float) (i / 2)) + f, f3, this.p);
                float f4 = this.f1298X;
                float f5 = this.f1299Y;
                int i2 = this.targetBrushSize;
                canvas.drawLine(f4, f5 - ((float) (i2 / 2)), f4, ((float) (i2 / 2)) + f5, this.p);
                this.drawOnLasso = true;
            }
            if (MODE == this.LASSO) {
                this.p = new Paint();
                this.p.setColor(SupportMenu.CATEGORY_MASK);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
                canvas.drawCircle(this.f1298X, this.f1299Y, (float) (this.targetBrushSize / 2), this.erPaint);
                canvas.drawCircle(this.f1298X, this.f1299Y + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7.0f), scale), this.p);
                this.p.setStrokeWidth(updatebrushsize(ImageUtils.dpToPx(getContext(), 1.0f), scale));
                float f6 = this.f1298X;
                int i3 = this.targetBrushSize;
                float f7 = f6 - ((float) (i3 / 2));
                float f8 = this.f1299Y;
                canvas.drawLine(f7, f8, ((float) (i3 / 2)) + f6, f8, this.p);
                float f9 = this.f1298X;
                float f10 = this.f1299Y;
                int i4 = this.targetBrushSize;
                canvas.drawLine(f9, f10 - ((float) (i4 / 2)), f9, ((float) (i4 / 2)) + f10, this.p);
                if (!this.drawOnLasso) {
                    this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, scale));
                    canvas.drawPath(this.lPath, this.erPaint1);
                }
            }
            int i5 = MODE;
            if (i5 == this.ERASE || i5 == this.REDRAW) {
                this.p = new Paint();
                this.p.setColor(SupportMenu.CATEGORY_MASK);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
                if (this.isRectBrushEnable) {
                    float f11 = this.f1298X;
                    float f12 = (float) (this.brushSize / 2);
                    float f13 = this.f1299Y;
                    canvas.drawRect(f11 - f12, f13 - f12, f12 + f11, f12 + f13, this.erPaint);
                } else {
                    canvas.drawCircle(this.f1298X, this.f1299Y, (float) (this.brushSize / 2), this.erPaint);
                }
                canvas.drawCircle(this.f1298X, this.f1299Y + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7.0f), scale), this.p);
            }
            this.updateOnly = false;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (motionEvent.getPointerCount() == 1) {
            ActionListener actionListener2 = this.actionListener;
            if (actionListener2 != null) {
                actionListener2.onAction(motionEvent.getAction());
            }
            if (MODE == this.TARGET) {
                this.drawOnLasso = false;
                this.f1298X = motionEvent.getX();
                this.f1299Y = motionEvent.getY() - ((float) this.offset);
                updateShader1(this.f1298X, this.f1299Y, motionEvent.getRawX(), motionEvent.getRawY(), true);
                if (action == 0) {
                    Log.e("TARGET ACTION_DOWN", "=" + motionEvent.getAction());
                    invalidate();
                } else if (action == 1) {
                    Log.e("TARGET ACTION_UP", "=" + motionEvent.getAction());
                    float f = this.f1298X;
                    if (f >= 0.0f && this.f1299Y >= 0.0f && f < ((float) this.Bitmap2.getWidth()) && this.f1299Y < ((float) this.Bitmap2.getHeight())) {
                        this.point = new Point((int) this.f1298X, (int) this.f1299Y);
                        pc = this.Bitmap2.getPixel((int) this.f1298X, (int) this.f1299Y);
                        if (!this.isAutoRunning) {
                            this.isAutoRunning = true;
                            new AsyncTaskRunner(pc).execute(new Void[0]);
                        }
                    }
                    updateShader1(this.f1298X, this.f1299Y, motionEvent.getRawX(), motionEvent.getRawY(), false);
                    invalidate();
                } else if (action == 2) {
                    Log.e("TARGET ACTION_MOVE", "=" + motionEvent.getAction());
                    invalidate();
                }
            }
            if (MODE == this.LASSO) {
                this.f1298X = motionEvent.getX();
                this.f1299Y = motionEvent.getY() - ((float) this.offset);
                if (action == 0) {
                    Log.e("LASSO ACTION_DOWN", "=" + motionEvent.getAction());
                    this.isNewPath = true;
                    this.drawOnLasso = false;
                    this.sX = this.f1298X;
                    this.sY = this.f1299Y;
                    this.lPath = new Path();
                    this.lPath.moveTo(this.f1298X, this.f1299Y);
                    updateShader1(this.f1298X, this.f1299Y, motionEvent.getRawX(), motionEvent.getRawY(), true);
                    invalidate();
                } else if (action == 1) {
                    Log.e("LASSO ACTION_UP", "=" + motionEvent.getAction());
                    this.lPath.lineTo(this.f1298X, this.f1299Y);
                    this.lPath.lineTo(this.sX, this.sY);
                    this.drawLasso = true;
                    updateShader1(this.f1298X, this.f1299Y, motionEvent.getRawX(), motionEvent.getRawY(), false);
                    invalidate();
                    ActionListener actionListener3 = this.actionListener;
                    if (actionListener3 != null) {
                        actionListener3.onActionCompleted(5);
                    }
                } else if (action != 2) {
                    return false;
                } else {
                    Log.e("LASSO ACTION_MOVE", "=" + motionEvent.getAction());
                    this.lPath.lineTo(this.f1298X, this.f1299Y);
                    updateShader1(this.f1298X, this.f1299Y, motionEvent.getRawX(), motionEvent.getRawY(), true);
                    invalidate();
                }
            }
            int i = MODE;
            if (i == this.ERASE || i == this.REDRAW) {
                int i2 = this.brushSize / 2;
                this.f1298X = motionEvent.getX();
                this.f1299Y = motionEvent.getY() - ((float) this.offset);
                this.isTouched = true;
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
                updateShader(this.f1298X, this.f1299Y, motionEvent.getRawX(), motionEvent.getRawY(), true);
                if (action == 0) {
                    Log.e("ERASE ACTION_DOWN", "=" + motionEvent.getAction());
                    this.paint.setStrokeWidth((float) this.brushSize);
                    this.tPath = new Path();
                    if (this.isRectBrushEnable) {
                        Path path = this.tPath;
                        float f2 = this.f1298X;
                        float f3 = (float) i2;
                        float f4 = this.f1299Y;
                        path.addRect(f2 - f3, f4 - f3, f2 + f3, f4 + f3, Path.Direction.CW);
                    } else {
                        this.tPath.moveTo(this.f1298X, this.f1299Y);
                    }
                    invalidate();
                } else if (action == 1) {
                    Log.e("ERASE ACTION_UP", "=" + motionEvent.getAction());
                    updateShader(this.f1298X, this.f1299Y, motionEvent.getRawX(), motionEvent.getRawY(), false);
                    Path path2 = this.tPath;
                    if (path2 != null) {
                        if (this.isRectBrushEnable) {
                            float f5 = this.f1298X;
                            float f6 = (float) i2;
                            float f7 = this.f1299Y;
                            path2.addRect(f5 - f6, f7 - f6, f5 + f6, f7 + f6, Path.Direction.CW);
                        } else {
                            path2.lineTo(this.f1298X, this.f1299Y);
                        }
                        invalidate();
                        this.changesIndx.add(this.curIndx + 1, new Path(this.tPath));
                        this.brushIndx.add(this.curIndx + 1, this.brushSize);
                        this.modeIndx.add(this.curIndx + 1, MODE);
                        this.brushTypeIndx.add(this.curIndx + 1, this.isRectBrushEnable);
                        this.vectorPoints.add(this.curIndx + 1, null);
                        this.lassoIndx.add(this.curIndx + 1, this.insidCutEnable);
                        this.tPath.reset();
                        this.curIndx++;
                        clearNextChanges();
                        this.tPath = null;
                    }
                } else if (action != 2) {
                    return false;
                } else {
                    Log.e("ERASE ACTION_MOVE", "=" + motionEvent.getAction());
                    if (this.tPath != null) {
                        Log.e("movetest", " In Action Move " + this.f1298X + " " + this.f1299Y);
                        if (this.isRectBrushEnable) {
                            Path path3 = this.tPath;
                            float f8 = this.f1298X;
                            float f9 = (float) i2;
                            float f10 = this.f1299Y;
                            path3.addRect(f8 - f9, f10 - f9, f8 + f9, f10 + f9, Path.Direction.CW);
                        } else {
                            this.tPath.lineTo(this.f1298X, this.f1299Y);
                        }
                        invalidate();
                        this.isMoved = true;
                    }
                }
            }
        }
        this.mScaleGestureDetector.onTouchEvent((View) view.getParent(), motionEvent);
        invalidate();
        return true;
    }

    private void updateShader(float f, float f2, float f3, float f4, boolean z) {
        if (this.eraseSView != null) {
            Paint paint2 = new Paint();
            if (f4 - ((float) this.offset) < ((float) ImageUtils.dpToPx(this.ctx, 300.0f))) {
                if (f3 < ((float) ImageUtils.dpToPx(this.ctx, 180.0f))) {
                    this.onLeft = false;
                }
                if (f3 > ((float) (this.screenWidth - ImageUtils.dpToPx(this.ctx, 180.0f)))) {
                    this.onLeft = true;
                }
            }
            BitmapShader bitmapShader = new BitmapShader(this.Bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint2.setShader(bitmapShader);
            Matrix matrix = new Matrix();
            float f5 = scale;
            matrix.postScale(f5 * 1.5f, f5 * 1.5f, f, f2);
            if (this.onLeft) {
                matrix.postTranslate(-(f - ((float) ImageUtils.dpToPx(this.ctx, 75.0f))), -(f2 - ((float) ImageUtils.dpToPx(this.ctx, 75.0f))));
            } else {
                matrix.postTranslate(-(f - ((float) (this.screenWidth - ImageUtils.dpToPx(this.ctx, 75.0f)))), -(f2 - ((float) ImageUtils.dpToPx(this.ctx, 75.0f))));
            }
            bitmapShader.setLocalMatrix(matrix);
            EraseSView eraseSView2 = this.eraseSView;
            double d = (this.brushSize1 / 2);
            Double.isNaN(d);
            eraseSView2.updateShaderView(paint2, (int) (d * 1.5d), z, this.onLeft, this.isRectBrushEnable);
        }
    }

    private void updateShader1(float f, float f2, float f3, float f4, boolean z) {
        BitmapShader bitmapShader;
        if (this.eraseSView != null) {
            Paint paint2 = new Paint();
            if (f4 - ((float) this.offset) < ((float) ImageUtils.dpToPx(this.ctx, 300.0f))) {
                if (f3 < ((float) ImageUtils.dpToPx(this.ctx, 180.0f))) {
                    this.onLeft = false;
                }
                if (f3 > ((float) (this.screenWidth - ImageUtils.dpToPx(this.ctx, 180.0f)))) {
                    this.onLeft = true;
                }
            }
            if (MODE == this.LASSO) {
                Bitmap createBitmap = Bitmap.createBitmap(this.Bitmap2.getWidth(), this.Bitmap2.getHeight(), this.Bitmap2.getConfig());
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawBitmap(this.Bitmap2, 0.0f, 0.0f, null);
                this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, scale));
                canvas.drawPath(this.lPath, this.erPaint1);
                bitmapShader = new BitmapShader(createBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            } else {
                bitmapShader = new BitmapShader(this.Bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }
            paint2.setShader(bitmapShader);
            Matrix matrix = new Matrix();
            float f5 = scale;
            matrix.postScale(f5 * 1.5f, f5 * 1.5f, f, f2);
            if (this.onLeft) {
                matrix.postTranslate(-(f - ((float) ImageUtils.dpToPx(this.ctx, 75.0f))), -(f2 - ((float) ImageUtils.dpToPx(this.ctx, 75.0f))));
            } else {
                matrix.postTranslate(-(f - ((float) (this.screenWidth - ImageUtils.dpToPx(this.ctx, 75.0f)))), -(f2 - ((float) ImageUtils.dpToPx(this.ctx, 75.0f))));
            }
            bitmapShader.setLocalMatrix(matrix);
            EraseSView eraseSView2 = this.eraseSView;
            double d = ((((float) this.targetBrushSize) * scale) / 2.0f);
            Double.isNaN(d);
            eraseSView2.updateShaderView(paint2, (int) (d * 1.5d), z, this.onLeft, this.isRectBrushEnable);
        }
    }

    public void move(View view, TransformInfo transformInfo) {
        computeRenderOffset(view, transformInfo.pivotX, transformInfo.pivotY);
        adjustTranslation(view, transformInfo.deltaX, transformInfo.deltaY);
        float max = Math.max(transformInfo.minimumScale, Math.min(transformInfo.maximumScale, view.getScaleX() * transformInfo.deltaScale));
        view.setScaleX(max);
        view.setScaleY(max);
        updateOnScale(max);
        invalidate();
    }

    private void clearNextChanges() {
        int size = this.changesIndx.size();
        Log.i("testings", "ClearNextChange Curindx " + this.curIndx + " Size " + size);
        int i = this.curIndx + 1;
        while (size > i) {
            Log.i("testings", " indx " + i);
            this.changesIndx.remove(i);
            this.brushIndx.remove(i);
            this.modeIndx.remove(i);
            this.brushTypeIndx.remove(i);
            this.vectorPoints.remove(i);
            this.lassoIndx.remove(i);
            size = this.changesIndx.size();
        }
        UndoRedoListener undoRedoListener2 = this.undoRedoListener;
        if (undoRedoListener2 != null) {
            undoRedoListener2.enableUndo(true, this.curIndx + 1);
            this.undoRedoListener.enableRedo(false, this.modeIndx.size() - (this.curIndx + 1));
        }
        ActionListener actionListener2 = this.actionListener;
        if (actionListener2 != null) {
            actionListener2.onActionCompleted(MODE);
        }
    }

    public void setMODE(int i) {
        Bitmap bitmap;
        MODE = i;
        if (!(i == this.TARGET || (bitmap = this.Bitmap3) == null)) {
            bitmap.recycle();
            this.Bitmap3 = null;
        }
        if (i != this.LASSO) {
            this.drawOnLasso = true;
            this.drawLasso = false;
            Bitmap bitmap2 = this.Bitmap4;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.Bitmap4 = null;
            }
        }
    }

    private Paint getPaintByMode(int i, int i2, boolean z) {
        this.paint = new Paint();
        this.paint.setAlpha(0);
        if (z) {
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStrokeJoin(Paint.Join.MITER);
            this.paint.setStrokeCap(Paint.Cap.SQUARE);
        } else {
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeJoin(Paint.Join.ROUND);
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setStrokeWidth((float) i2);
        }
        this.paint.setAntiAlias(true);
        if (i == this.ERASE) {
            this.paint.setColor(0);
            this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        if (i == this.REDRAW) {
            this.paint.setColor(-1);
            this.patternBMPshader = StickerRemoveActivity.patternBMPshader;
            this.paint.setShader(this.patternBMPshader);
        }
        return this.paint;
    }

    public void updateThreshHold() {
        if (this.Bitmap3 != null && !this.isAutoRunning) {
            this.isAutoRunning = true;
            new AsyncTaskRunner1(pc).execute(new Void[0]);
        }
    }

    public int getLastChangeMode() {
        int i = this.curIndx;
        if (i < 0) {
            return this.NONE;
        }
        return this.modeIndx.get(i).intValue();
    }

    public int getOffset() {
        return this.offset1;
    }

    public void setOffset(int i) {
        this.offset1 = i;
        this.offset = (int) updatebrushsize(ImageUtils.dpToPx(this.ctx, (float) i), scale);
        this.updateOnly = true;
    }

    public void setRadius(int i) {
        this.brushSize1 = ImageUtils.dpToPx(getContext(), (float) i);
        this.brushSize = (int) updatebrushsize(this.brushSize1, scale);
        this.updateOnly = true;
    }

    public void updateOnScale(float f) {
        Log.i("testings", "Scale " + f + "  Brushsize  " + this.brushSize);
        this.brushSize = (int) updatebrushsize(this.brushSize1, f);
        this.targetBrushSize = (int) updatebrushsize(this.targetBrushSize1, f);
        this.offset = (int) updatebrushsize(ImageUtils.dpToPx(this.ctx, (float) this.offset1), f);
    }

    public void setThreshold(int i) {
        this.TOLERANCE = i;
        if (this.curIndx >= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(" Threshold ");
            sb.append(i);
            sb.append("  ");
            sb.append(this.modeIndx.get(this.curIndx).intValue() == this.TARGET);
            Log.i("testings", sb.toString());
        }
    }

    public boolean isTouchEnable() {
        return this.isSelected;
    }

    public void enableTouchClear(boolean z) {
        this.isSelected = z;
        if (z) {
            setOnTouchListener(this);
        } else {
            setOnTouchListener(null);
        }
    }

    public void enableInsideCut(boolean z) {
        this.insidCutEnable = z;
        if (this.drawLasso) {
            Log.i("testings", " draw lassso   on up ");
            if (this.isNewPath) {
                Bitmap bitmap = this.Bitmap2;
                this.Bitmap4 = bitmap.copy(bitmap.getConfig(), true);
                drawLassoPath(this.lPath, this.insidCutEnable);
                this.changesIndx.add(this.curIndx + 1, new Path(this.lPath));
                this.brushIndx.add(this.curIndx + 1, Integer.valueOf(this.brushSize));
                this.modeIndx.add(this.curIndx + 1, Integer.valueOf(MODE));
                this.brushTypeIndx.add(this.curIndx + 1, Boolean.valueOf(this.isRectBrushEnable));
                this.vectorPoints.add(this.curIndx + 1, null);
                this.lassoIndx.add(this.curIndx + 1, Boolean.valueOf(this.insidCutEnable));
                this.curIndx++;
                clearNextChanges();
                invalidate();
                this.isNewPath = false;
                return;
            }
            Log.i("testings", " New PAth false ");
            setImageBitmap(this.Bitmap4);
            drawLassoPath(this.lPath, this.insidCutEnable);
            this.lassoIndx.add(this.curIndx, Boolean.valueOf(this.insidCutEnable));
            return;
        }
        Toast.makeText(this.ctx, "Please Draw a closed path!!!", Toast.LENGTH_SHORT).show();
    }

    public boolean isRectBrushEnable() {
        return this.isRectBrushEnable;
    }

    public void enableRectBrush(boolean z) {
        this.isRectBrushEnable = z;
        this.updateOnly = true;
    }

    public void undoChange() {
        UndoRedoListener undoRedoListener2;
        this.drawLasso = false;
        setImageBitmap(this.orgBit);
        Log.i("testings", "Performing UNDO Curindx " + this.curIndx + "  " + this.changesIndx.size());
        int i = this.curIndx;
        if (i >= 0) {
            this.curIndx = i - 1;
            redrawCanvas();
            Log.i("testings", " Curindx " + this.curIndx + "  " + this.changesIndx.size());
            UndoRedoListener undoRedoListener3 = this.undoRedoListener;
            if (undoRedoListener3 != null) {
                undoRedoListener3.enableUndo(true, this.curIndx + 1);
                this.undoRedoListener.enableRedo(true, this.modeIndx.size() - (this.curIndx + 1));
            }
            int i2 = this.curIndx;
            if (i2 < 0 && (undoRedoListener2 = this.undoRedoListener) != null) {
                undoRedoListener2.enableUndo(false, i2 + 1);
            }
        }
    }

    public void redoChange() {
        UndoRedoListener undoRedoListener2;
        this.drawLasso = false;
        StringBuilder sb = new StringBuilder();
        sb.append(this.curIndx + 1 >= this.changesIndx.size());
        sb.append(" Curindx ");
        sb.append(this.curIndx);
        sb.append(" ");
        sb.append(this.changesIndx.size());
        Log.i("testings", sb.toString());
        if (this.curIndx + 1 < this.changesIndx.size()) {
            setImageBitmap(this.orgBit);
            this.curIndx++;
            redrawCanvas();
            UndoRedoListener undoRedoListener3 = this.undoRedoListener;
            if (undoRedoListener3 != null) {
                undoRedoListener3.enableUndo(true, this.curIndx + 1);
                this.undoRedoListener.enableRedo(true, this.modeIndx.size() - (this.curIndx + 1));
            }
            if (this.curIndx + 1 >= this.changesIndx.size() && (undoRedoListener2 = this.undoRedoListener) != null) {
                undoRedoListener2.enableRedo(false, this.modeIndx.size() - (this.curIndx + 1));
            }
        }
    }

    private void redrawCanvas() {
        for (int i = 0; i <= this.curIndx; i++) {
            if (this.modeIndx.get(i).intValue() == this.ERASE || this.modeIndx.get(i).intValue() == this.REDRAW) {
                this.tPath = new Path(this.changesIndx.get(i));
                this.paint = getPaintByMode(this.modeIndx.get(i).intValue(), this.brushIndx.get(i).intValue(), this.brushTypeIndx.get(i).booleanValue());
                this.c2.drawPath(this.tPath, this.paint);
                this.tPath.reset();
            }
            if (this.modeIndx.get(i).intValue() == this.TARGET) {
                Vector vector = this.vectorPoints.get(i);
                int i2 = this.width;
                int i3 = this.height;
                int[] iArr = new int[(i2 * i3)];
                this.Bitmap2.getPixels(iArr, 0, i2, 0, 0, i2, i3);
                for (int i4 = 0; i4 < vector.size(); i4++) {
                    Point point2 = (Point) vector.get(i4);
                    iArr[getIndex(point2.x, point2.y, this.width)] = 0;
                }
                Bitmap bitmap = this.Bitmap2;
                int i5 = this.width;
                bitmap.setPixels(iArr, 0, i5, 0, 0, i5, this.height);
            }
            if (this.modeIndx.get(i).intValue() == this.LASSO) {
                Log.i("testings", " onDraw Lassoo ");
                drawLassoPath(new Path(this.changesIndx.get(i)), this.lassoIndx.get(i).booleanValue());
            }
        }
    }

    private void drawLassoPath(Path path, boolean z) {
        Log.i("testings", z + " New PAth false " + path.isEmpty());
        if (z) {
            Paint paint2 = new Paint();
            paint2.setAntiAlias(true);
            paint2.setColor(0);
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            this.c2.drawPath(path, paint2);
        } else {
            Bitmap bitmap = this.Bitmap2;
            Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
            new Canvas(copy).drawBitmap(this.Bitmap2, 0.0f, 0.0f, null);
            this.c2.drawColor(this.NONE, PorterDuff.Mode.CLEAR);
            Paint paint3 = new Paint();
            paint3.setAntiAlias(true);
            this.c2.drawPath(path, paint3);
            paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            this.c2.drawBitmap(copy, 0.0f, 0.0f, paint3);
            copy.recycle();
        }
        this.drawOnLasso = true;
    }

    public Bitmap getFinalBitmap() {
        Bitmap bitmap = this.Bitmap2;
        return bitmap.copy(bitmap.getConfig(), true);
    }

    public void setEraseSView(EraseSView eraseSView2) {
        this.eraseSView = eraseSView2;
    }

    public interface ActionListener {
        void onAction(int i);

        void onActionCompleted(int i);
    }

    public interface UndoRedoListener {
        void enableRedo(boolean z, int i);

        void enableUndo(boolean z, int i);
    }

    private class AsyncTaskRunner1 extends AsyncTask<Void, Void, Bitmap> {

        int ch;
        Vector<Point> targetPoints;

        public AsyncTaskRunner1(int i) {
            this.ch = i;
        }


        public Bitmap doInBackground(Void... voidArr) {
            if (this.ch == 0) {
                return null;
            }
            this.targetPoints = new Vector<>();
            FloodFill(new Point(EraseView.this.point.x, EraseView.this.point.y), this.ch, 0);
            if (EraseView.this.curIndx < 0) {
                EraseView.this.changesIndx.add(EraseView.this.curIndx + 1, new Path());
                EraseView.this.brushIndx.add(EraseView.this.curIndx + 1, EraseView.this.brushSize);
                EraseView.this.modeIndx.add(EraseView.this.curIndx + 1, EraseView.this.TARGET);
                EraseView.this.brushTypeIndx.add(EraseView.this.curIndx + 1, EraseView.this.isRectBrushEnable);
                EraseView.this.vectorPoints.add(EraseView.this.curIndx + 1, new Vector(this.targetPoints));
                EraseView.this.lassoIndx.add(EraseView.this.curIndx + 1, EraseView.this.insidCutEnable);
                EraseView eraseView = EraseView.this;
                eraseView.curIndx = eraseView.curIndx + 1;
                clearNextChanges();
            } else if (EraseView.this.modeIndx.get(EraseView.this.curIndx) == EraseView.this.TARGET && EraseView.this.curIndx == EraseView.this.modeIndx.size() - 1) {
                EraseView.this.vectorPoints.add(EraseView.this.curIndx, new Vector(this.targetPoints));
            } else {
                EraseView.this.changesIndx.add(EraseView.this.curIndx + 1, new Path());
                EraseView.this.brushIndx.add(EraseView.this.curIndx + 1, EraseView.this.brushSize);
                EraseView.this.modeIndx.add(EraseView.this.curIndx + 1, EraseView.this.TARGET);
                EraseView.this.brushTypeIndx.add(EraseView.this.curIndx + 1, EraseView.this.isRectBrushEnable);
                EraseView.this.vectorPoints.add(EraseView.this.curIndx + 1, new Vector(this.targetPoints));
                EraseView.this.lassoIndx.add(EraseView.this.curIndx + 1, EraseView.this.insidCutEnable);
                EraseView eraseView2 = EraseView.this;
                eraseView2.curIndx = eraseView2.curIndx + 1;
                clearNextChanges();
            }
            Log.i("testing", "Time : " + this.ch + "  " + EraseView.this.curIndx + "   " + EraseView.this.changesIndx.size());
            return null;
        }

        private void FloodFill(Point point, int i, int i2) {
            if (i != 0) {
                int[] iArr = new int[(EraseView.this.width * EraseView.this.height)];
                EraseView.this.Bitmap3.getPixels(iArr, 0, EraseView.this.width, 0, 0, EraseView.this.width, EraseView.this.height);
                LinkedList linkedList = new LinkedList();
                linkedList.add(point);
                while (linkedList.size() > 0) {
                    Point point2 = (Point) linkedList.poll();
                    if (compareColor(iArr[EraseView.this.getIndex(point2.x, point2.y, EraseView.this.width)], i)) {
                        Point point3 = new Point(point2.x + 1, point2.y);
                        while (point2.x > 0 && compareColor(iArr[EraseView.this.getIndex(point2.x, point2.y, EraseView.this.width)], i)) {
                            iArr[EraseView.this.getIndex(point2.x, point2.y, EraseView.this.width)] = i2;
                            this.targetPoints.add(new Point(point2.x, point2.y));
                            if (point2.y > 0 && compareColor(iArr[EraseView.this.getIndex(point2.x, point2.y - 1, EraseView.this.width)], i)) {
                                linkedList.add(new Point(point2.x, point2.y - 1));
                            }
                            if (point2.y < EraseView.this.height && compareColor(iArr[EraseView.this.getIndex(point2.x, point2.y + 1, EraseView.this.width)], i)) {
                                linkedList.add(new Point(point2.x, point2.y + 1));
                            }
                            point2.x--;
                        }
                        if (point2.y > 0 && point2.y < EraseView.this.height) {
                            iArr[EraseView.this.getIndex(point2.x, point2.y, EraseView.this.width)] = i2;
                            this.targetPoints.add(new Point(point2.x, point2.y));
                        }
                        while (point3.x < EraseView.this.width && compareColor(iArr[EraseView.this.getIndex(point3.x, point3.y, EraseView.this.width)], i)) {
                            iArr[EraseView.this.getIndex(point3.x, point3.y, EraseView.this.width)] = i2;
                            this.targetPoints.add(new Point(point3.x, point3.y));
                            if (point3.y > 0 && compareColor(iArr[EraseView.this.getIndex(point3.x, point3.y - 1, EraseView.this.width)], i)) {
                                linkedList.add(new Point(point3.x, point3.y - 1));
                            }
                            if (point3.y < EraseView.this.height && compareColor(iArr[EraseView.this.getIndex(point3.x, point3.y + 1, EraseView.this.width)], i)) {
                                linkedList.add(new Point(point3.x, point3.y + 1));
                            }
                            point3.x++;
                        }
                        if (point3.y > 0 && point3.y < EraseView.this.height) {
                            iArr[EraseView.this.getIndex(point3.x, point3.y, EraseView.this.width)] = i2;
                            this.targetPoints.add(new Point(point3.x, point3.y));
                        }
                    }
                }
                EraseView.this.Bitmap2.setPixels(iArr, 0, EraseView.this.width, 0, 0, EraseView.this.width, EraseView.this.height);
            }
        }

        public boolean compareColor(int i, int i2) {
            if (!(i == 0 || i2 == 0)) {
                if (i == i2) {
                    return true;
                }
                return Math.abs(Color.red(i) - Color.red(i2)) <= EraseView.this.TOLERANCE && Math.abs(Color.green(i) - Color.green(i2)) <= EraseView.this.TOLERANCE && Math.abs(Color.blue(i) - Color.blue(i2)) <= EraseView.this.TOLERANCE;
            }
            return false;
        }

        private void clearNextChanges() {
            int size = EraseView.this.changesIndx.size();
            Log.i("testings", " Curindx " + EraseView.this.curIndx + " Size " + size);
            int index = EraseView.this.curIndx + 1;
            while (size > index) {
                Log.i("testings", " indx " + index);
                EraseView.this.changesIndx.remove(index);
                EraseView.this.brushIndx.remove(index);
                EraseView.this.modeIndx.remove(index);
                EraseView.this.brushTypeIndx.remove(index);
                EraseView.this.vectorPoints.remove(index);
                EraseView.this.lassoIndx.remove(index);
                size = EraseView.this.changesIndx.size();
            }
            if (EraseView.this.undoRedoListener != null) {
                EraseView.this.undoRedoListener.enableUndo(true, EraseView.this.curIndx + 1);
                EraseView.this.undoRedoListener.enableRedo(false, EraseView.this.modeIndx.size() - (EraseView.this.curIndx + 1));
            }
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            EraseView eraseView = EraseView.this;
            eraseView.pd = new ProgressDialog(eraseView.getContext());
            EraseView.this.pd.setMessage("Processing...");
            EraseView.this.pd.setCancelable(false);
            EraseView.this.pd.show();
        }

        @Override
        public void onPostExecute(Bitmap bitmap) {
            EraseView.this.pd.dismiss();
            EraseView.this.invalidate();
            EraseView.this.isAutoRunning = false;
        }
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Bitmap> {

        int ch;
        Vector<Point> targetPoints;

        public AsyncTaskRunner(int i) {
            this.ch = i;
        }


        public Bitmap doInBackground(Void... voidArr) {
            if (this.ch == 0) {
                return null;
            }
            this.targetPoints = new Vector<>();
            EraseView eraseView = EraseView.this;
            eraseView.Bitmap3 = eraseView.Bitmap2.copy(EraseView.this.Bitmap2.getConfig(), true);
            FloodFill(EraseView.this.Bitmap2, new Point(EraseView.this.point.x, EraseView.this.point.y), this.ch, 0);
            EraseView.this.changesIndx.add(EraseView.this.curIndx + 1, new Path());
            EraseView.this.brushIndx.add(EraseView.this.curIndx + 1, EraseView.this.brushSize);
            EraseView.this.modeIndx.add(EraseView.this.curIndx + 1, EraseView.this.TARGET);
            EraseView.this.brushTypeIndx.add(EraseView.this.curIndx + 1, EraseView.this.isRectBrushEnable);
            EraseView.this.vectorPoints.add(EraseView.this.curIndx + 1, new Vector(this.targetPoints));
            EraseView.this.lassoIndx.add(EraseView.this.curIndx + 1, EraseView.this.insidCutEnable);
            EraseView eraseView2 = EraseView.this;
            eraseView2.curIndx = eraseView2.curIndx + 1;
            clearNextChanges();
            EraseView.this.updateOnly = true;
            Log.i("testing", "Time : " + this.ch + "  " + EraseView.this.curIndx + "   " + EraseView.this.changesIndx.size());
            return null;
        }

        private void FloodFill(Bitmap bitmap, Point point, int i, int i2) {
            if (i != 0) {
                int[] iArr = new int[(EraseView.this.width * EraseView.this.height)];
                bitmap.getPixels(iArr, 0, EraseView.this.width, 0, 0, EraseView.this.width, EraseView.this.height);
                LinkedList linkedList = new LinkedList();
                linkedList.add(point);
                while (linkedList.size() > 0) {
                    Point point2 = (Point) linkedList.poll();
                    if (compareColor(iArr[EraseView.this.getIndex(point2.x, point2.y, EraseView.this.width)], i)) {
                        Point point3 = new Point(point2.x + 1, point2.y);
                        while (point2.x > 0 && compareColor(iArr[EraseView.this.getIndex(point2.x, point2.y, EraseView.this.width)], i)) {
                            iArr[EraseView.this.getIndex(point2.x, point2.y, EraseView.this.width)] = i2;
                            this.targetPoints.add(new Point(point2.x, point2.y));
                            if (point2.y > 0 && compareColor(iArr[EraseView.this.getIndex(point2.x, point2.y - 1, EraseView.this.width)], i)) {
                                linkedList.add(new Point(point2.x, point2.y - 1));
                            }
                            if (point2.y < EraseView.this.height && compareColor(iArr[EraseView.this.getIndex(point2.x, point2.y + 1, EraseView.this.width)], i)) {
                                linkedList.add(new Point(point2.x, point2.y + 1));
                            }
                            point2.x--;
                        }
                        if (point2.y > 0 && point2.y < EraseView.this.height) {
                            iArr[EraseView.this.getIndex(point2.x, point2.y, EraseView.this.width)] = i2;
                            this.targetPoints.add(new Point(point2.x, point2.y));
                        }
                        while (point3.x < EraseView.this.width && compareColor(iArr[EraseView.this.getIndex(point3.x, point3.y, EraseView.this.width)], i)) {
                            iArr[EraseView.this.getIndex(point3.x, point3.y, EraseView.this.width)] = i2;
                            this.targetPoints.add(new Point(point3.x, point3.y));
                            if (point3.y > 0 && compareColor(iArr[EraseView.this.getIndex(point3.x, point3.y - 1, EraseView.this.width)], i)) {
                                linkedList.add(new Point(point3.x, point3.y - 1));
                            }
                            if (point3.y < EraseView.this.height && compareColor(iArr[EraseView.this.getIndex(point3.x, point3.y + 1, EraseView.this.width)], i)) {
                                linkedList.add(new Point(point3.x, point3.y + 1));
                            }
                            point3.x++;
                        }
                        if (point3.y > 0 && point3.y < EraseView.this.height) {
                            iArr[EraseView.this.getIndex(point3.x, point3.y, EraseView.this.width)] = i2;
                            this.targetPoints.add(new Point(point3.x, point3.y));
                        }
                    }
                }
                bitmap.setPixels(iArr, 0, EraseView.this.width, 0, 0, EraseView.this.width, EraseView.this.height);
            }
        }

        public boolean compareColor(int i, int i2) {
            if (!(i == 0 || i2 == 0)) {
                if (i == i2) {
                    return true;
                }
                return Math.abs(Color.red(i) - Color.red(i2)) <= EraseView.this.TOLERANCE && Math.abs(Color.green(i) - Color.green(i2)) <= EraseView.this.TOLERANCE && Math.abs(Color.blue(i) - Color.blue(i2)) <= EraseView.this.TOLERANCE;
            }
            return false;
        }

        private void clearNextChanges() {
            int size = EraseView.this.changesIndx.size();
            Log.i("testings", " Curindx " + EraseView.this.curIndx + " Size " + size);
            int index = EraseView.this.curIndx + 1;
            while (size > index) {
                Log.i("testings", " indx " + index);
                EraseView.this.changesIndx.remove(index);
                EraseView.this.brushIndx.remove(index);
                EraseView.this.modeIndx.remove(index);
                EraseView.this.brushTypeIndx.remove(index);
                EraseView.this.vectorPoints.remove(index);
                EraseView.this.lassoIndx.remove(index);
                size = EraseView.this.changesIndx.size();
            }
            if (EraseView.this.undoRedoListener != null) {
                EraseView.this.undoRedoListener.enableUndo(true, EraseView.this.curIndx + 1);
                EraseView.this.undoRedoListener.enableRedo(false, EraseView.this.modeIndx.size() - (EraseView.this.curIndx + 1));
            }
            if (EraseView.this.actionListener != null) {
                EraseView.this.actionListener.onActionCompleted(EraseView.MODE);
            }
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            EraseView eraseView = EraseView.this;
            eraseView.pd = new ProgressDialog(eraseView.getContext());
            EraseView.this.pd.setMessage("Processing...");
            EraseView.this.pd.setCancelable(false);
            EraseView.this.pd.show();
        }

        @Override
        public void onPostExecute(Bitmap bitmap) {
            EraseView.this.pd.dismiss();
            EraseView eraseView = EraseView.this;
            eraseView.pd = null;
            eraseView.invalidate();
            EraseView.this.isAutoRunning = false;
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

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

        public boolean onScale(View view, ScaleGestureDetector scaleGestureDetector) {
            TransformInfo transformInfo = new TransformInfo();
            transformInfo.deltaScale = EraseView.this.isScaleEnabled ? scaleGestureDetector.getScaleFactor() : 1.0f;
            float f = 0.0f;
            transformInfo.deltaAngle = EraseView.this.isRotateEnabled ? Vector2D.getAngle(this.mPrevSpanVector, scaleGestureDetector.getCurrentSpanVector()) : 0.0f;
            transformInfo.deltaX = EraseView.this.isTranslateEnabled ? scaleGestureDetector.getFocusX() - this.mPivotX : 0.0f;
            if (EraseView.this.isTranslateEnabled) {
                f = scaleGestureDetector.getFocusY() - this.mPivotY;
            }
            transformInfo.deltaY = f;
            transformInfo.pivotX = this.mPivotX;
            transformInfo.pivotY = this.mPivotY;
            transformInfo.minimumScale = EraseView.this.minimumScale;
            transformInfo.maximumScale = EraseView.this.maximumScale;
            EraseView.this.move(view, transformInfo);
            return false;
        }
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
}
