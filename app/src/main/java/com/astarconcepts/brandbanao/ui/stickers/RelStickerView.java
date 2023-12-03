package com.astarconcepts.brandbanao.ui.stickers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.utils.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

public class RelStickerView extends RelativeLayout
        implements MultiTouchListener.TouchCallbackListener {
    public static final String TAG = "ResizableStickerView";
    private final boolean isColorFilterEnable = false;
    private final OnTouchListener mTouchListener1 = new TouchListerner();
    private final OnTouchListener rTouchListener = new TouchListner1();
    public int f26s;
    public String field_two = "0,0";
    public int f167he;
    public boolean isMultiTouchEnabled = true;
    public int leftMargin = 0;
    public TouchEventListener listener = null;
    public ImageView main_iv;
    public String stkr_path = "";
    public int topMargin = 0;
    public int f168wi;
    double angle = 0.0d;
    int baseh;
    int basew;
    int basex;
    int basey;
    float f165cX = 0.0f;
    float f166cY = 0.0f;
    double dAngle = 0.0d;
    float heightMain = 0.0f;
    int margl;
    int margt;
    Animation scale;
    int screenHeight = 300;
    int screenWidth = 300;
    double tAngle = 0.0d;
    double vAngle = 0.0d;
    float widthMain = 0.0f;
    Animation zoomInScale;
    Animation zoomOutScale;
    private ImageView border_iv;
    private Bitmap btmp = null;
    private String colorType = "colored";
    private Context context;
    private ImageView delete_iv;
    private String drawableId;
    private ImageView flip_iv;
    private int hueProg = 1;
    private int imgAlpha = 255;
    private int imgColor = 0;
    private boolean isBorderVisible = false;
    private Uri resUri = null;
    private ImageView rotate_iv;
    private float rotation;
    private ImageView scale_iv;
    private float yRotation;
    private boolean isImage = false;

    public RelStickerView(Context context2, boolean isImage) {
        super(context2);
        this.isImage = isImage;
        init(context2);
    }

    public RelStickerView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        init(context2);
    }

    public RelStickerView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        init(context2);
    }


    public void init(Context context2) {
        this.context = context2;
        this.main_iv = new ImageView(this.context);
        this.scale_iv = new ImageView(this.context);
        this.border_iv = new ImageView(this.context);
        this.flip_iv = new ImageView(this.context);
        this.rotate_iv = new ImageView(this.context);
        this.delete_iv = new ImageView(this.context);
        this.f26s = dpToPx(this.context, 25);
        this.f168wi = dpToPx(this.context, 200);
        this.f167he = dpToPx(this.context, 200);
        this.scale_iv.setImageResource(R.drawable.sticker_scale);
        this.border_iv.setImageResource(R.drawable.sticker_border_gray);
        if (isImage) {
            this.flip_iv.setImageResource(R.drawable.sticker_done);
        } else {
            this.flip_iv.setImageResource(R.drawable.sticker_flip);
        }
        this.rotate_iv.setImageResource(R.drawable.sticker_rotate);
        this.delete_iv.setImageResource(R.drawable.sticker_delete1);
        LayoutParams layoutParams = new LayoutParams(this.f168wi, this.f167he);
        LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        layoutParams2.setMargins(5, 5, 5, 5);
        if (Build.VERSION.SDK_INT >= 17) {
            layoutParams2.addRule(17);
        } else {
            layoutParams2.addRule(1);
        }
        LayoutParams layoutParams3 = new LayoutParams(this.f26s, this.f26s);
        layoutParams3.addRule(12);
        layoutParams3.addRule(11);
        layoutParams3.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams4 = new LayoutParams(this.f26s, this.f26s);
        layoutParams4.addRule(10);
        layoutParams4.addRule(11);
        layoutParams4.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams5 = new LayoutParams(this.f26s, this.f26s);
        layoutParams5.addRule(12);
        layoutParams5.addRule(9);
        layoutParams5.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams6 = new LayoutParams(this.f26s, this.f26s);
        layoutParams6.addRule(10);
        layoutParams6.addRule(9);
        layoutParams6.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams7 = new LayoutParams(-1, -1);
        setLayoutParams(layoutParams);
        setBackgroundResource(R.drawable.sticker_border_gray);
        addView(this.border_iv);
        this.border_iv.setLayoutParams(layoutParams7);
        this.border_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        this.border_iv.setTag("border_iv");
        addView(this.main_iv);
        this.main_iv.setLayoutParams(layoutParams2);
        addView(this.flip_iv);
        this.flip_iv.setLayoutParams(layoutParams4);
        this.flip_iv.setOnClickListener(view -> {
            if (isImage) {
                RelStickerView.this.listener.onMainClick(view);
            } else {
                ImageView imageView = RelStickerView.this.main_iv;
                float f = -180.0f;
                if (RelStickerView.this.main_iv.getRotationY() == -180.0f) {
                    f = 0.0f;
                }
                imageView.setRotationY(f);
                RelStickerView.this.main_iv.invalidate();
                RelStickerView.this.requestLayout();
            }
        });
        addView(this.rotate_iv);
        this.rotate_iv.setLayoutParams(layoutParams5);
        this.rotate_iv.setOnTouchListener(this.rTouchListener);
        addView(this.delete_iv);
        this.delete_iv.setLayoutParams(layoutParams6);
        this.delete_iv.setOnClickListener(new DeleteClick());
        addView(this.scale_iv);
        this.scale_iv.setLayoutParams(layoutParams3);
        this.scale_iv.setOnTouchListener(this.mTouchListener1);
        this.scale_iv.setTag("scale_iv");
        this.rotation = getRotation();
        this.scale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_anim_view);
        this.zoomOutScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_out_view);
        this.zoomInScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_in_view);
        this.isMultiTouchEnabled = setDefaultTouchListener(true);
    }

    public boolean setDefaultTouchListener(boolean z) {
        if (z) {
            setOnTouchListener(new MultiTouchListener().enableRotation(true).setOnTouchCallbackListener(this));
            return true;
        }
        setOnTouchListener(null);
        return false;
    }

    public void setBorderVisibility(boolean z) {
        this.isBorderVisible = z;
        if (!z) {
            this.border_iv.setVisibility(GONE);
            this.scale_iv.setVisibility(GONE);
            this.flip_iv.setVisibility(GONE);
            this.rotate_iv.setVisibility(GONE);
            this.delete_iv.setVisibility(GONE);
            setBackgroundResource(VISIBLE);
            if (this.isColorFilterEnable) {
                this.main_iv.setColorFilter(Color.parseColor("#303828"));
            }
        } else if (this.border_iv.getVisibility() != VISIBLE) {
            this.border_iv.setVisibility(VISIBLE);
            this.scale_iv.setVisibility(VISIBLE);
            this.flip_iv.setVisibility(VISIBLE);
            this.rotate_iv.setVisibility(VISIBLE);
            this.delete_iv.setVisibility(VISIBLE);
            setBackgroundResource(R.drawable.sticker_border_gray);
            this.main_iv.startAnimation(this.scale);
        }
    }



    public int getColor() {
        return this.imgColor;
    }

    public void setColor(int i) {
        try {
            this.main_iv.setColorFilter(i);
            this.imgColor = i;
        } catch (Exception unused) {
        }
    }



    public void optimize(float f, float f2) {
        setX(getX() * f);
        setY(getY() * f2);
        getLayoutParams().width = (int) (((float) this.f168wi) * f);
        getLayoutParams().height = (int) (((float) this.f167he) * f2);
    }

    public int dpToPx(Context context2, int i) {
        context2.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    public void onTouchCallback(View view) {
        if (this.listener != null) {
            this.listener.onTouchDown(view);
        }
    }

    public void onTouchUpCallback(View view) {
        if (this.listener != null) {
            this.listener.onTouchUp(view);
        }
    }

    public void onTouchMoveCallback(View view) {
        if (this.listener != null) {
            this.listener.onTouchMove(view);
        }
    }

    public interface TouchEventListener {
        void onDelete();

        void onEdit(View view, Uri uri);

        void onRotateDown(View view);

        void onRotateMove(View view);

        void onRotateUp(View view);

        void onScaleDown(View view);

        void onScaleMove(View view);

        void onScaleUp(View view);

        void onTouchDown(View view);

        void onTouchMove(View view);

        void onTouchUp(View view);

        void onMainClick(View view);
    }

    class TouchListerner implements OnTouchListener {
        TouchListerner() {
        }

        @SuppressLint({"NewApi"})
        public boolean onTouch(View view, MotionEvent motionEvent) {
            RelStickerView relStickerView = (RelStickerView) view.getParent();
            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) RelStickerView.this.getLayoutParams();
            switch (motionEvent.getAction()) {
                case 0:
                    if (relStickerView != null) {
                        relStickerView.requestDisallowInterceptTouchEvent(true);
                    }
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onScaleDown(RelStickerView.this);
                    }
                    RelStickerView.this.invalidate();
                    RelStickerView.this.basex = rawX;
                    RelStickerView.this.basey = rawY;
                    RelStickerView.this.basew = RelStickerView.this.getWidth();
                    RelStickerView.this.baseh = RelStickerView.this.getHeight();
                    RelStickerView.this.getLocationOnScreen(new int[2]);
                    RelStickerView.this.margl = layoutParams.leftMargin;
                    RelStickerView.this.margt = layoutParams.topMargin;
                    break;
                case 1:
                    int unused = RelStickerView.this.f168wi = RelStickerView.this.getLayoutParams().width;
                    int unused2 = RelStickerView.this.f167he = RelStickerView.this.getLayoutParams().height;
                    int unused3 = RelStickerView.this.leftMargin = ((FrameLayout.LayoutParams) RelStickerView.this.getLayoutParams()).leftMargin;
                    int unused4 = RelStickerView.this.topMargin = ((FrameLayout.LayoutParams) RelStickerView.this.getLayoutParams()).topMargin;
                    RelStickerView relStickerView2 = RelStickerView.this;
                    String unused5 = relStickerView2.field_two = RelStickerView.this.leftMargin + "," + RelStickerView.this.topMargin;
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onScaleUp(RelStickerView.this);
                        break;
                    }
                    break;
                case 2:
                    if (relStickerView != null) {
                        relStickerView.requestDisallowInterceptTouchEvent(true);
                    }
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onScaleMove(RelStickerView.this);
                    }
                    float degrees = (float) Math.toDegrees(Math.atan2(rawY - RelStickerView.this.basey, rawX - RelStickerView.this.basex));
                    if (degrees < 0.0f) {
                        degrees += 360.0f;
                    }
                    int i = rawX - RelStickerView.this.basex;
                    int i2 = rawY - RelStickerView.this.basey;
                    int i3 = i2 * i2;
                    int sqrt = (int) (Math.sqrt((i * i) + i3) * Math.cos(Math.toRadians(degrees - RelStickerView.this.getRotation())));
                    int sqrt2 = (int) (Math.sqrt((sqrt * sqrt) + i3) * Math.sin(Math.toRadians(degrees - RelStickerView.this.getRotation())));
                    int i4 = (sqrt * 2) + RelStickerView.this.basew;
                    int i5 = (sqrt2 * 2) + RelStickerView.this.baseh;
                    if (i4 > RelStickerView.this.f26s) {
                        layoutParams.width = i4;
                        layoutParams.leftMargin = RelStickerView.this.margl - sqrt;
                    }
                    if (i5 > RelStickerView.this.f26s) {
                        layoutParams.height = i5;
                        layoutParams.topMargin = RelStickerView.this.margt - sqrt2;
                    }
                    RelStickerView.this.setLayoutParams(layoutParams);
                    RelStickerView.this.performLongClick();
                    break;
            }
            return true;
        }
    }

    class TouchListner1 implements OnTouchListener {
        TouchListner1() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            RelStickerView relStickerView = (RelStickerView) view.getParent();
            switch (motionEvent.getAction()) {
                case 0:
                    if (relStickerView != null) {
                        relStickerView.requestDisallowInterceptTouchEvent(true);
                    }
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onRotateDown(RelStickerView.this);
                    }
                    Rect rect = new Rect();
                    ((View) view.getParent()).getGlobalVisibleRect(rect);
                    RelStickerView.this.f165cX = rect.exactCenterX();
                    RelStickerView.this.f166cY = rect.exactCenterY();
                    RelStickerView.this.vAngle = ((View) view.getParent()).getRotation();
                    RelStickerView.this.tAngle = (Math.atan2(RelStickerView.this.f166cY - motionEvent.getRawY(), RelStickerView.this.f165cX - motionEvent.getRawX()) * 180.0d) / 3.141592653589793d;
                    RelStickerView.this.dAngle = RelStickerView.this.vAngle - RelStickerView.this.tAngle;
                    break;
                case 1:
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onRotateUp(RelStickerView.this);
                        break;
                    }
                    break;
                case 2:
                    if (relStickerView != null) {
                        relStickerView.requestDisallowInterceptTouchEvent(true);
                    }
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onRotateMove(RelStickerView.this);
                    }
                    RelStickerView.this.angle = (Math.atan2(RelStickerView.this.f166cY - motionEvent.getRawY(), RelStickerView.this.f165cX - motionEvent.getRawX()) * 180.0d) / 3.141592653589793d;
                    ((View) view.getParent()).setRotation((float) (RelStickerView.this.angle + RelStickerView.this.dAngle));
                    ((View) view.getParent()).invalidate();
                    ((View) view.getParent()).requestLayout();
                    break;
            }
            return true;
        }
    }

    class DeleteClick implements OnClickListener {
        DeleteClick() {
        }

        public void onClick(View view) {
            final ViewGroup viewGroup = (ViewGroup) RelStickerView.this.getParent();
            RelStickerView.this.zoomInScale.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    viewGroup.removeView(RelStickerView.this);
                }
            });
            RelStickerView.this.main_iv.startAnimation(RelStickerView.this.zoomInScale);
            RelStickerView.this.setBorderVisibility(false);
            if (RelStickerView.this.listener != null) {
                RelStickerView.this.listener.onDelete();
            }
        }
    }

}
