package com.astarconcepts.brandbanao.custom.poster.views.eraser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.cookie.ClientCookie;


public class StickerRemoveActivity extends Activity implements View.OnClickListener {
    private static final String MyPREFERENCES = "MyPrefs";
    public static Bitmap bgCircleBit = null;
    public static Bitmap bitmap = null;
    public static int curBgType = 1;
    public static int orgBitHeight;
    public static int orgBitWidth;
    public static BitmapShader patternBMPshader;
    public static Uri selectedImageUri;
    private final View[] btnArr = new View[5];
    private final View[] btnArr1 = new View[5];
    public ImageButton auto_btn;
    public RelativeLayout auto_btn_rel;
    public Bitmap b = null;
    public EraseView dv;
    public int height;
    public boolean isTutOpen = true;
    public RelativeLayout offset_seekbar_lay;
    public Bitmap orgBitmap;
    public ImageButton redo_btn;
    public RelativeLayout rel_arrow_up;
    public RelativeLayout rel_auto;
    public RelativeLayout rel_down_btns;
    public RelativeLayout rel_seek_container;
    public RelativeLayout rel_up_btns;
    public Animation scale_anim;
    public boolean showDialog = false;
    public ImageView tbg_img;
    public TextView txt_redo;
    public TextView txt_undo;
    public ImageButton undo_btn;
    public int width;
    boolean adFlag = false;
    int id;
    Typeface ttf;
    TextView tv1;
    TextView tv10;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    TextView tv7;
    TextView tv8;
    TextView tv9;
    private Animation animSlideDown;
    private Animation animSlideUp;
    private ImageButton btn_bg;
    private ImageButton btn_brush;
    private ImageView dv1;
    private ImageButton erase_btn;
    private RelativeLayout erase_btn_rel;
    private TextView headertext;
    private RelativeLayout inside_cut_lay;
    private ImageButton lasso_btn;
    private RelativeLayout lasso_btn_rel;
    private LinearLayout lay_lasso_cut;
    private LinearLayout lay_offset_seek;
    private LinearLayout lay_threshold_seek;
    private RelativeLayout main_rel;
    private SeekBar offset_seekbar;
    private SeekBar offset_seekbar1;
    private SeekBar offset_seekbar2;
    private RelativeLayout outside_cut_lay;
    private SeekBar radius_seekbar;
    private RelativeLayout rel_bw;
    private RelativeLayout rel_color;
    private RelativeLayout rel_desc;
    private RelativeLayout rel_lasso;
    private ImageButton restore_btn;
    private RelativeLayout restore_btn_rel;
    private SeekBar threshold_seekbar;
    private TextView txt_desc;
    private ImageButton zoom_btn;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int i = 1;
        if (intrinsicWidth <= 0) {
            intrinsicWidth = 1;
        }
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicHeight > 0) {
            i = intrinsicHeight;
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }


    @SuppressLint({"WrongConstant"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_eraser);
        if (getIntent() != null) {
            this.id = getIntent().getExtras().getInt("id");
        }
        this.animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        this.animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        this.scale_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim);
        this.ttf = Typeface.createFromAsset(getAssets(), "font/Montserrat-Medium.ttf");
        initUI();
        ((TextView) findViewById(R.id.headertext)).setTypeface(this.ttf);
        ((TextView) findViewById(R.id.txt_offset)).setTypeface(this.ttf);
        ((TextView) findViewById(R.id.txt_offset1)).setTypeface(this.ttf);
        ((TextView) findViewById(R.id.txt_offset2)).setTypeface(this.ttf);
        ((TextView) findViewById(R.id.txt_radius)).setTypeface(this.ttf);
        ((TextView) findViewById(R.id.txt_threshold)).setTypeface(this.ttf);
        ((TextView) findViewById(R.id.txt_inside)).setTypeface(this.ttf);
        ((TextView) findViewById(R.id.txt_outside)).setTypeface(this.ttf);
        this.tv1 = findViewById(R.id.txt_auto1);
        this.tv1.setTypeface(this.ttf);
        this.tv2 = findViewById(R.id.txt_lasso1);
        this.tv2.setTypeface(this.ttf);
        this.tv3 = findViewById(R.id.txt_erase1);
        this.tv3.setTypeface(this.ttf);
        this.tv4 = findViewById(R.id.txt_restore1);
        this.tv4.setTypeface(this.ttf);
        this.tv5 = findViewById(R.id.txt_zoom1);
        this.tv5.setTypeface(this.ttf);
        this.tv6 = findViewById(R.id.auto_txt);
        this.tv1.setTypeface(this.ttf);
        this.tv7 = findViewById(R.id.txt_lasso);
        this.tv2.setTypeface(this.ttf);
        this.tv8 = findViewById(R.id.erase_txt);
        this.tv3.setTypeface(this.ttf);
        this.tv9 = findViewById(R.id.restore_txt);
        this.tv4.setTypeface(this.ttf);
        this.tv10 = findViewById(R.id.zoom_txt);
        this.tv5.setTypeface(this.ttf);
        this.isTutOpen = false;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        this.height = i - ImageUtils.dpToPx(this, 120.0f);
        curBgType = 1;
        this.main_rel.postDelayed(() -> {
            if (StickerRemoveActivity.this.isTutOpen) {
                ImageView tbgImg = StickerRemoveActivity.this.tbg_img;
                StickerRemoveActivity stickerRemoveActivity = StickerRemoveActivity.this;
                tbgImg.setImageBitmap(ImageUtils.getTiledBitmap(stickerRemoveActivity, R.drawable.tbg3, stickerRemoveActivity.width, StickerRemoveActivity.this.height));
                StickerRemoveActivity.bgCircleBit = ImageUtils.getBgCircleBit(StickerRemoveActivity.this, R.drawable.tbg3);
            } else {
                ImageView tbg_img = StickerRemoveActivity.this.tbg_img;
                StickerRemoveActivity stickerRemoveActivity2 = StickerRemoveActivity.this;
                tbg_img.setImageBitmap(ImageUtils.getTiledBitmap(stickerRemoveActivity2, R.drawable.tbg, stickerRemoveActivity2.width, StickerRemoveActivity.this.height));
                StickerRemoveActivity.bgCircleBit = ImageUtils.getBgCircleBit(StickerRemoveActivity.this, R.drawable.tbg);
            }
            StickerRemoveActivity stickerRemoveActivity3 = StickerRemoveActivity.this;
            stickerRemoveActivity3.importImageFromUri(stickerRemoveActivity3.getIntent().getData());
        }, 1000);
    }

    private void initUI() {
        this.rel_arrow_up = findViewById(R.id.up_rel_arrow);
        this.rel_auto = findViewById(R.id.rel_auto);
        this.rel_color = findViewById(R.id.rel_color);
        this.rel_bw = findViewById(R.id.rel_bw);
        this.rel_lasso = findViewById(R.id.rel_lasso);

        this.rel_desc = findViewById(R.id.rel_desc);
        this.offset_seekbar_lay = findViewById(R.id.offset_seekbar_lay);
        this.rel_seek_container = findViewById(R.id.rel_seek_container);
        this.auto_btn_rel = findViewById(R.id.rel_auto_btn);
        this.erase_btn_rel = findViewById(R.id.rel_erase_btn);
        this.restore_btn_rel = findViewById(R.id.rel_restore_btn);
        this.lasso_btn_rel = findViewById(R.id.rel_lasso_btn);

        this.headertext = findViewById(R.id.headertext);
        this.txt_desc = findViewById(R.id.txt_desc);
        this.main_rel = findViewById(R.id.main_rel);
        this.lay_threshold_seek = findViewById(R.id.lay_threshold_seek);
        this.lay_offset_seek = findViewById(R.id.lay_offset_seek);
        this.lay_lasso_cut = findViewById(R.id.lay_lasso_cut);
        this.inside_cut_lay = findViewById(R.id.inside_cut_lay);
        this.outside_cut_lay = findViewById(R.id.outside_cut_lay);
        this.undo_btn = findViewById(R.id.btn_undo);
        this.redo_btn = findViewById(R.id.btn_redo);
        ImageButton btn_up = findViewById(R.id.btn_up);
        this.rel_up_btns = findViewById(R.id.rv_up);
        this.rel_down_btns = findViewById(R.id.rel_down_btns);

        RelativeLayout rel_down = findViewById(R.id.rel_down);
        this.auto_btn = findViewById(R.id.btn_auto);
        this.erase_btn = findViewById(R.id.btn_erase);
        this.restore_btn = findViewById(R.id.btn_restore);
        this.lasso_btn = findViewById(R.id.btn_lasso);
        this.zoom_btn = findViewById(R.id.btn_zoom);
        ImageView back_btn = findViewById(R.id.btn_back);
        ImageButton save_btn = findViewById(R.id.save_image_btn);
        this.btn_bg = findViewById(R.id.btn_bg);
        this.btn_brush = findViewById(R.id.btn_brush);
        this.tbg_img = findViewById(R.id.tbg_img);
        this.txt_undo = findViewById(R.id.txt_undo);
        this.txt_redo = findViewById(R.id.txt_redo);
        btn_up.setOnClickListener(this);
        rel_down.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        this.undo_btn.setOnClickListener(this);
        this.redo_btn.setOnClickListener(this);
        this.undo_btn.setEnabled(false);
        this.redo_btn.setEnabled(false);
        save_btn.setOnClickListener(this);
        this.btn_bg.setOnClickListener(this);
        this.erase_btn.setOnClickListener(this);
        this.auto_btn.setOnClickListener(this);
        this.lasso_btn.setOnClickListener(this);
        this.restore_btn.setOnClickListener(this);
        this.zoom_btn.setOnClickListener(this);
        this.inside_cut_lay.setOnClickListener(this);
        this.outside_cut_lay.setOnClickListener(this);
        this.btnArr[0] = findViewById(R.id.lay_auto_btn);
        this.btnArr[1] = findViewById(R.id.lay_erase_btn);
        this.btnArr[2] = findViewById(R.id.lay_restore_btn);
        this.btnArr[3] = findViewById(R.id.lay_lasso_btn);
        this.btnArr[4] = findViewById(R.id.lay_zoom_btn);
        this.btnArr1[0] = findViewById(R.id.auto_btn_lay1);
        this.btnArr1[1] = findViewById(R.id.erase_btn_lay1);
        this.btnArr1[2] = findViewById(R.id.restore_btn_lay1);
        this.btnArr1[3] = findViewById(R.id.lasso_btn_lay1);
        this.btnArr1[4] = findViewById(R.id.zoom_btn_lay1);
        this.offset_seekbar = findViewById(R.id.offset_seekbar);
        this.offset_seekbar1 = findViewById(R.id.offset_seekbar1);
        this.offset_seekbar2 = findViewById(R.id.offset_seekbar2);
        SeekBar.OnSeekBarChangeListener r0 = new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (StickerRemoveActivity.this.dv != null) {
                    StickerRemoveActivity.this.dv.setOffset(i - 150);
                    StickerRemoveActivity.this.dv.invalidate();
                }
            }
        };
        this.offset_seekbar.setOnSeekBarChangeListener(r0);
        this.offset_seekbar1.setOnSeekBarChangeListener(r0);
        this.offset_seekbar2.setOnSeekBarChangeListener(r0);
        this.radius_seekbar = findViewById(R.id.radius_seekbar);
        this.radius_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (StickerRemoveActivity.this.dv != null) {
                    StickerRemoveActivity.this.dv.setRadius(i + 2);
                    StickerRemoveActivity.this.dv.invalidate();
                }
            }
        });
        this.threshold_seekbar = findViewById(R.id.threshold_seekbar);
        this.threshold_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (StickerRemoveActivity.this.dv != null) {
                    StickerRemoveActivity.this.dv.setThreshold(seekBar.getProgress() + 10);
                    StickerRemoveActivity.this.dv.updateThreshHold();
                }
            }
        });
    }

    @SuppressLint({"WrongConstant"})
    public void onClick(View view) {
        if (this.dv != null || view.getId() == R.id.btn_back) {
            switch (view.getId()) {
                case R.id.auto_btn_lay1:
                case R.id.btn_auto:
                case R.id.rel_auto_btn:
                    setSelected(R.id.lay_auto_btn);
                    setClickUnSelected();
                    this.auto_btn.setBackgroundResource(R.drawable.ic_slated_auto);
                    this.tv1.setTextColor(getResources().getColor(R.color.select_new));
                    this.tv6.setTextColor(getResources().getColor(R.color.select_new));
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(2);
                    this.dv.invalidate();
                    if (view.getId() != R.id.auto_btn_lay1) {
                        showButtonsLayout(false);
                        return;
                    }
                    return;
                case R.id.btn_back:
                    onBackPressed();
                    return;
                case R.id.btn_bg:
                    changeBG();
                    return;
                case R.id.btn_brush:
                    EraseView eraseView = this.dv;
                    if (eraseView != null) {
                        if (eraseView.isRectBrushEnable()) {
                            this.dv.enableRectBrush(false);
                            this.dv.invalidate();
                            this.btn_brush.setBackgroundResource(R.drawable.ic_square);
                            return;
                        }
                        this.dv.enableRectBrush(true);
                        this.btn_brush.setBackgroundResource(R.mipmap.ic_launcher_round);
                        this.dv.invalidate();
                        return;
                    }
                    return;
                case R.id.btn_erase:
                case R.id.erase_btn_lay1:
                case R.id.rel_erase_btn:
                    setSelected(R.id.lay_erase_btn);
                    setClickUnSelected();
                    this.erase_btn.setBackgroundResource(R.drawable.ic_slated_manual);
                    this.tv3.setTextColor(getResources().getColor(R.color.select_new));
                    this.tv8.setTextColor(getResources().getColor(R.color.select_new));
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(1);
                    this.dv.invalidate();
                    if (view.getId() != R.id.erase_btn_lay1) {
                        showButtonsLayout(false);
                    }
                    if (this.isTutOpen) {
                        this.rel_color.setVisibility(View.GONE);
                        this.rel_color.clearAnimation();
                        this.txt_desc.setText(getResources().getString(R.string.drag_color));
                        this.rel_desc.setVisibility(View.VISIBLE);
                        this.rel_desc.startAnimation(this.scale_anim);
                        this.erase_btn_rel.clearAnimation();
                        return;
                    }
                    return;
                case R.id.btn_lasso:
                case R.id.lasso_btn_lay1:
                case R.id.rel_lasso_btn:
                    this.offset_seekbar_lay.setVisibility(View.VISIBLE);
                    setSelected(R.id.lay_lasso_btn);
                    setClickUnSelected();
                    this.lasso_btn.setBackgroundResource(R.drawable.ic_slated_extract);
                    this.tv2.setTextColor(getResources().getColor(R.color.select_new));
                    this.tv7.setTextColor(getResources().getColor(R.color.select_new));
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(3);
                    this.dv.invalidate();
                    if (view.getId() != R.id.lasso_btn_lay1) {
                        showButtonsLayout(false);
                    }
                    if (this.isTutOpen) {
                        this.rel_lasso.setVisibility(View.GONE);
                        this.rel_lasso.clearAnimation();
                        this.txt_desc.setText(getResources().getString(R.string.msg_draw_lasso));
                        this.rel_desc.setVisibility(View.VISIBLE);
                        this.rel_desc.startAnimation(this.scale_anim);
                        this.lasso_btn_rel.clearAnimation();
                        return;
                    }
                    return;
                case R.id.btn_redo:
                    final ProgressDialog show = ProgressDialog.show(this, "", getString(R.string.redoing) + "...", true);
                    show.setCancelable(false);
                    new Thread(() -> {
                        try {
                            StickerRemoveActivity.this.runOnUiThread(() -> StickerRemoveActivity.this.dv.redoChange());
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        show.dismiss();
                    }).start();
                    return;
                case R.id.btn_restore:
                case R.id.rel_restore_btn:
                case R.id.restore_btn_lay1:
                    setSelected(R.id.lay_restore_btn);
                    setClickUnSelected();
                    this.restore_btn.setBackgroundResource(R.drawable.ic_slated_restore);
                    this.tv4.setTextColor(getResources().getColor(R.color.select_new));
                    this.tv9.setTextColor(getResources().getColor(R.color.select_new));
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(4);
                    this.dv.invalidate();
                    if (view.getId() != R.id.restore_btn_lay1) {
                        showButtonsLayout(false);
                    }
                    if (this.isTutOpen) {
                        this.rel_bw.setVisibility(View.GONE);
                        this.rel_bw.clearAnimation();
                        this.txt_desc.setText(getResources().getString(R.string.drag_bw));
                        this.rel_desc.setVisibility(View.VISIBLE);
                        this.rel_desc.startAnimation(this.scale_anim);
                        this.restore_btn_rel.clearAnimation();
                        return;
                    }
                    return;
                case R.id.btn_undo:
                    final ProgressDialog show2 = ProgressDialog.show(this, "", getString(R.string.undoing) + "...", true);
                    show2.setCancelable(false);
                    new Thread(() -> {
                        try {
                            StickerRemoveActivity.this.runOnUiThread(() -> StickerRemoveActivity.this.dv.undoChange());
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        show2.dismiss();
                    }).start();
                    return;
                case R.id.btn_up:
                    showButtonsLayout(true);
                    return;
                case R.id.btn_zoom:
                case R.id.rel_zoom_btn:
                case R.id.zoom_btn_lay1:
                    this.dv.enableTouchClear(false);
                    this.main_rel.setOnTouchListener(new MultiTouchListener());
                    setSelected(R.id.lay_zoom_btn);
                    setClickUnSelected();
                    this.zoom_btn.setBackgroundResource(R.drawable.ic_slated_zoom);
                    this.tv5.setTextColor(getResources().getColor(R.color.select_new));
                    this.tv10.setTextColor(getResources().getColor(R.color.select_new));
                    this.dv.setMODE(0);
                    this.dv.invalidate();
                    if (view.getId() != R.id.zoom_btn_lay1) {
                        showButtonsLayout(false);
                        return;
                    }
                    return;
                case R.id.inside_cut_lay:
                    this.offset_seekbar_lay.setVisibility(View.VISIBLE);
                    this.dv.enableInsideCut(true);
                    this.inside_cut_lay.clearAnimation();
                    this.outside_cut_lay.clearAnimation();
                    return;
                case R.id.outside_cut_lay:
                    this.offset_seekbar_lay.setVisibility(View.VISIBLE);
                    this.dv.enableInsideCut(false);
                    this.inside_cut_lay.clearAnimation();
                    this.outside_cut_lay.clearAnimation();
                    return;
                case R.id.rel_down:
                    showButtonsLayout(false);
                    return;
                case R.id.save_image_btn:
                    bitmap = this.dv.getFinalBitmap();
                    if (bitmap != null) {
                        try {
                            int dpToPx = ImageUtils.dpToPx(this, 42.0f);
                            bitmap = ImageUtils.resizeBitmap(bitmap, orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx);
                            int i = dpToPx + dpToPx;
                            bitmap = Bitmap.createBitmap(bitmap, dpToPx, dpToPx, bitmap.getWidth() - i, bitmap.getHeight() - i);
                            bitmap = Bitmap.createScaledBitmap(bitmap, orgBitWidth, orgBitHeight, true);
                            bitmap = ImageUtils.bitmapmasking(this.orgBitmap, bitmap);
                            setResult(-1, new Intent().putExtra("id", this.id).putExtra(ClientCookie.PATH_ATTR, saveBitmapObject1(bitmap)));
                            finish();
                            return;
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        finish();
                        return;
                    }
                default:
                    return;
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.import_img_warning), Toast.LENGTH_SHORT).show();
        }
    }

    private String saveBitmapObject1(Bitmap bitmap2) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), ".Poster Maker Stickers/category1");
        file.mkdirs();
        File file2 = new File(file, "raw1-" + System.currentTimeMillis() + ".png");
        String absolutePath = file2.getAbsolutePath();
        if (file2.exists()) {
            file2.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            return absolutePath;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("testing", "Exception" + e.getMessage());
            return "";
        }
    }

    public void onBackPressed() {
        AlertDialog create = new AlertDialog.Builder(this, 16974126).setTitle(getResources().getString(R.string.alert)).setIcon(R.mipmap.ic_launcher).setMessage(getResources().getString(R.string.exit_cut_page)).setNegativeButton(getResources().getString(R.string.yes), (dialogInterface, i) -> StickerRemoveActivity.this.finish()).setPositiveButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.cancel()).create();
        create.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_;
        create.show();
    }

    private void changeBG() {
        int i = curBgType;
        if (i == 1) {
            curBgType = 2;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg1, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg2);
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg1);
        } else if (i == 2) {
            curBgType = 3;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg2, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg3);
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg2);
        } else if (i == 3) {
            curBgType = 4;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg3, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg4);
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg3);
        } else if (i == 4) {
            curBgType = 5;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg4, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg5);
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg4);
        } else if (i == 5) {
            curBgType = 6;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg5, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg);
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg5);
        } else if (i == 6) {
            curBgType = 1;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg1);
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg);
        }
    }

    @SuppressLint({"WrongConstant"})
    private void showButtonsLayout(boolean z) {
        if (z) {
            if (this.rel_up_btns.getVisibility() != View.VISIBLE) {
                this.rel_up_btns.setVisibility(View.VISIBLE);
                this.rel_up_btns.startAnimation(this.animSlideUp);
                this.animSlideUp.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    @SuppressLint({"WrongConstant"})
                    public void onAnimationEnd(Animation animation) {
                        StickerRemoveActivity.this.rel_down_btns.setVisibility(View.INVISIBLE);
                        if (StickerRemoveActivity.this.isTutOpen) {
                            StickerRemoveActivity.this.rel_arrow_up.setVisibility(View.GONE);
                            StickerRemoveActivity.this.rel_arrow_up.clearAnimation();
                            StickerRemoveActivity.this.rel_auto.setVisibility(View.VISIBLE);
                            StickerRemoveActivity.this.rel_auto.startAnimation(StickerRemoveActivity.this.scale_anim);
                            StickerRemoveActivity.this.auto_btn.setEnabled(true);
                            StickerRemoveActivity.this.auto_btn_rel.startAnimation(StickerRemoveActivity.this.scale_anim);
                        }
                    }
                });
            }
        } else if (!this.isTutOpen && this.rel_up_btns.getVisibility() == View.VISIBLE) {
            this.rel_up_btns.startAnimation(this.animSlideDown);
            this.animSlideDown.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                    StickerRemoveActivity.this.rel_down_btns.setVisibility(View.VISIBLE);
                }

                public void onAnimationEnd(Animation animation) {
                    StickerRemoveActivity.this.rel_up_btns.setVisibility(View.GONE);
                }
            });
        }
    }


    public void importImageFromUri(Uri uri) {
        selectedImageUri = uri;
        this.showDialog = false;
        final ProgressDialog show = ProgressDialog.show(this, "", getResources().getString(R.string.importing_image), true);
        show.setCancelable(false);
        new Thread(() -> {
            try {
                if (StickerRemoveActivity.this.isTutOpen) {
                    StickerRemoveActivity.this.b = BitmapFactory.decodeResource(StickerRemoveActivity.this.getResources(), R.drawable.demo_img);
                    StickerRemoveActivity.this.b = ImageUtils.resizeBitmap(StickerRemoveActivity.this.b, StickerRemoveActivity.this.width, StickerRemoveActivity.this.height);
                } else {
                    if (!Constant.uri.equals("")) {
                        StickerRemoveActivity.this.b = BitmapFactory.decodeFile(Constant.uri);
                    } else if (Constant.rewid.equals("")) {
                        StickerRemoveActivity.this.b = Constant.bitmapSticker;
                    } else {
                        StickerRemoveActivity.this.b = StickerRemoveActivity.drawableToBitmap(StickerRemoveActivity.this.getResources().getDrawable(Integer.valueOf(StickerRemoveActivity.this.getResources().getIdentifier(Constant.rewid, "drawable", StickerRemoveActivity.this.getPackageName())).intValue()));
                    }
                    if (StickerRemoveActivity.this.b.getWidth() > StickerRemoveActivity.this.width || StickerRemoveActivity.this.b.getHeight() > StickerRemoveActivity.this.height || (StickerRemoveActivity.this.b.getWidth() < StickerRemoveActivity.this.width && StickerRemoveActivity.this.b.getHeight() < StickerRemoveActivity.this.height)) {
                        StickerRemoveActivity.this.b = ImageUtils.resizeBitmap(StickerRemoveActivity.this.b, StickerRemoveActivity.this.width, StickerRemoveActivity.this.height);
                    }
                }
                if (StickerRemoveActivity.this.b == null) {
                    StickerRemoveActivity.this.showDialog = true;
                } else {
                    StickerRemoveActivity.this.orgBitmap = StickerRemoveActivity.this.b.copy(StickerRemoveActivity.this.b.getConfig(), true);
                    int dpToPx = ImageUtils.dpToPx(StickerRemoveActivity.this, 42.0f);
                    StickerRemoveActivity.orgBitWidth = StickerRemoveActivity.this.b.getWidth();
                    StickerRemoveActivity.orgBitHeight = StickerRemoveActivity.this.b.getHeight();
                    Bitmap createBitmap = Bitmap.createBitmap(StickerRemoveActivity.this.b.getWidth() + dpToPx + dpToPx, StickerRemoveActivity.this.b.getHeight() + dpToPx + dpToPx, StickerRemoveActivity.this.b.getConfig());
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawColor(0);
                    float f = (float) dpToPx;
                    canvas.drawBitmap(StickerRemoveActivity.this.b, f, f, null);
                    StickerRemoveActivity.this.b = createBitmap;
                    if (StickerRemoveActivity.this.b.getWidth() > StickerRemoveActivity.this.width || StickerRemoveActivity.this.b.getHeight() > StickerRemoveActivity.this.height || (StickerRemoveActivity.this.b.getWidth() < StickerRemoveActivity.this.width && StickerRemoveActivity.this.b.getHeight() < StickerRemoveActivity.this.height)) {
                        StickerRemoveActivity.this.b = ImageUtils.resizeBitmap(StickerRemoveActivity.this.b, StickerRemoveActivity.this.width, StickerRemoveActivity.this.height);
                    }
                }
                Thread.sleep(1000);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
                StickerRemoveActivity.this.showDialog = true;
                show.dismiss();
            }
            show.dismiss();
        }).start();
        show.setOnDismissListener(dialogInterface -> {
            if (StickerRemoveActivity.this.showDialog) {
                StickerRemoveActivity stickerRemoveActivity = StickerRemoveActivity.this;
                Toast.makeText(stickerRemoveActivity, stickerRemoveActivity.getResources().getString(R.string.import_error), Toast.LENGTH_SHORT).show();
                StickerRemoveActivity.this.finish();
                return;
            }
            Constant.rewid = "";
            Constant.uri = "";
            Constant.bitmapSticker = null;
            StickerRemoveActivity.this.setImageBitmap();
        });
    }


    @SuppressLint({"WrongConstant"})
    public void setImageBitmap() {
        this.dv = new EraseView(this);
        this.dv1 = new ImageView(this);
        this.dv.setImageBitmap(this.b);
        this.dv1.setImageBitmap(getGreenLayerBitmap(this.b));
        this.dv.enableTouchClear(false);
        this.dv.setMODE(0);
        this.dv.invalidate();
        this.offset_seekbar.setProgress(225);
        this.radius_seekbar.setProgress(18);
        this.threshold_seekbar.setProgress(20);
        RelativeLayout relativeLayout = findViewById(R.id.main_rel_parent);
        EraseSView eraseSView = new EraseSView(this);
        eraseSView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        relativeLayout.addView(eraseSView);
        this.dv.setEraseSView(eraseSView);
        this.main_rel.removeAllViews();
        this.main_rel.setScaleX(1.0f);
        this.main_rel.setScaleY(1.0f);
        this.main_rel.addView(this.dv1);
        this.main_rel.addView(this.dv);
        relativeLayout.setLayoutParams(relativeLayout.getLayoutParams());
        this.dv.invalidate();
        this.dv1.setVisibility(View.GONE);
        this.dv.setUndoRedoListener(new EraseView.UndoRedoListener() {
            public void enableUndo(boolean z, int i) {
                if (z) {
                    StickerRemoveActivity stickerRemoveActivity = StickerRemoveActivity.this;
                    stickerRemoveActivity.setBGDrawable(stickerRemoveActivity.txt_undo, i, StickerRemoveActivity.this.undo_btn, R.drawable.ic_undo, z);
                    return;
                }
                StickerRemoveActivity stickerRemoveActivity2 = StickerRemoveActivity.this;
                stickerRemoveActivity2.setBGDrawable(stickerRemoveActivity2.txt_undo, i, StickerRemoveActivity.this.undo_btn, R.drawable.ic_undo_1, z);
            }

            public void enableRedo(boolean z, int i) {
                if (z) {
                    StickerRemoveActivity stickerRemoveActivity = StickerRemoveActivity.this;
                    stickerRemoveActivity.setBGDrawable(stickerRemoveActivity.txt_redo, i, StickerRemoveActivity.this.redo_btn, R.drawable.ic_redo, z);
                    return;
                }
                StickerRemoveActivity stickerRemoveActivity2 = StickerRemoveActivity.this;
                stickerRemoveActivity2.setBGDrawable(stickerRemoveActivity2.txt_redo, i, StickerRemoveActivity.this.redo_btn, R.drawable.ic_redo_1, z);
            }
        });
        this.b.recycle();
        this.dv.setActionListener(new EraseView.ActionListener() {
            public void onActionCompleted(final int i) {
                StickerRemoveActivity.this.runOnUiThread(() -> {
                    if (i == 5) {
                        StickerRemoveActivity.this.offset_seekbar_lay.setVisibility(View.GONE);
                    }
                });
            }

            public void onAction(final int i) {
                StickerRemoveActivity.this.runOnUiThread(() -> {
                    if (i == 0) {
                        StickerRemoveActivity.this.rel_seek_container.setVisibility(View.GONE);
                    }
                    if (i == 1) {
                        StickerRemoveActivity.this.rel_seek_container.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    public void setBGDrawable(TextView textView, int i, ImageView imageView, int i2, boolean z) {
        final ImageView imageView2 = imageView;
        final int i3 = i2;
        final boolean z2 = z;
        final TextView textView2 = textView;
        final int i4 = i;
        runOnUiThread(() -> {
            imageView2.setBackgroundResource(i3);
            imageView2.setEnabled(z2);
            textView2.setText(String.valueOf(i4));
        });
    }

    public Bitmap getGreenLayerBitmap(Bitmap bitmap2) {
        Paint paint = new Paint();
        paint.setColor(-16711936);
        paint.setAlpha(80);
        int dpToPx = ImageUtils.dpToPx(this, 42.0f);
        Bitmap createBitmap = Bitmap.createBitmap(orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx, bitmap2.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(0);
        float f = (float) dpToPx;
        canvas.drawBitmap(this.orgBitmap, f, f, null);
        canvas.drawRect(f, f, (float) (orgBitWidth + dpToPx), (float) (orgBitHeight + dpToPx), paint);
        Bitmap createBitmap2 = Bitmap.createBitmap(orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx, bitmap2.getConfig());
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawColor(0);
        canvas2.drawBitmap(this.orgBitmap, f, f, null);
        patternBMPshader = new BitmapShader(ImageUtils.resizeBitmap(createBitmap2, this.width, this.height), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return ImageUtils.resizeBitmap(createBitmap, this.width, this.height);
    }


    public void setClickUnSelected() {
        this.auto_btn.setBackgroundResource(R.drawable.ic_auto);
        this.lasso_btn.setBackgroundResource(R.drawable.ic_extract);
        this.erase_btn.setBackgroundResource(R.drawable.ic_manual);
        this.restore_btn.setBackgroundResource(R.drawable.ic_restore);
        this.zoom_btn.setBackgroundResource(R.drawable.ic_zoom);
        this.tv1.setTextColor(getResources().getColor(R.color.white));
        this.tv2.setTextColor(getResources().getColor(R.color.white));
        this.tv3.setTextColor(getResources().getColor(R.color.white));
        this.tv4.setTextColor(getResources().getColor(R.color.white));
        this.tv5.setTextColor(getResources().getColor(R.color.white));
        this.tv6.setTextColor(getResources().getColor(R.color.white));
        this.tv7.setTextColor(getResources().getColor(R.color.white));
        this.tv8.setTextColor(getResources().getColor(R.color.white));
        this.tv9.setTextColor(getResources().getColor(R.color.white));
        this.tv10.setTextColor(getResources().getColor(R.color.white));
    }

    @SuppressLint({"WrongConstant"})
    public void setSelected(int i) {
        if (i == R.id.lay_erase_btn) {
            this.offset_seekbar.setProgress(this.dv.getOffset() + 150);
            this.lay_offset_seek.setVisibility(View.VISIBLE);
            this.lay_threshold_seek.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
            this.headertext.setText(getResources().getString(R.string.erase));
        }
        if (i == R.id.lay_auto_btn) {
            this.offset_seekbar1.setProgress(this.dv.getOffset() + 150);
            this.lay_offset_seek.setVisibility(View.GONE);
            this.lay_threshold_seek.setVisibility(View.VISIBLE);
            this.lay_lasso_cut.setVisibility(View.GONE);
            this.headertext.setText(getResources().getString(R.string.auto));
        }
        if (i == R.id.lay_lasso_btn) {
            this.offset_seekbar2.setProgress(this.dv.getOffset() + 150);
            this.lay_offset_seek.setVisibility(View.GONE);
            this.lay_threshold_seek.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.VISIBLE);
            this.headertext.setText(getResources().getString(R.string.lasso));
        }
        if (i == R.id.lay_restore_btn) {
            this.offset_seekbar.setProgress(this.dv.getOffset() + 150);
            this.lay_offset_seek.setVisibility(View.VISIBLE);
            this.lay_threshold_seek.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
            this.headertext.setText(getResources().getString(R.string.restore));
        }
        if (i == R.id.lay_zoom_btn) {
            this.lay_offset_seek.setVisibility(View.GONE);
            this.lay_threshold_seek.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
            this.headertext.setText(getResources().getString(R.string.zoom));
        }
        if (i == R.id.lay_restore_btn) {
            this.dv1.setVisibility(View.VISIBLE);
        } else {
            this.dv1.setVisibility(View.GONE);
        }
        if (i != R.id.lay_zoom_btn) {
            this.dv.updateOnScale(this.main_rel.getScaleX());
        }
    }


    public void onDestroy() {
        Bitmap bitmap2 = this.b;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.b = null;
        }
        try {
            if (!isFinishing() && this.dv.pd != null && this.dv.pd.isShowing()) {
                this.dv.pd.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
