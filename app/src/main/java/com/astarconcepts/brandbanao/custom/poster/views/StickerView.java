package com.astarconcepts.brandbanao.custom.poster.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.model.ElementInfoPoster;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.ColorFilterGenerator;
import com.astarconcepts.brandbanao.utils.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.HttpStatus;


public class StickerView extends RelativeLayout implements MultiTouchListener.TouchCallbackListener {
    public static final String TAG = "StickerView";
    double angle = 0.0d;
    int baseh;
    int basew;
    int basex;
    int basey;
    private ImageView border_iv;
    private Bitmap btmp = null;

    float cX = 0.0f;
    public boolean isFrameItem = false;


    float cY = 0.0f;
    private double centerX;
    private double centerY;
    public String colorType = "colored";
    private Activity context;

    public int currentState = 0;
    double dAngle = 0.0d;
    private ImageView delete_iv;
    private String drawableId;

    public int extraMargin = 35;
    private int f26s;
    private final String field_four = "";
    private final int field_one = 0;
    private final String field_three = "";

    public String field_two = "0,0";
    private ImageView flip_iv;


    public int he;
    float heightMain = 0.0f;
    private int hueProg = 1;
    private int imgAlpha = 255;
    private int imgColor = 0;
    private boolean isBorderVisible = false;
    private boolean isColorFilterEnable = false;
    boolean isFisrtAnimation = false;
    private boolean isFromAddText = false;
    public boolean isMultiTouchEnabled = true;
    public boolean isUndoRedo = false;

    public int leftMargin = 0;

    public TouchEventListener listener = null;
    private final View.OnTouchListener scaleTouchListener = (view, event) -> {
        StickerView rl = (StickerView) view.getParent();
        int j = (int) event.getRawX();
        int i = (int) event.getRawY();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) StickerView.this.getLayoutParams();
        int action = event.getAction();
        if (action == 0) {
            if (rl != null) {
                rl.requestDisallowInterceptTouchEvent(true);
            }
            if (StickerView.this.listener != null) {
                StickerView.this.listener.onScaleDown(StickerView.this);
            }
            StickerView.this.invalidate();
            StickerView StickerView = StickerView.this;
            StickerView.basex = j;
            StickerView.basey = i;
            StickerView.basew = StickerView.getWidth();
            StickerView StickerView2 = StickerView.this;
            StickerView2.baseh = StickerView2.getHeight();
            StickerView.this.getLocationOnScreen(new int[2]);
            StickerView.this.margl = layoutParams.leftMargin;
            StickerView.this.margt = layoutParams.topMargin;
        } else if (action == 1) {
            StickerView StickerView3 = StickerView.this;
            StickerView3.wi = StickerView3.getLayoutParams().width;
            StickerView StickerView4 = StickerView.this;
            StickerView4.he = StickerView4.getLayoutParams().height;
            StickerView StickerView5 = StickerView.this;
            StickerView5.leftMargin = ((RelativeLayout.LayoutParams) StickerView5.getLayoutParams()).leftMargin;
            StickerView StickerView6 = StickerView.this;
            StickerView6.topMargin = ((RelativeLayout.LayoutParams) StickerView6.getLayoutParams()).topMargin;
            StickerView StickerView7 = StickerView.this;
            StickerView7.field_two = String.valueOf(StickerView.this.leftMargin) + "," + String.valueOf(StickerView.this.topMargin);
            if (StickerView.this.listener != null) {
                StickerView.this.listener.onScaleUp(StickerView.this);
            }
        } else if (action == 2) {
            if (rl != null) {
                rl.requestDisallowInterceptTouchEvent(true);
            }
            if (StickerView.this.listener != null) {
                StickerView.this.listener.onScaleMove(StickerView.this);
            }
            float f2 = (float) Math.toDegrees(Math.atan2((double) (i - StickerView.this.basey), (double) (j - StickerView.this.basex)));
            float f1 = f2;
            if (f2 < 0.0f) {
                f1 = f2 + 360.0f;
            }
            int j2 = j - StickerView.this.basex;
            int k = i - StickerView.this.basey;
            int i2 = (int) (Math.sqrt((double) ((j2 * j2) + (k * k))) * Math.cos(Math.toRadians((double) (f1 - StickerView.this.getRotation()))));
            int j3 = (int) (Math.sqrt((double) ((i2 * i2) + (k * k))) * Math.sin(Math.toRadians((double) (f1 - StickerView.this.getRotation()))));
            int k2 = (i2 * 2) + StickerView.this.basew;
            int m = (j3 * 2) + StickerView.this.baseh;
            if (k2 > StickerView.this.f26s) {
                layoutParams.width = k2;
                layoutParams.leftMargin = StickerView.this.margl - i2;
            }
            if (m > StickerView.this.f26s) {
                layoutParams.height = m;
                layoutParams.topMargin = StickerView.this.margt - j3;
            }
            StickerView.this.setLayoutParams(layoutParams);
            StickerView.this.performLongClick();
        }
        return true;

    };
    public ImageView main_iv;


    public void deleteView() {
        deleteViews();
    }

    public int margin5dp = 2;
    int margl;
    int margt;
    private final View.OnTouchListener rotateTouchListener = (view, event) -> {
        StickerView rl = (StickerView) view.getParent();
        int action = event.getAction();
        if (action == 0) {
            if (rl != null) {
                rl.requestDisallowInterceptTouchEvent(true);
            }
            if (StickerView.this.listener != null) {
                StickerView.this.listener.onRotateDown(StickerView.this);
            }
            Rect rect = new Rect();
            ((View) view.getParent()).getGlobalVisibleRect(rect);
            StickerView.this.cX = rect.exactCenterX();
            StickerView.this.cY = rect.exactCenterY();
            StickerView.this.vAngle = (double) ((View) view.getParent()).getRotation();
            StickerView StickerView = StickerView.this;
            StickerView.tAngle = (Math.atan2((double) (StickerView.cY - event.getRawY()), (double) (StickerView.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
            StickerView StickerView2 = StickerView.this;
            StickerView2.dAngle = StickerView2.vAngle - StickerView.this.tAngle;
        } else if (action != 1) {
            if (action == 2) {
                if (rl != null) {
                    rl.requestDisallowInterceptTouchEvent(true);
                }
                if (StickerView.this.listener != null) {
                    StickerView.this.listener.onRotateMove(StickerView.this);
                }
                StickerView StickerView3 = StickerView.this;
                StickerView3.angle = (Math.atan2((double) (StickerView3.cY - event.getRawY()), (double) (StickerView.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                ((View) view.getParent()).setRotation((float) (StickerView.this.angle + StickerView.this.dAngle));
                ((View) view.getParent()).invalidate();
                ((View) view.getParent()).requestLayout();
            }
        } else if (StickerView.this.listener != null) {
            StickerView.this.listener.onRotateUp(StickerView.this);
        }
        return true;

    };

    public void deleteViews() {
        final ViewGroup viewGroup = (ViewGroup) StickerView.this.getParent();
        StickerView.this.zoomInScale.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(StickerView.this);
            }
        });
        viewGroup.removeView(StickerView.this);

        StickerView.this.main_iv.startAnimation(StickerView.this.zoomInScale);
        StickerView stickerView = StickerView.this;
        stickerView.isFisrtAnimation = true;
        stickerView.setBorderVisibility(false);
        if (StickerView.this.listener != null) {
            StickerView.this.listener.onDelete();
        }
    }

    private Uri resUri = null;
    private ImageView rotate_iv;
    private float rotation;
    Animation scale;
    private final int scaleRotateProg = 0;
    private ImageView scale_iv;
    int screenHeight = HttpStatus.SC_MULTIPLE_CHOICES;
    int screenWidth = HttpStatus.SC_MULTIPLE_CHOICES;

    public String stkr_path = "";
    double tAngle = 0.0d;
    private final float this_orgX = -1.0f;
    private final float this_orgY = -1.0f;

    public int topMargin = 0;
    double vAngle = 0.0d;


    public int wi;
    float widthMain = 0.0f;
    private final int xRotateProg = 0;
    private final int yRotateProg = 0;
    private float yRotation;
    private final int zRotateProg = 0;
    Animation zoomInScale;
    Animation zoomOutScale;

    UniversalDialog universalDialog;


    public interface TouchEventListener {
        void onDelete();

        void onEdit(View view, Uri uri);

        void onMidX(View view);

        void onMidXY(View view);

        void onMidY(View view);

        void onRotateDown(View view);

        void onRotateMove(View view);

        void onRotateUp(View view);

        void onScaleDown(View view);

        void onScaleMove(View view);

        void onScaleUp(View view);

        void onTouchDown(View view);

        void onTouchMove(View view);

        void onTouchMoveUpClick(View view);

        void onTouchUp(View view);

        void onXY(View view);
    }

    public boolean isFromAddText() {
        return this.isFromAddText;
    }

    public void setFromAddText(boolean z) {
        this.isFromAddText = z;
    }

    public boolean isFisrtAnimation() {
        return this.isFisrtAnimation;
    }

    public void setFisrtAnimation(boolean z) {
        this.isFisrtAnimation = z;
    }

    public StickerView(Activity context2) {
        super(context2);
        init(context2);
    }

    public StickerView(Activity context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        init(context2);
    }

    public StickerView(Activity context2, boolean z) {
        super(context2);
        this.isUndoRedo = z;
        init(context2);
    }

    public StickerView(Activity context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        init(context2);
    }

    public StickerView setOnTouchCallbackListener(TouchEventListener touchEventListener) {
        this.listener = touchEventListener;
        return this;
    }

    public void init(Activity context2) {
        this.context = context2;
        this.main_iv = new ImageView(this.context);
        this.scale_iv = new ImageView(this.context);
        this.border_iv = new ImageView(this.context);
        this.flip_iv = new ImageView(this.context);
        this.rotate_iv = new ImageView(this.context);
        this.delete_iv = new ImageView(this.context);
        this.f26s = (int) dpToPx(this.context, 25.0f);
        this.extraMargin = (int) dpToPx(this.context, 25.0f);
        this.margin5dp = (int) dpToPx(this.context, 2.5f);
        this.wi = dpToPx(this.context, 200);
        this.he = dpToPx(this.context, 200);

        universalDialog = new UniversalDialog(context2, false);


        this.scale_iv.setImageResource(R.drawable.sticker_scale);
        this.border_iv.setImageResource(R.drawable.sticker_border_gray);
        this.flip_iv.setImageResource(R.drawable.sticker_flip);
        this.rotate_iv.setImageResource(R.drawable.sticker_rotate);
        this.delete_iv.setImageResource(R.drawable.sticker_delete1);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.wi, this.he);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.setMargins(0, 0, 0, 0);
        if (Build.VERSION.SDK_INT >= 17) {
            layoutParams2.addRule(17);
        } else {
            layoutParams2.addRule(1);
        }
        int i = this.f26s;
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(i, i);
        layoutParams3.addRule(12);
        layoutParams3.addRule(11);
        layoutParams3.setMargins(5, 5, 5, 5);
        int i2 = this.f26s;
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(i2, i2);
        layoutParams4.addRule(10);
        layoutParams4.addRule(11);
        layoutParams4.setMargins(5, 5, 5, 5);
        int i3 = this.f26s;
        RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(i3, i3);
        layoutParams5.addRule(12);
        layoutParams5.addRule(9);
        layoutParams5.setMargins(5, 5, 5, 5);
        int i4 = this.f26s;
        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(i4, i4);
        layoutParams6.addRule(10);
        layoutParams6.addRule(9);
        layoutParams6.setMargins(5, 5, 5, 5);
        RelativeLayout.LayoutParams layoutParams7 = new RelativeLayout.LayoutParams(-1, -1);
        setLayoutParams(layoutParams);
        setBackgroundResource(R.drawable.sticker_gray1);
        addView(this.border_iv);
        this.border_iv.setLayoutParams(layoutParams7);
        this.border_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        this.border_iv.setTag("border_iv");
        addView(this.main_iv);
        this.main_iv.setLayoutParams(layoutParams2);
        addView(this.flip_iv);
        this.flip_iv.setLayoutParams(layoutParams4);
        this.flip_iv.setOnClickListener(view -> {
            ImageView imageView = StickerView.this.main_iv;
            float f = -180.0f;
            if (StickerView.this.main_iv.getRotationY() == -180.0f) {
                f = 0.0f;
            }
            imageView.setRotationY(f);
            StickerView.this.main_iv.invalidate();
            StickerView.this.requestLayout();
        });
        addView(this.rotate_iv);
        this.rotate_iv.setLayoutParams(layoutParams5);
        this.rotate_iv.setOnTouchListener(this.rotateTouchListener);
        addView(this.delete_iv);
        this.delete_iv.setLayoutParams(layoutParams6);
        this.delete_iv.setOnClickListener(new DeleteClick());
        addView(this.scale_iv);
        this.scale_iv.setLayoutParams(layoutParams3);
        this.scale_iv.setOnTouchListener(this.scaleTouchListener);
        this.scale_iv.setTag("scale_iv");
        this.rotation = getRotation();
        this.scale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_anim);
        this.zoomOutScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_out);
        this.zoomInScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_in);
        this.isMultiTouchEnabled = setDefaultTouchListener(true);
    }

    public boolean isFrameItem() {
        return isFrameItem;
    }

    public void setFrameItem(boolean b) {
        isFrameItem = b;
    }

    public boolean setDefaultTouchListener(boolean z) {
        if (z) {
            setOnTouchListener(new MultiTouchListener(context).enableRotation(true).setOnTouchCallbackListener(this));
            return true;
        }
        setOnTouchListener(null);
        return false;
    }

    public void setBorderVisibility(boolean z) {
        this.isBorderVisible = z;
        if (!z) {
            this.border_iv.setVisibility(View.GONE);
            this.scale_iv.setVisibility(View.GONE);
            this.flip_iv.setVisibility(View.GONE);
            this.rotate_iv.setVisibility(View.GONE);
            this.delete_iv.setVisibility(View.GONE);
            setBackgroundResource(0);
            if (this.isColorFilterEnable) {
                this.main_iv.setColorFilter(Color.parseColor("#303828"));
            }
        } else if (this.border_iv.getVisibility() != View.VISIBLE) {
            this.border_iv.setVisibility(View.VISIBLE);
            this.scale_iv.setVisibility(View.VISIBLE);
            this.flip_iv.setVisibility(View.VISIBLE);
            this.rotate_iv.setVisibility(View.VISIBLE);
            this.delete_iv.setVisibility(View.VISIBLE);
            setBackgroundResource(R.drawable.sticker_gray1);
            if (this.isFisrtAnimation || this.isFromAddText) {
                this.main_iv.startAnimation(this.scale);
            }
            this.isFisrtAnimation = true;
        }
    }

    public boolean getBorderVisbilty() {
        return this.isBorderVisible;
    }

    public void opecitySticker(int i) {
        try {
            this.main_iv.setAlpha(i);
            this.imgAlpha = i;
        } catch (Exception e) {
        }
    }

    public int getHueProg() {
        return this.hueProg;
    }

    public void setHueProg(int i) {
        this.hueProg = i;
        int i2 = this.hueProg;
        if (i2 == 0) {
            this.main_iv.setColorFilter(-1);
        } else if (i2 == 100) {
            this.main_iv.setColorFilter(ViewCompat.MEASURED_STATE_MASK);
        } else {
            this.main_iv.setColorFilter(ColorFilterGenerator.adjustHue((float) i));
        }
    }

    public String getColorType() {
        return this.colorType;
    }

    public int getAlphaProg() {
        return this.imgAlpha;
    }

    public void setAlphaProg(int i) {
        opecitySticker(i);
    }

    public int getColor() {
        return this.imgColor;
    }

    public void setColor(int i) {
        try {
            this.main_iv.setColorFilter(i);
            this.imgColor = i;
        } catch (Exception e) {
        }
    }

    public void setBgDrawable(String str) {
        Glide.with(this.context).load(getResources().getIdentifier(str, "drawable", this.context.getPackageName())).apply(new RequestOptions().dontAnimate().placeholder(R.drawable.spaceholder).error(R.drawable.spaceholder)).into(this.main_iv);
        this.drawableId = str;
        if (this.isFisrtAnimation || this.isFromAddText) {
            this.main_iv.startAnimation(this.zoomOutScale);
        }
        this.isFisrtAnimation = true;
    }

    public void setStrPath(String str) {
        try {


            if (!isFrameItem()) {

                this.btmp = ImageUtils.getResampleImageBitmap(Uri.parse(str), this.context, this.screenWidth > this.screenHeight ? this.screenWidth : this.screenHeight);

            } else {

                this.btmp = ImageUtils.resampleImage(str, this.screenWidth > this.screenHeight ? this.screenWidth : this.screenHeight);

            }

            this.main_iv.setImageBitmap(this.btmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.stkr_path = str;
        if (this.isFisrtAnimation || this.isFromAddText) {
            this.main_iv.startAnimation(this.zoomOutScale);
        }
        this.isFisrtAnimation = true;
    }

    public Uri getMainImageUri() {
        return this.resUri;
    }

    public void setMainImageUri(Uri uri) {
        this.resUri = uri;
        this.main_iv.setImageURI(this.resUri);
    }

    public Bitmap getMainImageBitmap() {
        return this.btmp;
    }

    public void setMainImageBitmap(Bitmap bitmap) {
        this.main_iv.setImageBitmap(bitmap);
    }

    public void optimize(float f, float f2) {
        setX(getX() * f);
        setY(getY() * f2);
        getLayoutParams().width = (int) (((float) this.wi) * f);
        getLayoutParams().height = (int) (((float) this.he) * f2);
    }

    public void optimizeScreen(float f, float f2) {
        this.screenHeight = (int) f2;
        this.screenWidth = (int) f;
    }

    public void setViewWH(float f, float f2) {
        this.widthMain = f;
        this.heightMain = f2;
    }

    public float getMainWidth() {
        return this.widthMain;
    }

    public float getMainHeight() {
        return this.heightMain;
    }

    public void incrX() {
        setX(getX() + 2.0f);
    }

    public void decX() {
        setX(getX() - 2.0f);
    }

    public void incrY() {
        setY(getY() + 2.0f);
    }

    public void decY() {
        setY(getY() - 2.0f);
    }

    public ElementInfoPoster getComponentInfo() {
        Bitmap bitmap = this.btmp;
        if (bitmap != null) {
            this.stkr_path = saveBitmapObject1(bitmap);
        }
        ElementInfoPoster elementInfo = new ElementInfoPoster();
        elementInfo.setPOS_X(getX());
        elementInfo.setPOS_Y(getY());
        elementInfo.setWIDTH(this.wi);
        elementInfo.setHEIGHT(this.he);
        elementInfo.setRES_ID(this.drawableId);
        elementInfo.setSTC_COLOR(this.imgColor);
        elementInfo.setRES_URI(this.resUri);
        elementInfo.setSTC_OPACITY(this.imgAlpha);
        elementInfo.setCOLORTYPE(this.colorType);
        elementInfo.setBITMAP(this.btmp);
        elementInfo.setROTATION(getRotation());
        elementInfo.setY_ROTATION(this.main_iv.getRotationY());
        elementInfo.setXRotateProg(this.xRotateProg);
        elementInfo.setYRotateProg(this.yRotateProg);
        elementInfo.setZRotateProg(this.zRotateProg);
        elementInfo.setScaleProg(this.scaleRotateProg);
        elementInfo.setSTKR_PATH(this.stkr_path);
        elementInfo.setSTC_HUE(this.hueProg);
        elementInfo.setFIELD_ONE(this.field_one);
        elementInfo.setFIELD_TWO(this.field_two);
        elementInfo.setFIELD_THREE(this.field_three);
        elementInfo.setFIELD_FOUR(this.field_four);
        return elementInfo;
    }

    public void setComponentInfo(ElementInfoPoster elementInfo) {
        this.wi = elementInfo.getWIDTH();
        this.he = elementInfo.getHEIGHT();
        this.drawableId = elementInfo.getRES_ID();
        this.resUri = elementInfo.getRES_URI();
        this.btmp = elementInfo.getBITMAP();
        this.rotation = elementInfo.getROTATION();
        this.imgColor = elementInfo.getSTC_COLOR();
        this.yRotation = elementInfo.getY_ROTATION();
        this.imgAlpha = elementInfo.getSTC_OPACITY();
        this.stkr_path = elementInfo.getSTKR_PATH();
        this.colorType = elementInfo.getCOLORTYPE();
        this.hueProg = elementInfo.getSTC_HUE();
        this.field_two = elementInfo.getFIELD_TWO();
        if (stkr_path!=null && !this.stkr_path.equals("")) {
            setStrPath(this.stkr_path);
        } else if (this.drawableId.equals("")) {
            this.main_iv.setImageBitmap(this.btmp);
        } else {
            setBgDrawable(this.drawableId);
        }
        if (this.colorType.equals("white")) {
            setColor(this.imgColor);
        } else {
            setHueProg(this.hueProg);
        }
        setRotation(this.rotation);
        opecitySticker(this.imgAlpha);
        if (this.field_two.equals("")) {
            getLayoutParams().width = this.wi;
            getLayoutParams().height = this.he;
            setX(elementInfo.getPOS_X());
            setY(elementInfo.getPOS_Y());
        } else {
            try {
                String[] split = this.field_two.split(",");
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                ((RelativeLayout.LayoutParams) getLayoutParams()).leftMargin = parseInt;
                ((RelativeLayout.LayoutParams) getLayoutParams()).topMargin = parseInt2;
                getLayoutParams().width = this.wi;
                getLayoutParams().height = this.he;
                setX(elementInfo.getPOS_X() + ((float) (parseInt * -1)));
                setY(elementInfo.getPOS_Y() + ((float) (parseInt2 * -1)));
            } catch (ArrayIndexOutOfBoundsException e) {
                getLayoutParams().width = this.wi;
                getLayoutParams().height = this.he;
                setX(elementInfo.getPOS_X());
                setY(elementInfo.getPOS_Y());
                e.printStackTrace();
            }
        }
        if (elementInfo.getTYPE() == "SHAPE") {
            this.flip_iv.setVisibility(View.GONE);
        }
        if (elementInfo.getTYPE() == "STICKER") {
            this.flip_iv.setVisibility(View.VISIBLE);
        }
        this.main_iv.setRotationY(this.yRotation);
    }


    public String saveBitmapObject1(Bitmap bitmap) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), ".Poster Maker Stickers/category1");
        file.mkdirs();
        File file2 = new File(file, "raw1-" + System.currentTimeMillis() + ".png");
        String absolutePath = file2.getAbsolutePath();
        if (file2.exists()) {
            file2.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            return absolutePath;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("testing", "Exception" + e.getMessage());
            return "";
        }
    }

    public int dpToPx(Context context2, int i) {
        context2.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    public float dpToPx(Context context2, float f) {
        context2.getResources();
        return (float) Math.round(f * Resources.getSystem().getDisplayMetrics().density);
    }

    private double getLength(double d, double d2, double d3, double d4) {
        return Math.sqrt(Math.pow(d4 - d2, 2.0d) + Math.pow(d3 - d, 2.0d));
    }

    public void enableColorFilter(boolean z) {
        this.isColorFilterEnable = z;
    }

    public void onTouchCallback(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchDown(view);
        }
    }

    public void onTouchUpCallback(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchUp(view);
        }
    }

    public void onTouchMoveCallback(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchMove(view);
        }
    }

    public void onMidX(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onMidX(view);
        }
    }

    public void onMidY(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onMidY(view);
        }
    }

    public void onMidXY(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onMidXY(view);
        }
    }

    public void onXY(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onXY(view);
        }
    }

    public void onTouchUpClick(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchMoveUpClick(view);
        }
    }

    class DeleteClick implements View.OnClickListener {
        DeleteClick() {
        }

        public void onClick(View view) {


            universalDialog.showDeleteDialog(context.getString(R.string.delete), context.getString(R.string.sure_delete), "Delete", "Cancel");

            universalDialog.okBtn.setOnClickListener(v -> {

                final ViewGroup parent = (ViewGroup) StickerView.this.getParent();
                parent.removeView(StickerView.this);
                universalDialog.cancel();
            });

            universalDialog.cancelBtn.setOnClickListener(v -> universalDialog.cancel());

            universalDialog.show();

        }
    }

    class RingProgressClick implements DialogInterface.OnDismissListener {
        public void onDismiss(DialogInterface dialogInterface) {
        }

        RingProgressClick() {
        }
    }

    public void clickToSaveWork() {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchMoveUpClick(this);
        }
    }

}
