package com.astarconcepts.brandbanao.custom.poster.activity;


import static android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_TOP;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.TRUE;
import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.astarconcepts.brandbanao.utils.Constant.IS_PREMIUM;
import static com.astarconcepts.brandbanao.utils.Constant.IS_SUBSCRIBE;
import static com.astarconcepts.brandbanao.utils.Constant.movieImageList;
import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.AdsUtils.RewardAdsManager;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.Recorder.renderer.RenderEngine;
import com.astarconcepts.brandbanao.Recorder.renderer.VideoRecorder;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.TemplateListAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.fragment.MusicFragment;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;
import com.astarconcepts.brandbanao.custom.poster.adapter.FontAdapter;
import com.astarconcepts.brandbanao.custom.poster.adapter.FramesAdapter;
import com.astarconcepts.brandbanao.custom.poster.adapter.RecyclerItemClickListener;
import com.astarconcepts.brandbanao.custom.poster.adapter.RecyclerOverLayAdapter;
import com.astarconcepts.brandbanao.custom.poster.adapter.RecyclerTextBgAdapter;
import com.astarconcepts.brandbanao.custom.poster.adapter.RecyclerVideoAnimationAdapter;
import com.astarconcepts.brandbanao.custom.poster.adapter.RecyclerVideoFilterAdapter;
import com.astarconcepts.brandbanao.custom.poster.colorpicker.LineColorPicker;
import com.astarconcepts.brandbanao.custom.poster.create.DatabaseHandler;
import com.astarconcepts.brandbanao.custom.poster.create.RepeatListener;
import com.astarconcepts.brandbanao.custom.poster.create.TemplateInfo;
import com.astarconcepts.brandbanao.custom.poster.fragment.BackgroundFragment;
import com.astarconcepts.brandbanao.custom.poster.fragment.BackgroundFragment2;
import com.astarconcepts.brandbanao.custom.poster.fragment.ListFragment;
import com.astarconcepts.brandbanao.custom.poster.fragment.StickerFragment;
import com.astarconcepts.brandbanao.custom.poster.fragment.StickerFragmentMore;
import com.astarconcepts.brandbanao.custom.poster.imagecroper.CropActivity;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSnapListener;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSnapListenerData;
import com.astarconcepts.brandbanao.custom.poster.listener.GetColorListener;
import com.astarconcepts.brandbanao.custom.poster.listener.OnClickCallback;
import com.astarconcepts.brandbanao.custom.poster.listener.OnSetImageSticker;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.custom.poster.model.ElementInfoPoster;
import com.astarconcepts.brandbanao.custom.poster.model.Sticker_info;
import com.astarconcepts.brandbanao.custom.poster.model.Text_infoposter;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailCo;
import com.astarconcepts.brandbanao.custom.poster.movie.DemoPresenter;
import com.astarconcepts.brandbanao.custom.poster.movie.IDemoView;
import com.astarconcepts.brandbanao.custom.poster.views.AutoFitEditText;
import com.astarconcepts.brandbanao.custom.poster.views.StickerView;
import com.astarconcepts.brandbanao.custom.poster.views.ViewIdGenerator;
import com.astarconcepts.brandbanao.custom.poster.views.text.AutofitTextRel;
import com.astarconcepts.brandbanao.custom.poster.views.text.TextInfo;
import com.astarconcepts.brandbanao.databinding.ActivityPosterEditorBinding;
import com.astarconcepts.brandbanao.model.FrameItem;
import com.astarconcepts.brandbanao.model.FrameModel;
import com.astarconcepts.brandbanao.model.FrameResponse;
import com.astarconcepts.brandbanao.model.FramesModelCategory;
import com.astarconcepts.brandbanao.ui.activities.SubsPlanActivity;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.FilterAdjuster;
import com.astarconcepts.brandbanao.utils.FrameUtils;
import com.astarconcepts.brandbanao.utils.ImageCropperFragment;
import com.astarconcepts.brandbanao.utils.ImageUtils;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.StorageUtils;
import com.astarconcepts.brandbanao.zoomviwe.ZoomLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.common.Scopes;
import com.google.android.material.tabs.TabLayout;
import com.hw.photomovie.render.GLTextureView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import yuku.ambilwarna.AmbilWarnaDialog;


public class ThumbnailActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, GetColorListener, GetSnapListenerData, OnSetImageSticker, GetSnapListener, StickerView.TouchEventListener, AutofitTextRel.TouchEventListener, RecyclerOverLayAdapter.OnOverlaySelected, IDemoView {

    private static final int SELECT_PICTURE_FROM_CAMERA = 905;
    private static final int SELECT_PICTURE_FROM_GALLERY = 907;
    private static final int SELECT_PICTURE_FROM_GALLERY_BACKGROUND = 909;
    private static final int SELECT_PICTURE_FROM_GALLERY_FOR_STICKER_CHANGE = 1907;
    private static final String TAG = "ThumbnailActivity";
    private static final int TEXT_ACTIVITY = 908;
    private static final int TYPE_STICKER = 9072;
    public static ThumbnailActivity activity = null;
    public static ImageView background_img = null;
    public static Bitmap btmSticker = null;
    public static ImageView btn_layControls = null;

    public static Activity context = null;
    public static Bitmap imgBtmap = null;
    public static FrameLayout lay_container;
    public static int mRadius;
    public static SeekBar seek_tailys;
    public static RelativeLayout txtStkrRel;
    public static Bitmap withoutWatermark;
    public static String musicPath = "";
    private final int bColor = Color.parseColor("#4149b6");
    private final int curTileId = 0;
    private final List<WeakReference<Fragment>> mFragments = new ArrayList();
    public StickerView currentStickerView = null;
    public Animation animSlideUp;
    public Bitmap bit;
    public Bitmap bitmap;
    public ImageButton btn_bck1;
    public RelativeLayout centerRel;
    public boolean checkTouchContinue = false;
    public boolean editMode = false;
    public String filename;
    public View focusedView;
    public float hr = 1.0f;
    public LinearLayout layFontsSpacing;
    public RelativeLayout layoutShadow1;
    public RelativeLayout layoutShadow2;
    public Handler mHandler;
    public int mInterval = 50;
    public Runnable mStatusChecker;
    public RelativeLayout mainRel;
    public int outerColor = 0;
    public int outerSize = 0;
    public SeekBar seek;
    public SeekBar seekBar3;
    public SeekBar seekbarShadow;
    public float wr = 1.0f;
    String fileName = "";

    ActivityPosterEditorBinding binding;

    PlayerView playerView;
    ExoPlayer exoplayer;
    String postPath;


    RecyclerView rv_frame;
    int seekValue = 90;
    SeekBar seek_blur;
    int shadowFlag = 0;
    List<TemplateInfo> templateList = new ArrayList();
    boolean OneShow = true;
    FontAdapter adapter;
    RecyclerOverLayAdapter adaptorOverlay;
    RecyclerTextBgAdapter adaptorTxtBg;
    int alpha = 80;
    int backgroundOrientation = 2;
    ImageView backgroundBlur;
    ImageView cancel_text_frame;

    ImageView cancel_text_filters;
    ImageView cancel_text_animation;
    ImageView cancel_text_effects;
    int bgAlpha = 0;
    int bgColor = ViewCompat.MEASURED_STATE_MASK;
    String bgDrawable = "0";
    LinearLayout bgShow;
    ImageView btnColorBackgroundPic;
    ImageView btnEditControlBg;
    ImageView btnEditControlColor;
    ImageView btnEditControlOutlineColor;
    ImageView btnEditControlShadowColor;
    ImageView btnImgBackground;
    ImageView btnImgCameraSticker;
    ImageView btnRedo;
    RelativeLayout bckprass;
    RelativeLayout musicBack;
    RelativeLayout bckprassSticker;
    ImageView btnShadowBottom;
    ImageView btnShadowLeft;
    ImageView btnShadowRight;
    ImageView btnShadowTop;
    ImageView btnTakePicture;
    ImageView btnUndo;
    AppCompatButton btnErase;
    ImageButton btnUpDown;
    ImageButton btnUpDown1;
    LinearLayout layMusic;
    int catId;
    boolean checkMemory;
    LinearLayout colorShow;
    String colorType;
    LinearLayout controlsShow;
    LinearLayout controlsShowStkr;
    ProgressDialog dialogIs;
    boolean dialogShow = true;
    float distance;
    int distanceScroll;
    int dsfc;
    ArrayList<ElementInfoPoster> elementInfoPosters = new ArrayList<>();
    String fontName = "";
    LinearLayout fontsCurve;
    LinearLayout fontsShow;
    LinearLayout fontsSpacing;
    String frameName = "";
    String hex;
    LinearLayout imgOK;
    RelativeLayout layStkrMain;
    RelativeLayout layTextMain;
    LinearLayout layBackground;
    RelativeLayout layColor;
    LinearLayout layColorOacity;
    RelativeLayout layColorOpacity;
    RelativeLayout layControlStkr;
    LinearLayout layDupliStkr;
    ImageView layDupliText;
    ImageView lay_edit;
    boolean isAdWatchedFull = false;
    AppCompatButton sticker_gallery_change;
    RelativeLayout layFilter;
    RelativeLayout layHandletails;
    RelativeLayout layHue;
    ScrollView layScroll;
    LinearLayout laySticker;
    int leftRightShadow = 0;
    ListFragment listFragment;
    FrameLayout mViewAllFrame;
    int myDesignFlag;
    BitmapFactory.Options options = new BitmapFactory.Options();
    LinearLayout outlineShow;
    String overlay_Name = "";
    int overlay_blur;
    int overlay_opacty;
    String[] pallete = {"#ffffff", "#cccccc", "#999999", "#666666", "#333333", "#000000", "#ffee90", "#ffd700", "#daa520", "#b8860b", "#ccff66", "#adff2f", "#00fa9a", "#00ff7f", "#00ff00", "#32cd32", "#3cb371", "#99cccc", "#66cccc", "#339999", "#669999", "#006666", "#336666", "#ffcccc", "#ff9999", "#ff6666", "#ff3333", "#ff0033", "#cc0033"};
    float parentY;
    String position = "0";
    int postId;
    String profile;
    ProgressBar progressBarUndo;
    String ratio;
    String actualRatio;
    RelativeLayout rellative;
    float rotation = 0.0f;
    LinearLayout sadowShow;
    float screenHeight;
    float screenWidth;
    int shadowColor = ViewCompat.MEASURED_STATE_MASK;
    int shadowProg = 0;
    RelativeLayout shapeRel;
    boolean showtailsSeek = false;
    int sizeFull = 0;
    ArrayList<Sticker_info> stickerInfoArrayList = new ArrayList<>();
    ArrayList<Sticker_info> frameInfoArrayList = new ArrayList<>();
    int stkrColorSet = Color.parseColor("#ffffff");
    int tAlpha = 100;
    int tColor = -1;
    int tempID = 2001;
    String tempPath = "";
    ArrayList<TemplateInfo> templateListRU = new ArrayList<>();
    ArrayList<TemplateInfo> templateListUR = new ArrayList<>();
    int templateId;
    int textColorSet = Color.parseColor("#ffffff");
    ArrayList<Text_infoposter> textInfoArrayList = new ArrayList<>();
    ArrayList<Text_infoposter> textFrameInfoArrayList = new ArrayList<>();
    ArrayList<TextInfo> textInfosUR = new ArrayList<>();
    int topBottomShadow = 0;
    UniversalDialog universalDialog;
    ImageView transImg;
    TextView txtBG;
    TextView txtEffect;
    TextView txtMusic;
    TextView txtImage;
    HashMap<Integer, Object> txtShapeList;
    HashMap<Integer, Object> txtShapeListFrame;
    TextView txtSticker;
    TextView txtText;
    TextView txtFrame;
    SeekBar verticalSeekBar = null;
    float yAtLayoutCenter = -1.0f;
    List<FrameItem> frameItemList;
    ImageView iv_save;
    PreferenceManager preferenceManager;
    boolean isTemplatePremium = false;
    ImageView frameImage;
    ProgressDialog prgDialog;
    boolean isAnimatedFrame = false;
    LottieAnimationView lottieAnimationFrame;
    TemplateModel templateModel;
    TabLayout frameTabLayout;
    ViewPager frameViewPager;
    FramesAdapter framesAdapter;
    FrameModel selectedFrameModel;
    List<FramesModelCategory> framesModelCategories;
    ZoomLayout greetingZoomLay;
    LottieAnimationView disk_lottie;
    ImageView play_pause_btn;
    ExoPlayer musicPlayer;
    private boolean isChangeSticker;
    private Dialog dialog;
    private Dialog dialogPremium, dialogUnlockFrame;
    private ProgressBar progressBarPremium;
    private SeekBar alphaSeekbar;
    private Animation animSlideDown;
    private File file;
    private View focusedCopy = null;
    private LineColorPicker horizontalPicker;
    private LineColorPicker horizontalPickerColor;
    private SeekBar hueSeekbar;
    private boolean isBackground;

    private LinearLayout layVideoFilter;
    private LinearLayout layVideoAnimation;
    private RelativeLayout layEffects;
    private LinearLayout lay_frames;
    private RelativeLayout layRemove;
    private LinearLayout layTextedit;

    private LinearLayout imageFrame;
    private LinearLayout animatedFrame;

    private float letterSpacing = 0.0f;
    private float lineSpacing = 0.0f;
    private int min = 0;
    private LineColorPicker pickerBg;
    private LineColorPicker pickerOutline;
    private int processs;
    private SeekBar seekLetterSpacing;
    private SeekBar seekLineSpacing;
    private SeekBar seekOutlineSize;
    private SeekBar seekShadowBlur;
    private LinearLayout seekbarContainer;
    private LinearLayout seekbarHandle;
    private LineColorPicker shadowPickerColor;
    private Typeface ttf;
    private Typeface ttfHeader;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.e(ThumbnailActivity.TAG, "onReceive: ");
            ThumbnailActivity.this.layBackground.setVisibility(View.GONE);
            ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
            thumbnailActivity.backgroundOrientation = 2;
            thumbnailActivity.openCustomActivity(null, intent);
        }

    };
    private TextView txtBgControl;
    private TextView txtColorOpacity;
    private TextView txtColorsControl;
    private TextView txtControlText;
    private TextView txtEffectText;
    private TextView txtFilterText;
    private TextView txtFontsControl;
    private TextView txtShadowControl;
    private TextView txtTextControls;
    private TextView txtFontsSpacing;
    private TextView txtFontsStyle;
    private TextView txtFontsCurve;
    private TextView txtOutlineControl;
    private RelativeLayout userImage;
    private ImageView ivPremium;
    private InterstitialsAdsManager googleAds;
    private Dialog dialogWatermarkOption;
    private Integer currentFramePos = 1;
    private InterstitialsAdsManager interstitialsAdsManager;
    private int isTamplate;
    private String backgroundPosterPath;
    private String animatedFramePath;
    private GLTextureView glTextureView;
    private DemoPresenter demoPresenter;
    private String type;
    private boolean isMovie = false;
    private boolean isGreeting = false;
    private RelativeLayout movie_lay;
    private RecyclerVideoFilterAdapter adaptorVideoFilter;
    private RecyclerVideoAnimationAdapter adaptorVideoAnimation;
    private SeekBar seekTextCurve;
    private RelativeLayout select_backgnd;
    private RelativeLayout select_frame;
    private RelativeLayout select_effect;
    private RelativeLayout add_sticker;
    private RelativeLayout add_text;
    private RelativeLayout video_filter;
    private RelativeLayout video_animation;
    private boolean isAdWatched = false;
    private boolean isWatermarkEnabled = true;
    private boolean isExportingWithFrame = false;
    private String musicCat;
    private final Integer page = 1;
    private Dialog dialogExporting;
    private ProgressBar progressBarExporting;

    private float getnewHeight(int i, int i2, float f, float f2) {
        return (((float) i2) * f) / ((float) i);
    }

    private float getnewWidth(int i, int i2, float f, float f2) {
        return (((float) i) * f2) / ((float) i2);
    }

    public void onEdit(View view, Uri uri) {
    }

    public void onMidX(View view) {
    }

    public void onMidXY(View view) {
    }

    public void onMidY(View view) {
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onXY(View view) {
    }

    public float getXpos(float f) {
        return (((float) this.mainRel.getWidth()) * f) / 100.0f;
    }

    public float getYpos(float f) {
        return (((float) this.mainRel.getHeight()) * f) / 100.0f;
    }

    public int getNewWidht(float f, float f2) {
        return (int) ((((float) this.mainRel.getWidth()) * (f2 - f)) / 100.0f);
    }

    public int getNewHeight(float f, float f2) {
        return (int) ((((float) this.mainRel.getHeight()) * (f2 - f)) / 100.0f);
    }

    public int getNewHeightText(float f, float f2) {
        float height = (((float) this.mainRel.getHeight()) * (f2 - f)) / 100.0f;
        return (int) (((float) ((int) height)) + (height / 2.0f));
    }

    private void loadCategoryFrame(String frameType) {
        frameTabLayout = findViewById(R.id.frameTabLayout);
        frameViewPager = findViewById(R.id.framePager);

        universalDialog.showLoadingDialog(context, "Loading Frames");

        // Currently frames support for "1024:1024 (1:1) ratio."

        Constant.getHomeViewModel(this).getFrames(preferenceManager.getString(Constant.USER_ID),frameType, actualRatio).observe(this, frameResponse -> {

            universalDialog.dissmissLoadingDialog();

            if (frameResponse != null) {
                MyUtils.showResponse(frameResponse.framecategories);


                framesModelCategories = frameResponse.framecategories;

                framesAdapter = new FramesAdapter(getSupportFragmentManager(), framesModelCategories, (position, frameModel) -> {

                    selectedFrameModel = frameModel;

                    if (!preferenceManager.getBoolean(IS_SUBSCRIBE) && frameModel.isIs_premium()) {

                        dialogUnlockFrame.show();

                        return;
                    }

                    if (frameModel.getType().contains(Constant.FRAME_TYPE_ANIMATED)) {

                        universalDialog.showLoadingDialog(context, "Loading...");
                        frameAnimatedProcess(frameModel);

                    } else
                    if (frameModel.getType().contains(Constant.FRAME_TYPE_IMAGE)) {

                        isAnimatedFrame = false;

                        frameImageProcess(frameModel);

                    }else{

                        framePngProcess(frameModel);
                    }


                });

                frameViewPager.setAdapter(framesAdapter);
                frameTabLayout.setupWithViewPager(frameViewPager);

                if (frameResponse.framecategories.size() > 0 && frameResponse.framecategories.get(0).getFrameModels().size() > 0) {

                    if (frameType.equals(Constant.FRAME_TYPE_ANIMATED)) {

                        if (getFrameModel(frameResponse, 0).getFrame_category().contains("business")) {

                            frameAnimatedProcess(getFrameModel(frameResponse, 0));

                        } else if (getFrameModel(frameResponse, 1).getFrame_category().contains("personal")) {

                            frameAnimatedProcess(getFrameModel(frameResponse, 1));
                        }

                    } else  if (frameType.equals(Constant.FRAME_TYPE_IMAGE)) {

                        if (getFrameModel(frameResponse, 0).getFrame_category().contains("business")) {
                            frameImageProcess(getFrameModel(frameResponse, 0));
                        } else if (getFrameModel(frameResponse, 1).getFrame_category().contains("personal")) {
                            frameImageProcess(getFrameModel(frameResponse, 1));
                        }
                    }else{
                        framePngProcess(getFrameModel(frameResponse, 0));
                    }


                }

            }

        });

        // Rest of the code...
    }



    private void framePngProcess(FrameModel frameModel) {

        universalDialog.showLoadingDialog(context, "Setting Frame...");

        if (isAnimatedFrame) {

            lottieAnimationFrame.clearAnimation();
            lottieAnimationFrame.invalidate();
            lottieAnimationFrame.setVisibility(GONE);

            binding.frameImage.setVisibility(VISIBLE);
            Glide.with(context).load(frameModel.getThumbnail()).into(binding.frameImage);

            universalDialog.dissmissLoadingDialog();

        } else {
            removeExistingFrame(() -> {

                universalDialog.dissmissLoadingDialog();

                binding.frameImage.setVisibility(VISIBLE);
                Glide.with(context).load(frameModel.getThumbnail()).into(binding.frameImage);

            });
        }


    }

    private FrameModel getFrameModel(FrameResponse frameResponse, int index) {
        return frameResponse.framecategories.get(index).getFrameModels().get(0);
    }

    private void frameAnimatedProcess(FrameModel frameModel) {

        universalDialog.showLoadingDialog(context, "Setting Frame...");


        removeExistingFrame(() -> {

            lottieAnimationFrame.invalidate();
            lottieAnimationFrame.pauseAnimation();
            lottieAnimationFrame.cancelAnimation();
            lottieAnimationFrame.clearAnimation();

            TemplateModel templateModel1 = new TemplateModel();
            templateModel1.setId(frameModel.getId());
            templateModel1.setZip_link_preview(frameModel.getFrame_zip());
            templateModel1.setCode(frameModel.getTitle());
            templateModel1.setTemplate_type("frame");
            templateModel1.setCategory(frameModel.getFrame_category());
            LottieAnimationView animationView = new LottieAnimationView(context);

            templateModel = templateModel1;


            //load the Animated frame

            universalDialog.dissmissLoadingDialog();

            TemplateListAdapter.showLottieAnimation(context, lottieAnimationFrame, templateModel1, animationView);
            lottieAnimationFrame.setVisibility(VISIBLE);
            isAnimatedFrame = true;
        });


    }

    private void frameImageProcess(FrameModel model) {
        isAnimatedFrame = false;

        lottieAnimationFrame.invalidate();
        lottieAnimationFrame.setVisibility(GONE);

        universalDialog.showLoadingDialog(context, "Setting Frame...");

        removeExistingFrame(() -> new FrameUtils(context, model, (stickerInfos, textInfos) -> {

            if (!textInfos.isEmpty() && !stickerInfos.isEmpty()) {

                frameInfoArrayList = stickerInfos;
                textFrameInfoArrayList = textInfos;

                new LoadFrameAsync().execute();

            } else {

                Toast.makeText(activity, "Unable to proceed with this frame", Toast.LENGTH_SHORT).show();
                universalDialog.dissmissLoadingDialog();

            }

        }));

    }

    private void removeExistingFrame(FrameRemovalCallback callback) {
        frameInfoArrayList.clear();
        textFrameInfoArrayList.clear();

        new Thread(() -> {
            int childCount = txtStkrRel.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = txtStkrRel.getChildAt(i);

                runOnUiThread(() -> {
                    if ((childAt instanceof StickerView)) {
                        if (((StickerView) childAt).isFrameItem()) {
                            ((StickerView) childAt).deleteView();
                        }
                    }
                    if ((childAt instanceof AutofitTextRel)) {
                        if (((AutofitTextRel) childAt).isFrameItem()) {
                            ((AutofitTextRel) childAt).deleteView();
                        }
                    }
                });
            }

            // Notify the callback when the removal process is complete
            runOnUiThread(() -> {
                if (callback != null) {
                    callback.onFrameRemovalComplete();
                }
            });
        }).start();
    }

    public void closeMusicFragment(String outputPath) {

        musicPath = outputPath;

        layMusic.setVisibility(GONE);

        playOnDisk();

        playMusic(musicPath);


    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (bundle != null) {
            if (file == null && bundle.getString("uri_file_path") != null) {
                file = new File(bundle.getString("uri_file_path"));
            }
        }

        binding = ActivityPosterEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        activity = this;

        frameItemList = new ArrayList<>();

        preferenceManager = new PreferenceManager(context);
        interstitialsAdsManager = new InterstitialsAdsManager(context);

        isTemplatePremium = preferenceManager.getBoolean(IS_PREMIUM);

        universalDialog = new UniversalDialog(this, false);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Creating....");
        prgDialog.setCancelable(false);

        this.PreferenceManager = new PreferenceManager(getApplicationContext());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = (float) displayMetrics.widthPixels;
        this.screenHeight = (float) (displayMetrics.heightPixels - ImageUtils.dpToPx(this, 105.0f));

        AndroidNetworking.initialize(getApplicationContext());

        Explode explode = new Explode();
        explode.setDuration(400);
        getWindow().setEnterTransition(explode);
        getWindow().setExitTransition(explode);

        findView();
        setUnlockFrame();

        setupDialogWatermarkOption();

        intilization();

        googleAds = new InterstitialsAdsManager(this);

        findViewById(R.id.logo_ll).setOnClickListener(v -> dialogWatermarkOption.show());



        setDialogPremium();

        this.options.inScaled = false;
        this.ttfHeader = Constant.getHeaderTypeface(this);
        this.myDesignFlag = getIntent().getIntExtra("cat_id", 0);
        this.catId = getIntent().getIntExtra("cat_id", 0);
        this.postId = getIntent().getIntExtra("pos_id", 0);
        this.isTamplate = getIntent().getIntExtra("isTamplate", 0);
        this.ratio = getIntent().getStringExtra("sizeposition");
        this.type = getIntent().getStringExtra("type");

        this.postPath = getIntent().getStringExtra("backgroundImage");
        this.backgroundPosterPath = getIntent().getStringExtra("backgroundImage");


        if (this.isTamplate != 0) {
            ArrayList parcelableArrayListExtra = getIntent().getParcelableArrayListExtra("template");
            this.textInfoArrayList = getIntent().getParcelableArrayListExtra("text");
            this.stickerInfoArrayList = getIntent().getParcelableArrayListExtra("sticker");
            this.profile = getIntent().getStringExtra(Scopes.PROFILE);
            this.tempPath = ((ThumbnailCo) parcelableArrayListExtra.get(0)).getBack_image();
            ThumbnailCo thumbnailCo = (ThumbnailCo) parcelableArrayListExtra.get(0);
            this.postId = Integer.parseInt(thumbnailCo.getPost_id());
            this.templateId = Integer.parseInt(thumbnailCo.getPost_id());
            this.frameName = thumbnailCo.getBack_image();
            this.dialogIs = new ProgressDialog(this);
            this.dialogIs.setMessage(getResources().getString(R.string.plzwait));
            this.dialogIs.setCancelable(false);
            this.dialogIs.show();
            drawBackgroundImageFromDp(this.ratio, this.profile, "created");


        } else {

            if (type != null && type.equals(Constant.GREETING)) {
                findViewById(R.id.greeting_btn).setVisibility(View.VISIBLE);
                findViewById(R.id.greeting_btn).setOnClickListener(view -> {
                    isGreeting = true;
                    layVideoAnimation.setVisibility(View.GONE);
                    layVideoFilter.setVisibility(View.GONE);
                    lay_frames.setVisibility(View.GONE);
                    layEffects.setVisibility(View.GONE);
                    layStkrMain.setVisibility(View.GONE);
                    layBackground.setVisibility(View.GONE);
                    layTextMain.setVisibility(View.GONE);
                    laySticker.setVisibility(View.GONE);
                    showPicImageDialog();
                });
            } else if (type != null && type.equals("Movie")) {
                isMovie = true;
                background_img.setVisibility(View.INVISIBLE);

                findViewById(R.id.video_animation).setVisibility(View.VISIBLE);
                select_backgnd.setVisibility(GONE);
                findViewById(R.id.video_filter).setVisibility(View.VISIBLE);
                glTextureView = new GLTextureView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.addRule(CENTER_IN_PARENT, TRUE);
                params.addRule(ALIGN_TOP, background_img.getId());
                params.addRule(ALIGN_BOTTOM, background_img.getId());
                movie_lay.addView(glTextureView, params);
                demoPresenter = new DemoPresenter();
                demoPresenter.attachView(this);
                demoPresenter.onPhotoPick(Constant.movieImageList);


                initVideoAnimationRecycler();
                initVideoFilterRecycler();
            }

            this.postPath = getIntent().getStringExtra("backgroundImage");
            this.backgroundPosterPath = getIntent().getStringExtra("backgroundImage");
            this.hex = getIntent().getStringExtra("hex");


            drawBackgroundImage();

            if (postPath.endsWith(".mp4")) {
                initializePlayer();
                background_img.setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.playerview).setVisibility(View.GONE);
            }
        }

        Log.i("backgroundPosterPath", "" + backgroundPosterPath);

        int[] iArr = new int[this.pallete.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = Color.parseColor(this.pallete[i]);
        }
        this.horizontalPicker.setColors(iArr);
        this.horizontalPickerColor.setColors(iArr);
        this.shadowPickerColor.setColors(iArr);
        this.pickerOutline.setColors(iArr);
        this.pickerBg.setColors(iArr);
        this.horizontalPicker.setSelectedColor(this.textColorSet);
        this.horizontalPickerColor.setSelectedColor(this.stkrColorSet);
        this.shadowPickerColor.setSelectedColor(iArr[5]);
        this.pickerOutline.setSelectedColor(iArr[5]);
        this.pickerBg.setSelectedColor(iArr[5]);
        int color = this.horizontalPicker.getColor();
        int color2 = this.horizontalPickerColor.getColor();
        int color3 = this.shadowPickerColor.getColor();
        int color4 = this.pickerBg.getColor();
        int color5 = this.pickerOutline.getColor();
        updateColor(color);
        updateColor(color2);
        updateShadow(color3);
        updateOutline(color5);
        updateBgColor(color4);
        this.horizontalPickerColor.setOnColorChangedListener(ThumbnailActivity.this::updateColor);
        this.horizontalPicker.setOnColorChangedListener(ThumbnailActivity.this::updateColor);
        this.shadowPickerColor.setOnColorChangedListener(i -> ThumbnailActivity.this.updateShadow(i));
        this.pickerOutline.setOnColorChangedListener(i -> ThumbnailActivity.this.updateOutline(i));
        this.pickerBg.setOnColorChangedListener(i -> ThumbnailActivity.this.updateBgColor(i));
        this.mViewAllFrame = findViewById(R.id.viewall_layout);
        this.rellative = findViewById(R.id.rellative);
        this.layScroll = findViewById(R.id.lay_scroll);


        this.background_img.setOnTouchListener((v, event) -> {

            ThumbnailActivity.this.onTouchApply();

            return false;
        });


        findViewById(R.id.btnLeft).setOnTouchListener(new RepeatListener(200, 100, view -> ThumbnailActivity.this.updatePositionSticker("decX")));
        findViewById(R.id.btnUp).setOnTouchListener(new RepeatListener(200, 100, view -> ThumbnailActivity.this.updatePositionSticker("incrX")));
        findViewById(R.id.btnRight).setOnTouchListener(new RepeatListener(200, 100, view -> ThumbnailActivity.this.updatePositionSticker("decY")));
        findViewById(R.id.btnDown).setOnTouchListener(new RepeatListener(200, 100, view -> ThumbnailActivity.this.updatePositionSticker("incrY")));
        findViewById(R.id.btnLeftS).setOnTouchListener(new RepeatListener(200, 100, view -> ThumbnailActivity.this.updatePositionSticker("decX")));
        findViewById(R.id.btnRightS).setOnTouchListener(new RepeatListener(200, 100, view -> ThumbnailActivity.this.updatePositionSticker("incrX")));
        findViewById(R.id.btnUpS).setOnTouchListener(new RepeatListener(200, 100, view -> ThumbnailActivity.this.updatePositionSticker("decY")));
        findViewById(R.id.btnDownS).setOnTouchListener(new RepeatListener(200, 100, view -> ThumbnailActivity.this.updatePositionSticker("incrY")));

        this.mHandler = new Handler();


        this.mStatusChecker = new Runnable() {
            public void run() {
                if (ThumbnailActivity.this.layScroll != null) {
                    ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
                    thumbnailActivity.removeScrollViewPosition(thumbnailActivity.focusedView);
                }
                ThumbnailActivity.this.mHandler.postDelayed(this, ThumbnailActivity.this.mInterval);
            }
        };
        oneTimeLayerAdjust();

        Constant.getBusinessItem(this);


        imageFrame.setOnClickListener(v -> {

            buttonClick(R.color.active_color, R.color.edit_bg_color, binding.tvFrameImage, binding.tvFrameAnimated, binding.ivFrameImage, R.drawable.ic_frame_img, binding.ivFrameAnimated, R.drawable.ic_frame_video_black);

            loadCategoryFrame(Constant.FRAME_TYPE_IMAGE);


        });

        animatedFrame.setOnClickListener(v -> {

            buttonClick(R.color.edit_bg_color, R.color.active_color, binding.tvFrameAnimated, binding.tvFrameImage, binding.ivFrameAnimated, R.drawable.ic_frame_video, binding.ivFrameImage, R.drawable.ic_frame_img_black);

            loadCategoryFrame(Constant.FRAME_TYPE_ANIMATED);

        });

    }

    private void buttonClick(int iconBg, int active_color, TextView textWhite, TextView textBlack, ImageView ivframe, int ic_frame_video, ImageView ivframe2, int ic_frame_img_black) {
        imageFrame.setBackground(getResources().getDrawable(iconBg));
        animatedFrame.setBackground(getResources().getDrawable(active_color));
        textWhite.setTextColor(getResources().getColor(R.color.al_gray));
        textBlack.setTextColor(getResources().getColor(R.color.black_1000));
        ivframe.setImageDrawable(getResources().getDrawable(ic_frame_video));
        ivframe2.setImageDrawable(getResources().getDrawable(ic_frame_img_black));
    }

    private void initVideoFilterRecycler() {
        this.adaptorVideoFilter = new RecyclerVideoFilterAdapter(this, item -> demoPresenter.onFilterSelect(item));
        RecyclerView recyclerView = findViewById(R.id.videofilter_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorVideoFilter);
    }

    private void initVideoAnimationRecycler() {
        this.adaptorVideoAnimation = new RecyclerVideoAnimationAdapter(this, item -> demoPresenter.onTransferSelect(item));
        RecyclerView recyclerView = findViewById(R.id.videoanim_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorVideoAnimation);
    }


    private void initializePlayer() {

        playerView = findViewById(R.id.playerview);
        playerView.setControllerHideOnTouch(true);
        playerView.setShowBuffering(true);
        TrackSelector trackSelectorDef = new DefaultTrackSelector();
        exoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef);

        int appNameStringRes = R.string.app_name;
        String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
        Uri uriOfContentUrl = Uri.parse(postPath);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source

        exoplayer.prepare(mediaSource);
        exoplayer.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline
        playerView.setPlayer(exoplayer); // attach surface to the view
        playerView.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);


        findViewById(R.id.iv_play).setOnClickListener(v -> {
            findViewById(R.id.iv_play).setVisibility(GONE);
            exoplayer.seekTo(0);
            exoplayer.setPlayWhenReady(true);
        });

        exoplayer.addListener(new Player.EventListener() {

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Player.EventListener.super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);

                if (playbackState == ExoPlayer.STATE_ENDED) {
                    findViewById(R.id.iv_play).setVisibility(VISIBLE);
                }

            }
        });

        playerView.setOnTouchListener(new View.OnTouchListener() {

            private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);

                    if (!exoplayer.getPlayWhenReady()) {
                        exoplayer.setPlayWhenReady(true);
                        exoplayer.seekTo(0);
                        findViewById(R.id.iv_play).setVisibility(GONE);

                    } else {

                        new Handler(getMainLooper()).postDelayed(() -> {

                            exoplayer.setPlayWhenReady(false);
                            findViewById(R.id.iv_play).setVisibility(VISIBLE);

                        }, 100);
                    }


                    return true;
                }


                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if (!exoplayer.getPlayWhenReady()) {
                        exoplayer.setPlayWhenReady(true);
                    }

                    return super.onDoubleTap(e);

                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public void setDialogPremium() {

        dialogPremium = new Dialog(context, R.style.MyAlertDialog);
        dialogPremium.setContentView(R.layout.dialog_layout_watermark_option);

        Window window = dialogPremium.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ImageView close = dialogPremium.findViewById(R.id.iv_close);
        LinearLayout premium = dialogPremium.findViewById(R.id.cv_no);

        TextView tv_title = dialogPremium.findViewById(R.id.tv_title);
        TextView dialogMessageTextView = dialogPremium.findViewById(R.id.dialogMessageTextView);

        tv_title.setText("Premium Template");
        dialogMessageTextView.setText(R.string.please_subscribe_videp);


        progressBarPremium = dialogPremium.findViewById(R.id.pb_loading);

        close.setOnClickListener(view -> {

            dialogPremium.dismiss();


            progressBarPremium.setVisibility(GONE);

        });

        premium.setOnClickListener(view -> {

            startActivity(new Intent(context, SubsPlanActivity.class));
            dialogPremium.dismiss();

        });

        LinearLayout yes = dialogPremium.findViewById(R.id.cv_yes);

        yes.setOnClickListener(view -> {

            progressBarPremium.setVisibility(View.VISIBLE);

            Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show();

            new RewardAdsManager(context, new RewardAdsManager.OnAdLoaded() {
                @Override
                public void onAdClosed() {
                    Toast.makeText(context, "Ad Not loaded", Toast.LENGTH_SHORT).show();
                    progressBarPremium.setVisibility(GONE);
                }

                @Override
                public void onAdWatched() {

                    Toast.makeText(context, "Congratulations, You unlocked it", Toast.LENGTH_SHORT).show();
                    progressBarPremium.setVisibility(GONE);
                    dialogPremium.dismiss();
                    isAdWatched = true;
                    exportBtnDone();
                }
            });


        });

    }

    public void setUnlockFrame() {

        dialogUnlockFrame = new Dialog(context, R.style.MyAlertDialog);
        dialogUnlockFrame.setContentView(R.layout.dialog_layout_watermark_option);

        Window window = dialogUnlockFrame.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ImageView close = dialogUnlockFrame.findViewById(R.id.iv_close);
        LinearLayout premium = dialogUnlockFrame.findViewById(R.id.cv_no);

        TextView tv_title = dialogUnlockFrame.findViewById(R.id.tv_title);
        TextView dialogMessageTextView = dialogUnlockFrame.findViewById(R.id.dialogMessageTextView);

        tv_title.setText("Unlock Frame");
        dialogMessageTextView.setText(R.string.please_subscribe_frame);

        ProgressBar progressBarFrame = dialogUnlockFrame.findViewById(R.id.pb_loading);

        close.setOnClickListener(view -> {

            dialogUnlockFrame.dismiss();

            progressBarPremium.setVisibility(GONE);

        });

        premium.setOnClickListener(view -> {

            startActivity(new Intent(context, SubsPlanActivity.class));
            dialogUnlockFrame.dismiss();

        });

        LinearLayout watermark = dialogUnlockFrame.findViewById(R.id.cv_yes);

        watermark.setOnClickListener(view -> {

            progressBarFrame.setVisibility(View.VISIBLE);

            Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show();

            new RewardAdsManager(context, new RewardAdsManager.OnAdLoaded() {
                @Override
                public void onAdClosed() {
                    Toast.makeText(context, "Ad Not loaded", Toast.LENGTH_SHORT).show();
                    progressBarFrame.setVisibility(GONE);
                }

                @Override
                public void onAdWatched() {

                    Toast.makeText(context, "Congratulations, You unlocked it", Toast.LENGTH_SHORT).show();
                    progressBarFrame.setVisibility(GONE);
                    dialogUnlockFrame.dismiss();
                    isAdWatched = true;

                    if (selectedFrameModel.getType().equals(Constant.FRAME_TYPE_ANIMATED)) {

                        frameAnimatedProcess(selectedFrameModel);

                    } else {
                        frameImageProcess(selectedFrameModel);
                    }

                }
            });


        });

    }

    public void removeScrollViewPosition(View view) {
        float f;
        int[] iArr = new int[2];
        this.layScroll.getLocationOnScreen(iArr);
        float f2 = (float) iArr[1];
        float width = (float) view.getWidth();
        float height = (float) view.getHeight();
        float x = view.getX();
        float y = (view.getY() + f2) - ((float) this.distanceScroll);
        if (view instanceof StickerView) {
            f = view.getRotation();
        } else {
            f = view.getRotation();
        }
        Matrix matrix = new Matrix();
        RectF rectF = new RectF(x, y, x + width, y + height);
        matrix.postRotate(f, x + (width / 2.0f), (height / 2.0f) + y);
        matrix.mapRect(rectF);
        float min2 = Math.min(rectF.top, rectF.bottom);
        if (f2 > min2) {
            float f3 = (float) ((int) (f2 - min2));
            try {
                float scrollY = (float) this.layScroll.getScrollY();
                if (scrollY > 0.0f) {
                    float f4 = scrollY - ((float) (((int) f3) / 4));
                    if (f4 >= 0.0f) {
                        this.layScroll.smoothScrollTo(0, (int) f4);
                        this.layScroll.getLayoutParams().height = (int) (((float) this.layScroll.getHeight()) + (y / 4.0f));
                        this.layScroll.postInvalidate();
                        this.layScroll.requestLayout();
                        return;
                    }
                    this.distanceScroll = 0;
                    this.layScroll.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
                    this.layScroll.postInvalidate();
                    this.layScroll.requestLayout();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private void findView() {
        this.progressBarUndo = findViewById(R.id.progress_undo);
        this.btnUndo = findViewById(R.id.btn_undo);
        this.btnRedo = findViewById(R.id.btn_redo);
        this.bckprass = findViewById(R.id.bckprass);
        this.movie_lay = findViewById(R.id.movie_lay);
        lottieAnimationFrame = findViewById(R.id.lottieAnimationFrames);
        greetingZoomLay = findViewById(R.id.greeting_zoom_lay);
        this.bckprassSticker = findViewById(R.id.bckprass_sticker);
        this.musicBack = findViewById(R.id.music_back);
        this.layVideoAnimation = findViewById(R.id.lay_video_animations);
        this.layVideoFilter = findViewById(R.id.lay_video_filters);
        this.btn_bck1 = findViewById(R.id.btn_bck1);
        this.btn_bck1.setOnClickListener(this);
        this.txtTextControls = findViewById(R.id.txt_text_controls);
        this.txtFontsControl = findViewById(R.id.txt_fonts_control);
        this.txtFontsStyle = findViewById(R.id.txt_fonts_Style);
        this.layFontsSpacing = findViewById(R.id.lay_fonts_Spacing);
        this.txtFontsSpacing = findViewById(R.id.txt_fonts_Spacing);
        this.txtFontsCurve = findViewById(R.id.txt_fonts_curve);
        this.txtColorsControl = findViewById(R.id.txt_colors_control);
        this.txtShadowControl = findViewById(R.id.txt_shadow_control);
        this.txtOutlineControl = findViewById(R.id.txt_outline_control);
        this.txtBgControl = findViewById(R.id.txt_bg_control);
        this.btnEditControlColor = findViewById(R.id.btnEditControlColor);
        this.btnEditControlShadowColor = findViewById(R.id.btnEditControlShadowColor);
        this.btnEditControlOutlineColor = findViewById(R.id.btnEditControlOutlineColor);
        this.btnEditControlBg = findViewById(R.id.btnEditControlBg);
        this.btnShadowLeft = findViewById(R.id.btnShadowLeft);
        this.btnShadowRight = findViewById(R.id.btnShadowRight);
        this.btnShadowTop = findViewById(R.id.btnShadowTop);
        this.btnShadowBottom = findViewById(R.id.btnShadowBottom);
        this.btnErase = findViewById(R.id.btn_erase);


        imageFrame = findViewById(R.id.image_frame);
        animatedFrame = findViewById(R.id.animated_frame);

        this.layoutShadow1 = findViewById(R.id.layoutShadow1);
        this.layoutShadow2 = findViewById(R.id.layoutShadow2);
        this.txtFrame = findViewById(R.id.bt_frame);
        this.txtText = findViewById(R.id.bt_text);
        this.txtSticker = findViewById(R.id.bt_sticker);
        this.txtImage = findViewById(R.id.bt_image);
        this.txtEffect = findViewById(R.id.bt_effect);
        this.txtMusic = findViewById(R.id.bt_music);
        this.txtBG = findViewById(R.id.bt_bg);

        this.lay_frames = findViewById(R.id.lay_frames);
        this.layEffects = findViewById(R.id.lay_effects);
        this.laySticker = findViewById(R.id.lay_sticker);
        this.layBackground = findViewById(R.id.lay_background);
        this.layHandletails = findViewById(R.id.lay_handletails);
        this.seekbarContainer = findViewById(R.id.seekbar_container);
        this.seekbarHandle = findViewById(R.id.seekbar_handle);
        this.shapeRel = findViewById(R.id.shape_rel);
        seek_tailys = findViewById(R.id.seek_tailys);
        this.alphaSeekbar = findViewById(R.id.alpha_seekBar);
        this.seekBar3 = findViewById(R.id.seekBar3);
        this.seekbarShadow = findViewById(R.id.seekBar_shadow);
        seekTextCurve = findViewById(R.id.seekTextCurve);
        this.hueSeekbar = findViewById(R.id.hue_seekBar);
        this.seekShadowBlur = findViewById(R.id.seekShadowBlur);
        this.seekOutlineSize = findViewById(R.id.seekOutlineSize);
        this.transImg = findViewById(R.id.trans_img);
        this.frameImage = findViewById(R.id.frameImage);

        cancel_text_frame = findViewById(R.id.cancel_text_frame);
        cancel_text_filters = findViewById(R.id.cancel_text_filters);
        cancel_text_animation = findViewById(R.id.cancel_text_animation);
        cancel_text_effects = findViewById(R.id.cancel_text_effects);

        lay_container = findViewById(R.id.lay_container);
        this.centerRel = findViewById(R.id.center_rel);
        this.btnImgCameraSticker = findViewById(R.id.btnImgCameraSticker);
        this.btnImgBackground = findViewById(R.id.btnImgBackground);
        this.btnTakePicture = findViewById(R.id.btnTakePicture);
        this.btnColorBackgroundPic = findViewById(R.id.btnColorBackgroundPic);
        this.sticker_gallery_change = findViewById(R.id.sticker_gallery_change);
        this.layRemove = findViewById(R.id.lay_remove);
        this.layTextMain = findViewById(R.id.lay_TextMain);
        this.layStkrMain = findViewById(R.id.lay_StkrMain);
        this.btnUpDown = findViewById(R.id.btn_up_down);
        this.btnUpDown1 = findViewById(R.id.btn_up_down1);
        this.layMusic = findViewById(R.id.lay_music);
        this.mainRel = findViewById(R.id.main_rel);
        background_img = findViewById(R.id.background_img);
        this.backgroundBlur = findViewById(R.id.background_blur);
        txtStkrRel = findViewById(R.id.txt_stkr_rel);
        this.userImage = findViewById(R.id.select_artwork);
        select_backgnd = findViewById(R.id.select_backgnd);
        select_frame = findViewById(R.id.select_frame);
        select_effect = findViewById(R.id.select_effect);
        add_sticker = findViewById(R.id.add_sticker);
        add_text = findViewById(R.id.add_text);

        this.seek_blur = findViewById(R.id.seek_blur);
        this.seek = findViewById(R.id.seek);
        this.layFilter = findViewById(R.id.lay_filter);
        this.layDupliText = findViewById(R.id.lay_dupliText);
        this.layDupliStkr = findViewById(R.id.lay_dupliStkr);
        this.lay_edit = findViewById(R.id.lay_edit);
        this.iv_save = findViewById(R.id.iv_save);
        this.ivPremium = findViewById(R.id.iv_premium);
        this.imgOK = findViewById(R.id.ll_save);
        btn_layControls = findViewById(R.id.btn_layControls);
        this.layTextedit = findViewById(R.id.lay_textEdit);
        this.verticalSeekBar = findViewById(R.id.seekBar2);
        this.horizontalPicker = findViewById(R.id.picker);
        this.horizontalPickerColor = findViewById(R.id.picker1);
        this.shadowPickerColor = findViewById(R.id.pickerShadow);
        this.pickerOutline = findViewById(R.id.pickerOutline);
        this.pickerBg = findViewById(R.id.pickerBg);
        this.layColor = findViewById(R.id.lay_color);
        this.layHue = findViewById(R.id.lay_hue);

        this.txtControlText = findViewById(R.id.txtControlText);
        this.txtColorOpacity = findViewById(R.id.txtColorOpacity);
        this.seekLetterSpacing = findViewById(R.id.seekLetterSpacing);
        this.seekLineSpacing = findViewById(R.id.seekLineSpacing);
        this.fontsShow = findViewById(R.id.fontsShow);
        this.fontsSpacing = findViewById(R.id.fontsSpacing);
        this.fontsCurve = findViewById(R.id.fontsCurve);
        this.colorShow = findViewById(R.id.colorShow);
        this.sadowShow = findViewById(R.id.sadowShow);
        this.outlineShow = findViewById(R.id.outlineShow);
        this.bgShow = findViewById(R.id.bgShow);
        this.controlsShow = findViewById(R.id.controlsShow);
        this.layColorOpacity = findViewById(R.id.lay_colorOpacity);
        this.layControlStkr = findViewById(R.id.lay_controlStkr);
        this.layColorOacity = findViewById(R.id.lay_colorOacity);
        this.controlsShowStkr = findViewById(R.id.controlsShowStkr);
        video_filter = findViewById(R.id.video_filter);
        video_animation = findViewById(R.id.video_animation);

        ImageView btnShadowTabChange = findViewById(R.id.btnShadowTabChange);
        btnShadowTabChange.setOnClickListener(view -> {
            if (ThumbnailActivity.this.shadowFlag == 0) {
                ThumbnailActivity.this.shadowFlag = 1;
                ThumbnailActivity.this.layoutShadow2.setVisibility(View.VISIBLE);
                ThumbnailActivity.this.layoutShadow1.setVisibility(GONE);
            } else if (ThumbnailActivity.this.shadowFlag == 1) {
                ThumbnailActivity.this.shadowFlag = 0;
                ThumbnailActivity.this.layoutShadow1.setVisibility(View.VISIBLE);
                ThumbnailActivity.this.layoutShadow2.setVisibility(GONE);
            }
        });

    }

    private void drawBackgroundImageFromDp(final String str, String str3, final String str4) {
        this.laySticker.setVisibility(GONE);
        File file = new File(this.frameName);
        Log.e("file", "==" + file);
        if (file.exists()) {
            RequestBuilder<Bitmap> asBitmap = Glide.with(getApplicationContext()).asBitmap();
            RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true);

            float f = this.screenWidth;
            float f2 = this.screenHeight;
            if (f <= f2) {
                f = f2;
            }
            asBitmap.apply(requestOptions.override((int) f)).load(this.frameName).into(new SimpleTarget<Bitmap>() {
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    ThumbnailActivity.this.bitmapRatio(str, "Background", bitmap, str4);

                }
            });
        } else if (!str.equals("")) {
            String replace = file.getName().replace(".png", "");
            new SavebackgrundAsync().execute(replace, str, str3, str4);
        } else if (this.OneShow) {
            errorDialogTempInfo();
            this.OneShow = false;
        }
    }

    public void drawBackgroundImage() {

        universalDialog.showLoadingDialog(context, "Loading Frames...");

        RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true);
        float f = this.screenWidth;
        float f2 = this.screenHeight;
        if (f <= f2) {
            f = f2;
        }

        this.laySticker.setVisibility(View.GONE);
        RequestBuilder<Bitmap> asBitmap = Glide.with(getApplicationContext()).asBitmap();

        asBitmap.apply(requestOptions.override((int) f)).load(backgroundPosterPath).into(new SimpleTarget<Bitmap>() {
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {

                bitmapRatio(ratio, bitmap);

            }
        });
    }

    public void bitmapRatio(String str, String str2, Bitmap bitmap2, String str3) {

        if (str != null) {

            String[] split = str.split(":");

            int gcd = gcd(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

            Integer.parseInt(split[0]);
            Integer.parseInt(split[1]);
            String str4 = "" + (Integer.parseInt(split[0]) / gcd) + ":" + (Integer.parseInt(split[1]) / gcd);
            if (!str4.equals("")) {
                if (str4.equals("1:1")) {
                    bitmap2 = cropInRatio(bitmap2, 1, 1);
                } else if (str4.equals("16:9")) {
                    bitmap2 = cropInRatio(bitmap2, 16, 9);
                } else if (str4.equals("9:16")) {
                    bitmap2 = cropInRatio(bitmap2, 9, 16);
                } else if (str4.equals("4:3")) {
                    bitmap2 = cropInRatio(bitmap2, 4, 3);
                } else if (str4.equals("4:5")) {
                    bitmap2 = cropInRatio(bitmap2, 4, 5);
                } else if (str4.equals("3:4")) {
                    bitmap2 = cropInRatio(bitmap2, 3, 4);
                } else {

                    bitmap2 = cropInRatio(bitmap2, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                }
            }

            Bitmap resizeBitmap = Constant.resizeBitmap(bitmap2, (int) this.screenWidth, (int) this.screenHeight);

            if (!str3.equals("created")) {
                if (str2.equals("Texture")) {
                    setImageBitmapAndResizeLayout(Constant.getTiledBitmap(this, this.curTileId, resizeBitmap), "nonCreated");
                } else {
                    setImageBitmapAndResizeLayout(resizeBitmap, "nonCreated");
                }
            } else if (str2.equals("Texture")) {
                setImageBitmapAndResizeLayout(Constant.getTiledBitmap(this, this.curTileId, resizeBitmap), "created");
            } else {
                setImageBitmapAndResizeLayout(resizeBitmap, "created");
            }
            actualRatio = str4;
            loadCategoryFrame(Constant.FRAME_TYPE_IMAGE);

        } else {
            Toast.makeText(activity, "Layout Ratio Error", Toast.LENGTH_SHORT).show();
        }

    }

    public void bitmapRatio(String str, Bitmap bitmap2) {
        Log.d("editorActivity___", "1 -> " + str);
        if (str != null) {
            String[] split = str.split(":");
            int gcd = gcd(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

            String str4 = "" + (Integer.parseInt(split[0]) / gcd) + ":" + (Integer.parseInt(split[1]) / gcd);

            if (!str4.equals("")) {
                if (str4.equals("1:1")) {

                    bitmap2 = cropInRatio(bitmap2, 1, 1);

                } else if (str4.equals("16:9")) {
                    bitmap2 = cropInRatio(bitmap2, 16, 9);
                } else if (str4.equals("9:16")) {
                    bitmap2 = cropInRatio(bitmap2, 9, 16);
                } else if (str4.equals("4:3")) {
                    bitmap2 = cropInRatio(bitmap2, 4, 3);
                } else if (str4.equals("3:4")) {
                    bitmap2 = cropInRatio(bitmap2, 3, 4);
                } else if (str4.equals("4:5")) {
                    bitmap2 = cropInRatio(bitmap2, 4, 5);
                } else {

                    bitmap2 = cropInRatio(bitmap2, Integer.parseInt(split[0]), Integer.parseInt(split[1]));


                }
            }

            Bitmap resizeBitmap = Constant.resizeBitmap(bitmap2, (int) this.screenWidth, (int) this.screenHeight);

            setImageBitmapAndResizeLayout(resizeBitmap, str);

            actualRatio = str4;

            loadCategoryFrame(Constant.FRAME_TYPE_IMAGE);


        } else {
            Toast.makeText(context, "Layout Ratio Error", Toast.LENGTH_SHORT).show();
        }

    }

    public Bitmap cropInRatio(Bitmap bitmap2, int ratio1, int ratio2) {
        float width = (float) bitmap2.getWidth();
        float height = (float) bitmap2.getHeight();
        float f = getnewHeight(ratio1, ratio2, width, height);
        float f2 = getnewWidth(ratio1, ratio2, width, height);
        return (f2 == width && f == height) ? bitmap2 : (f > height || f >= height) ? (f2 > width || f2 >= width) ? null : Bitmap.createBitmap(bitmap2, (int) ((width - f2) / 2.0f), 0, (int) f2, (int) height) : Bitmap.createBitmap(bitmap2, 0, (int) ((height - f) / 2.0f), (int) width, (int) f);
    }

    private void setImageBitmapAndResizeLayout(Bitmap bitmap2, String str) {

        Log.d(TAG, "setImageBitmapAndResizeLayout: " + bitmap2.getWidth() + "::" + bitmap2.getHeight());

        this.mainRel.getLayoutParams().width = bitmap2.getWidth();
        this.mainRel.getLayoutParams().height = bitmap2.getHeight();
        this.mainRel.postInvalidate();
        this.mainRel.requestLayout();
        background_img.setImageBitmap(bitmap2);
        imgBtmap = bitmap2;
        this.bit = bitmap2;
        this.mainRel.post(() -> {


           /* ThumbnailActivity.this.layScroll.post(() -> {
                int[] iArr = new int[2];
                ThumbnailActivity.this.layScroll.getLocationOnScreen(iArr);
                ThumbnailActivity.this.parentY = (float) iArr[1];
                ThumbnailActivity.this.yAtLayoutCenter = ThumbnailActivity.this.parentY;
            });*/

            try {
                ThumbnailActivity.this.bit = ImageUtils.resizeBitmap(ThumbnailActivity.this.bit, ThumbnailActivity.this.centerRel.getWidth(), ThumbnailActivity.this.centerRel.getHeight());
                float height = (float) ThumbnailActivity.this.bit.getHeight();
                ThumbnailActivity.this.wr = ((float) ThumbnailActivity.this.bit.getWidth()) / ((float) ThumbnailActivity.this.bit.getWidth());
                ThumbnailActivity.this.hr = height / ((float) ThumbnailActivity.this.bit.getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (this.min != 0) {
            this.backgroundBlur.setVisibility(View.VISIBLE);
        } else {
            this.backgroundBlur.setVisibility(GONE);
        }
        if (str.equals("created")) {
            new BlurOperationTwoAsync(this, this.bit, this.backgroundBlur).execute("");
            return;
        }
    }

    private void intilization() {

        this.btnUndo.setOnClickListener(this);
        this.btnRedo.setOnClickListener(this);
        this.btnErase.setOnClickListener(this);
        this.btn_bck1.setOnClickListener(this);
        cancel_text_frame.setOnClickListener(this);
        cancel_text_animation.setOnClickListener(this);
        cancel_text_effects.setOnClickListener(this);
        cancel_text_filters.setOnClickListener(this);


        this.ttf = Constant.getTextTypeface(this);
        findViewById(R.id.select_music).setOnClickListener(this);
        this.alphaSeekbar.setOnSeekBarChangeListener(this);
        this.seekBar3.setOnSeekBarChangeListener(this);
        this.seekbarShadow.setOnSeekBarChangeListener(this);
        this.hueSeekbar.setOnSeekBarChangeListener(this);
        seek_tailys.setOnSeekBarChangeListener(this);
        this.layDupliText.setOnClickListener(this);
        this.layDupliStkr.setOnClickListener(this);
        this.lay_edit.setOnClickListener(this);
        this.sticker_gallery_change.setOnClickListener(this);
        this.hueSeekbar.setProgress(1);
        this.seek.setMax(255);
        this.seek.setProgress(80);
        this.seek_blur.setMax(255);
        this.seekbarShadow.setProgress(5);
        this.seekBar3.setProgress(255);
        this.seek_blur.setProgress(this.min);
        this.transImg.setImageAlpha(this.alpha);
        seek_tailys.setMax(290);
        seek_tailys.setProgress(90);
        this.seek.setOnSeekBarChangeListener(this);
        this.seek_blur.setOnSeekBarChangeListener(this);
        this.imgOK.setOnClickListener(this);
        btn_layControls.setOnClickListener(this);
        this.userImage.setOnClickListener(this);
        select_backgnd.setOnClickListener(this);
        select_frame.setOnClickListener(this);

        select_effect.setOnClickListener(this);
        add_sticker.setOnClickListener(this);
        add_text.setOnClickListener(this);
        this.lay_frames.setOnClickListener(this);
        this.layRemove.setOnClickListener(this);
        this.centerRel.setOnClickListener(this);
        this.animSlideUp = Constant.getAnimUp(this);
        this.animSlideDown = Constant.getAnimDown(this);
        this.verticalSeekBar.setOnSeekBarChangeListener(this);
        this.btnImgCameraSticker.setOnClickListener(this);
        this.btnImgBackground.setOnClickListener(this);
        this.btnColorBackgroundPic.setOnClickListener(this);
        this.btnTakePicture.setOnClickListener(this);
        this.seekLetterSpacing.setOnSeekBarChangeListener(this);
        this.seekLineSpacing.setOnSeekBarChangeListener(this);
        seekTextCurve.setOnSeekBarChangeListener(this);
        this.seekShadowBlur.setOnSeekBarChangeListener(this);
        this.seekOutlineSize.setOnSeekBarChangeListener(this);
        this.layColorOpacity.setOnClickListener(this);
        this.layControlStkr.setOnClickListener(this);
        video_animation.setOnClickListener(this);
        video_filter.setOnClickListener(this);
        this.adapter = new FontAdapter(this, getResources().getStringArray(R.array.fonts_array));
        this.adapter.setSelected(0);


        ((GridView) findViewById(R.id.font_gridview)).setAdapter(this.adapter);

        this.adapter.setItemClickCallback((OnClickCallback<ArrayList<String>, Integer, String, Activity>) (arrayList, num, str, activity) -> {
            ThumbnailActivity.this.setTextFonts(str);
            ThumbnailActivity.this.adapter.setSelected(num.intValue());
        });
        this.adaptorTxtBg = new RecyclerTextBgAdapter(this, Constant.imageId);
        RecyclerView recyclerView = findViewById(R.id.txtBg_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorTxtBg);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, i) -> {
            ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
            thumbnailActivity.setTextBgTexture("btxt" + i);
        }));

        showLayoutFragment();
        initOverlayRecycler();
        StickerCategoryVertical();
        BackgroundCategoryVertical();
        fackClick();
        getMusicCategoryData();

    }

    public void onGalleryButtonClick_forChangeSticker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), SELECT_PICTURE_FROM_GALLERY_FOR_STICKER_CHANGE);
    }

    private void StickerCategoryVertical() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        StickerFragment stickerFragment = (StickerFragment) supportFragmentManager.findFragmentByTag("sticker_main");
        if (stickerFragment != null) {
            beginTransaction.remove(stickerFragment);
        }
        StickerFragment newInstance = StickerFragment.newInstance();
        this.mFragments.add(new WeakReference(newInstance));
        beginTransaction.add(R.id.frameContainerSticker, newInstance, "sticker_main");
        try {
            beginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BackgroundCategoryVertical() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        BackgroundFragment backgroundFragment = (BackgroundFragment) supportFragmentManager.findFragmentByTag("inback_category_frgm");
        if (backgroundFragment != null) {
            beginTransaction.remove(backgroundFragment);
        }
        BackgroundFragment newInstance = BackgroundFragment.newInstance();
        this.mFragments.add(new WeakReference(newInstance));
        beginTransaction.add(R.id.frameContainerBackground, newInstance, "inback_category_frgm");
        try {
            beginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOverlayRecycler() {
        this.adaptorOverlay = new RecyclerOverLayAdapter(this, Constant.overlayArr, this);
        RecyclerView recyclerView = findViewById(R.id.overlay_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorOverlay);

    }

    private void showLayoutFragment() {

        this.listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_container, this.listFragment, "fragment").commit();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_sticker:
                removeScroll();
                removeImageViewControll();
                hideSlideBar();
                if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                    this.seekbarContainer.startAnimation(this.animSlideDown);
                    this.seekbarContainer.setVisibility(GONE);
                    this.laySticker.setVisibility(GONE);
                }
                if (this.laySticker.getVisibility() != View.VISIBLE) {
                    this.laySticker.setVisibility(View.VISIBLE);
                    this.laySticker.startAnimation(this.animSlideUp);
                    this.imgOK.setVisibility(View.VISIBLE);
                    this.btnErase.setVisibility(View.VISIBLE);
                    this.btnUndo.setVisibility(View.VISIBLE);
                    this.btnRedo.setVisibility(View.VISIBLE);
                    this.bckprassSticker.setOnClickListener(view1 -> {
                        laySticker.setVisibility(GONE);
                        laySticker.startAnimation(animSlideDown);
                    });
                } else {
                    this.laySticker.setVisibility(GONE);
                    this.laySticker.startAnimation(this.animSlideDown);
                    this.imgOK.setVisibility(View.VISIBLE);
                    this.btnErase.setVisibility(View.VISIBLE);
                    this.btnUndo.setVisibility(View.VISIBLE);
                    this.btnRedo.setVisibility(View.VISIBLE);
                }
                this.layEffects.setVisibility(GONE);
                this.layStkrMain.setVisibility(GONE);
                this.layBackground.setVisibility(GONE);
                this.layTextMain.setVisibility(GONE);
                this.lay_frames.setVisibility(GONE);

                this.layVideoAnimation.setVisibility(GONE);
                this.layVideoFilter.setVisibility(GONE);


                return;

            case R.id.cancel_text_frame:
                lay_frames.setVisibility(GONE);
                return;
            case R.id.cancel_text_effects:
                layEffects.setVisibility(GONE);
                return;
            case R.id.cancel_text_filters:
                layVideoFilter.setVisibility(GONE);
                return;
            case R.id.cancel_text_animation:
                layVideoAnimation.setVisibility(GONE);
                return;

            case R.id.add_text:
                removeScroll();
                removeImageViewControll();
                hideSlideBar();
                if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                    this.seekbarContainer.startAnimation(this.animSlideDown);
                    this.seekbarContainer.setVisibility(GONE);
                }
                this.layEffects.setVisibility(GONE);
                this.layStkrMain.setVisibility(GONE);
                this.layBackground.setVisibility(GONE);
                this.layTextMain.setVisibility(GONE);
                this.laySticker.setVisibility(GONE);
                this.lay_frames.setVisibility(GONE);
                this.layVideoAnimation.setVisibility(GONE);
                this.layVideoFilter.setVisibility(GONE);


                addTextDialog(null);
                return;
            case R.id.btnAlignMentFont:
                setLeftAlignMent();
                return;
            case R.id.btnBoldFont:
                setBoldFonts();
                return;
            case R.id.btnCapitalFont:
                setCapitalFont();
                return;
            case R.id.btnCenterFont:
                setCenterAlignMent();
                return;
            case R.id.btnColorBackgroundPic:
                colorPickerDialog(false);
                return;

            case R.id.sticker_gallery_change:
                onGalleryButtonClick_forChangeSticker();
                return;
            case R.id.btnEditControlBg:
                mainControlBgPickerDialog(false);
                return;
            case R.id.btnEditControlColor:
                mainControlcolorPickerDialog(false);
                return;
            case R.id.btnEditControlOutlineColor:
                mainControlOutlinePickerDialog(false);
                return;
            case R.id.btnEditControlShadowColor:
                mainControlShadowPickerDialog(false);
                return;
            case R.id.btnImgBackground:
                onGalleryBackground();
                return;
            case R.id.btnTakePicture:
                ThumbnailActivity.this.onGalleryBackground();
                return;
            case R.id.btnImgCameraSticker:
                ThumbnailActivity.this.onGalleryButtonClick();
                return;
            case R.id.btnItalicFont:
                setItalicFont();
                return;

            case R.id.btnRightFont:
                setRightAlignMent();
                return;
            case R.id.btnShadowBottom:
                setBottomShadow();
                return;
            case R.id.btnShadowLeft:
                setLeftShadow();
                return;
            case R.id.btnShadowRight:
                setRightShadow();
                return;
            case R.id.btnShadowTop:
                setTopShadow();
                return;
            case R.id.btnUnderlineFont:
                setUnderLineFont();
                return;
            case R.id.btn_bck1:
                this.layScroll.smoothScrollTo(0, this.distanceScroll);
                return;
            case R.id.btn_bckprass:
                removeScroll();
                onBackPressed();
                return;
            case R.id.ll_save:
                removeScroll();
                hideSlideBar();
                this.layEffects.setVisibility(GONE);
                this.layStkrMain.setVisibility(GONE);
                this.layBackground.setVisibility(GONE);
                this.layTextMain.setVisibility(GONE);
                this.laySticker.setVisibility(GONE);
                this.lay_frames.setVisibility(GONE);

                removeImageViewControll();
                if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                    this.seekbarContainer.startAnimation(this.animSlideDown);
                    this.seekbarContainer.setVisibility(GONE);
                }
                if (this.layTextMain.getVisibility() == View.VISIBLE) {
                    this.layTextMain.startAnimation(this.animSlideDown);
                    this.layTextMain.setVisibility(GONE);
                }
                if (this.layStkrMain.getVisibility() == View.VISIBLE) {
                    this.layStkrMain.startAnimation(this.animSlideDown);
                    this.layStkrMain.setVisibility(GONE);
                }

                exportBtnDone();


                return;
            case R.id.btn_erase:
                int childCount = txtStkrRel.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = txtStkrRel.getChildAt(i);
                    if (childAt instanceof StickerView) {
                        StickerView stickerView = (StickerView) childAt;
                        if (stickerView.getBorderVisbilty()) {
                            if (!stickerView.getComponentInfo().getSTKR_PATH().equals("")) {
                                Constant.uri = stickerView.getComponentInfo().getSTKR_PATH();

                                try {
                                    new ImageCropperFragment(childAt.getId(), stickerView.getComponentInfo().getSTKR_PATH(), (id, out) -> {
                                        int childCount1 = txtStkrRel.getChildCount();
                                        int i5 = id;
                                        for (int i6 = 0; i6 < childCount1; i6++) {
                                            View childAt1 = txtStkrRel.getChildAt(i6);
                                            if ((childAt1 instanceof StickerView) && childAt1.getId() == i5) {
                                                ((StickerView) childAt1).setStrPath(out);
                                                saveBitmapUndu();
                                            }
                                        }
                                    }).show(getSupportFragmentManager(), "");

                                    return;
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            } else if (!stickerView.getComponentInfo().getRES_ID().equals("")) {
                                Constant.rewid = stickerView.getComponentInfo().getRES_ID();

                                try {
                                    new ImageCropperFragment(childAt.getId(), stickerView.getComponentInfo().getRES_ID(), (id, out) -> {
                                        int childCount12 = txtStkrRel.getChildCount();
                                        int i5 = id;
                                        for (int i6 = 0; i6 < childCount12; i6++) {
                                            View childAt12 = txtStkrRel.getChildAt(i6);
                                            if ((childAt12 instanceof StickerView) && childAt12.getId() == i5) {
                                                ((StickerView) childAt12).setStrPath(out);
                                                saveBitmapUndu();
                                            }
                                        }
                                    }).show(getSupportFragmentManager(), "");

                                    return;
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            } else if (stickerView.getMainImageBitmap() != null) {
                                Constant.bitmapSticker = stickerView.getMainImageBitmap();

                                try {
                                    new ImageCropperFragment(childAt.getId(), stickerView.getMainImageBitmap(), (id, out) -> {
                                        int childCount13 = txtStkrRel.getChildCount();
                                        int i5 = id;
                                        for (int i6 = 0; i6 < childCount13; i6++) {
                                            View childAt13 = txtStkrRel.getChildAt(i6);
                                            if ((childAt13 instanceof StickerView) && childAt13.getId() == i5) {
                                                ((StickerView) childAt13).setStrPath(out);
                                                saveBitmapUndu();
                                            }
                                        }
                                    }).show(getSupportFragmentManager(), "");

                                    return;
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }

                        }
                    }
                }
                Toast.makeText(this, "Select sticker to perform erase operation..", Toast.LENGTH_SHORT).show();
                return;
            case R.id.btn_layControls:
                oneTimeScrollLayer();
                removeScroll();
                removeImageViewControll();
                if (this.layTextMain.getVisibility() == View.VISIBLE) {
                    this.layTextMain.startAnimation(this.animSlideDown);
                    this.layTextMain.setVisibility(GONE);
                }
                if (this.layStkrMain.getVisibility() == View.VISIBLE) {
                    this.layStkrMain.startAnimation(this.animSlideDown);
                    this.layStkrMain.setVisibility(GONE);
                }
                if (lay_container.getVisibility() == GONE) {
                    btn_layControls.setVisibility(VISIBLE);
                    this.listFragment.getLayoutChild();
                    lay_container.setVisibility(View.VISIBLE);
                    lay_container.animate().translationX((float) lay_container.getLeft()).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
                    return;
                }
                lay_container.setVisibility(View.VISIBLE);
                lay_container.animate().translationX((float) (-lay_container.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
                new Handler().postDelayed(() -> {
                    ThumbnailActivity.lay_container.setVisibility(GONE);
                    ThumbnailActivity.btn_layControls.setVisibility(View.VISIBLE);
                }, 200);

                return;
            case R.id.btn_redo:
                redo();
                return;
            case R.id.btn_undo:
                undo();
                return;
            case R.id.btn_up_down:
                this.focusedCopy = this.focusedView;
                removeScroll();
                this.layStkrMain.requestLayout();
                this.layStkrMain.postInvalidate();
                if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                    hideResContainer();
                } else {
                    showResContainer();
                }
                return;
            case R.id.btn_up_down1:
                this.focusedCopy = this.focusedView;
                removeScroll();
                this.layTextMain.requestLayout();
                this.layTextMain.postInvalidate();
                if (this.layTextedit.getVisibility() == View.VISIBLE) {
                    hideTextResContainer();
                    return;
                } else {
                    showTextResContainer();
                    return;
                }
            case R.id.center_rel:
                return;
            case R.id.lay_remove:
                this.layEffects.setVisibility(GONE);
                this.layStkrMain.setVisibility(GONE);
                this.laySticker.setVisibility(GONE);
                this.layBackground.setVisibility(GONE);
                this.lay_frames.setVisibility(GONE);

                onTouchApply();
                return;
            case R.id.lay_backgnd_control:
                this.fontsShow.setVisibility(GONE);
                this.fontsSpacing.setVisibility(GONE);
                this.fontsCurve.setVisibility(GONE);
                this.colorShow.setVisibility(GONE);
                this.sadowShow.setVisibility(GONE);
                this.outlineShow.setVisibility(GONE);
                this.bgShow.setVisibility(View.VISIBLE);
                this.controlsShow.setVisibility(GONE);
                selectControl8();
                return;
            case R.id.lay_colorOpacity:
                this.layColorOacity.setVisibility(View.VISIBLE);
                this.controlsShowStkr.setVisibility(GONE);
                this.txtControlText.setTextColor(getResources().getColor(R.color.titlecolorbtn));

                return;
            case R.id.lay_colors_control:
                this.fontsShow.setVisibility(GONE);
                this.fontsSpacing.setVisibility(GONE);
                this.fontsCurve.setVisibility(GONE);
                this.colorShow.setVisibility(View.VISIBLE);
                this.sadowShow.setVisibility(GONE);
                this.outlineShow.setVisibility(GONE);
                this.bgShow.setVisibility(GONE);
                this.controlsShow.setVisibility(GONE);
                selectControl6();
                return;
            case R.id.lay_controlStkr:
                this.layColorOacity.setVisibility(GONE);
                this.controlsShowStkr.setVisibility(View.VISIBLE);

                this.txtColorOpacity.setTextColor(getResources().getColor(R.color.titlecolorbtn));
                return;
            case R.id.lay_controls_control:
                this.fontsShow.setVisibility(GONE);
                this.fontsSpacing.setVisibility(GONE);
                this.fontsCurve.setVisibility(GONE);
                this.colorShow.setVisibility(GONE);
                this.sadowShow.setVisibility(GONE);
                this.outlineShow.setVisibility(GONE);
                this.bgShow.setVisibility(GONE);
                this.controlsShow.setVisibility(View.VISIBLE);
                selectControl1();
                return;
            case R.id.lay_dupliStkr:
                int childCount2 = txtStkrRel.getChildCount();
                for (int i2 = 0; i2 < childCount2; i2++) {
                    View childAt2 = txtStkrRel.getChildAt(i2);
                    if (childAt2 instanceof StickerView) {
                        StickerView stickerView2 = (StickerView) childAt2;
                        if (stickerView2.getBorderVisbilty()) {
                            StickerView stickerView3 = new StickerView(this);
                            stickerView3.setComponentInfo(stickerView2.getComponentInfo());
                            stickerView3.setId(ViewIdGenerator.generateViewId());
                            stickerView3.setViewWH((float) this.mainRel.getWidth(), (float) this.mainRel.getHeight());
                            txtStkrRel.addView(stickerView3);
                            removeImageViewControll();
                            stickerView3.setOnTouchCallbackListener(this);
                            stickerView3.setBorderVisibility(true);
                        }
                    }
                }
                return;
            case R.id.lay_dupliText:
                int childCount3 = txtStkrRel.getChildCount();
                for (int i3 = 0; i3 < childCount3; i3++) {
                    View childAt3 = txtStkrRel.getChildAt(i3);
                    if (childAt3 instanceof AutofitTextRel) {
                        AutofitTextRel autofitTextRel = (AutofitTextRel) childAt3;
                        if (autofitTextRel.getBorderVisibility()) {
                            AutofitTextRel autofitTextRel2 = new AutofitTextRel(this);
                            txtStkrRel.addView(autofitTextRel2);
                            removeImageViewControll();
                            autofitTextRel2.setTextInfo(autofitTextRel.getTextInfo(), false);
                            autofitTextRel2.setId(ViewIdGenerator.generateViewId());
                            autofitTextRel2.setOnTouchCallbackListener(this);
                            autofitTextRel2.setBorderVisibility(true);
                        }
                    }
                }
                return;
            case R.id.lay_edit:

                doubleTabPrass();
                return;
            case R.id.lay_fonts_Curve:
                this.fontsSpacing.setVisibility(GONE);
                this.fontsCurve.setVisibility(View.VISIBLE);
                this.fontsShow.setVisibility(GONE);
                this.colorShow.setVisibility(GONE);
                this.sadowShow.setVisibility(GONE);
                this.outlineShow.setVisibility(GONE);
                this.bgShow.setVisibility(GONE);
                this.controlsShow.setVisibility(GONE);
                selectControl5();
                return;
            case R.id.lay_fonts_Spacing:
                this.fontsSpacing.setVisibility(View.VISIBLE);
                this.fontsCurve.setVisibility(GONE);
                this.fontsShow.setVisibility(GONE);
                this.colorShow.setVisibility(GONE);
                this.sadowShow.setVisibility(GONE);
                this.outlineShow.setVisibility(GONE);
                this.bgShow.setVisibility(GONE);
                this.controlsShow.setVisibility(GONE);
                selectControl4();
                return;
            case R.id.lay_fonts_control:
                this.fontsShow.setVisibility(View.VISIBLE);
                this.fontsSpacing.setVisibility(GONE);
                this.fontsCurve.setVisibility(GONE);
                this.colorShow.setVisibility(GONE);
                this.sadowShow.setVisibility(GONE);
                this.outlineShow.setVisibility(GONE);
                this.bgShow.setVisibility(GONE);
                this.controlsShow.setVisibility(GONE);
                selectControl2();
                return;
            case R.id.lay_fonts_style:
                this.fontsSpacing.setVisibility(GONE);
                this.fontsCurve.setVisibility(GONE);
                this.fontsShow.setVisibility(GONE);
                this.colorShow.setVisibility(GONE);
                this.sadowShow.setVisibility(GONE);
                this.outlineShow.setVisibility(GONE);
                this.bgShow.setVisibility(GONE);
                this.controlsShow.setVisibility(GONE);
                selectControl3();
                return;
            case R.id.lay_outline_control:
                this.fontsShow.setVisibility(GONE);
                this.fontsSpacing.setVisibility(GONE);
                this.fontsCurve.setVisibility(GONE);
                this.colorShow.setVisibility(GONE);
                this.sadowShow.setVisibility(GONE);
                this.outlineShow.setVisibility(View.VISIBLE);
                this.bgShow.setVisibility(GONE);
                this.controlsShow.setVisibility(GONE);
                selectControl9();
                return;
            case R.id.lay_shadow_control:
                this.fontsShow.setVisibility(GONE);
                this.fontsSpacing.setVisibility(GONE);
                this.fontsCurve.setVisibility(GONE);
                this.colorShow.setVisibility(GONE);
                this.sadowShow.setVisibility(View.VISIBLE);
                this.outlineShow.setVisibility(GONE);
                this.bgShow.setVisibility(GONE);
                this.controlsShow.setVisibility(GONE);
                selectControl7();
                return;
            case R.id.select_artwork:
                removeScroll();
                removeImageViewControll();
                hideSlideBar();
                this.layEffects.setVisibility(GONE);
                this.layStkrMain.setVisibility(GONE);
                this.layBackground.setVisibility(GONE);
                this.layTextMain.setVisibility(GONE);
                this.laySticker.setVisibility(GONE);
                this.lay_frames.setVisibility(GONE);

                this.layVideoAnimation.setVisibility(GONE);
                this.layVideoFilter.setVisibility(GONE);
                showPicImageDialog();


                return;
            case R.id.select_music:
                removeImageViewControll();
                hideSlideBar();
                stopMusic();
                if (layMusic.getVisibility() != View.VISIBLE) {
                    layMusic.setVisibility(View.VISIBLE);
                    musicBack.setOnClickListener(view12 -> {
                        layMusic.startAnimation(animSlideDown);
                        layMusic.setVisibility(GONE);
                    });
                } else {
                    layMusic.startAnimation(animSlideDown);
                    layMusic.setVisibility(View.GONE);
                }

                this.layEffects.setVisibility(View.GONE);
                this.layStkrMain.setVisibility(View.GONE);
                this.layBackground.setVisibility(View.GONE);
                this.layTextMain.setVisibility(View.GONE);
                this.laySticker.setVisibility(GONE);
                this.lay_frames.setVisibility(GONE);

                this.layVideoAnimation.setVisibility(GONE);
                this.layVideoFilter.setVisibility(GONE);

                return;

            case R.id.select_backgnd:
                hideSlideBar();
                if (this.layBackground.getVisibility() != View.VISIBLE) {
                    this.bckprass.setOnClickListener(v -> {

                        layBackground.setVisibility(GONE);
                        layBackground.startAnimation(this.animSlideDown);
                    });
                    this.layBackground.setVisibility(View.VISIBLE);
                    this.layBackground.startAnimation(this.animSlideUp);
                } else {
                    this.imgOK.setVisibility(View.VISIBLE);
                    this.btnErase.setVisibility(View.VISIBLE);
                    this.btnUndo.setVisibility(View.VISIBLE);
                    this.btnRedo.setVisibility(View.VISIBLE);
                    this.layBackground.setVisibility(GONE);
                }
                this.layEffects.setVisibility(GONE);
                this.layStkrMain.setVisibility(GONE);
                this.layTextMain.setVisibility(GONE);
                this.laySticker.setVisibility(GONE);
                this.lay_frames.setVisibility(GONE);

                this.layVideoAnimation.setVisibility(GONE);
                this.layVideoFilter.setVisibility(GONE);


                return;

            case R.id.select_frame:


                removeScroll();
                removeImageViewControll();
                hideSlideBar();
                if (this.lay_frames.getVisibility() != View.VISIBLE) {
                    this.lay_frames.setVisibility(View.VISIBLE);
                    this.lay_frames.startAnimation(this.animSlideUp);
                } else {
                    this.lay_frames.setVisibility(GONE);
                    this.lay_frames.startAnimation(this.animSlideDown);
                }
                this.layEffects.setVisibility(GONE);
                this.layStkrMain.setVisibility(GONE);
                this.layBackground.setVisibility(GONE);
                this.layTextMain.setVisibility(GONE);
                this.laySticker.setVisibility(GONE);

                this.layVideoAnimation.setVisibility(GONE);
                this.layVideoFilter.setVisibility(GONE);


                return;

            case R.id.select_effect:
                removeScroll();
                removeImageViewControll();
                hideSlideBar();
                if (this.layEffects.getVisibility() != View.VISIBLE) {
                    this.layEffects.setVisibility(View.VISIBLE);
                    this.layEffects.startAnimation(this.animSlideUp);
                } else {
                    this.layEffects.setVisibility(GONE);
                    this.layEffects.startAnimation(this.animSlideDown);
                }
                this.layStkrMain.setVisibility(GONE);
                this.layBackground.setVisibility(GONE);
                this.layTextMain.setVisibility(GONE);
                this.laySticker.setVisibility(GONE);
                this.lay_frames.setVisibility(GONE);

                this.layVideoAnimation.setVisibility(GONE);
                this.layVideoFilter.setVisibility(GONE);


                return;

            case R.id.video_filter:
                removeImageViewControll();
                hideSlideBar();
                if (this.layVideoFilter.getVisibility() != View.VISIBLE) {
                    this.layVideoFilter.setVisibility(View.VISIBLE);
                    this.layVideoFilter.startAnimation(this.animSlideUp);
                } else {
                    this.layVideoFilter.setVisibility(View.GONE);
                    this.layVideoFilter.startAnimation(this.animSlideDown);
                }
                this.layVideoAnimation.setVisibility(View.GONE);
                this.layEffects.setVisibility(View.GONE);
                this.lay_frames.setVisibility(View.GONE);
                this.layStkrMain.setVisibility(View.GONE);
                this.layBackground.setVisibility(View.GONE);
                this.layTextMain.setVisibility(View.GONE);
                this.laySticker.setVisibility(View.GONE);

                this.imgOK.setVisibility(View.VISIBLE);
                this.btnUndo.setVisibility(View.VISIBLE);
                this.btnRedo.setVisibility(View.VISIBLE);
                return;
            case R.id.video_animation:
                removeImageViewControll();
                hideSlideBar();
                if (this.layVideoAnimation.getVisibility() != View.VISIBLE) {
                    this.layVideoAnimation.setVisibility(View.VISIBLE);
                    this.layVideoAnimation.startAnimation(this.animSlideUp);
                } else {
                    this.layVideoAnimation.setVisibility(View.GONE);
                    this.layVideoAnimation.startAnimation(this.animSlideDown);
                }
                this.layVideoFilter.setVisibility(View.GONE);
                this.lay_frames.setVisibility(View.GONE);
                this.layEffects.setVisibility(View.GONE);
                this.layStkrMain.setVisibility(View.GONE);
                this.layBackground.setVisibility(View.GONE);
                this.layTextMain.setVisibility(View.GONE);
                this.laySticker.setVisibility(View.GONE);

                this.imgOK.setVisibility(View.VISIBLE);
                this.btnUndo.setVisibility(View.VISIBLE);
                this.btnRedo.setVisibility(View.VISIBLE);
                return;

            default:
                return;
        }
    }

    private void removeWatermarkAndProcessOption(ProgressBar progressBar, boolean watermark) {

        progressBar.setVisibility(GONE);
        isWatermarkEnabled = watermark;

        if (watermark) {

            Toast.makeText(context, "Ad not loaded, Try Again", Toast.LENGTH_SHORT).show();

            findViewById(R.id.logo_ll).setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(context, "Congratulations, You Removed watermark.", Toast.LENGTH_SHORT).show();

            dialogWatermarkOption.dismiss();
            findViewById(R.id.logo_ll).setVisibility(GONE);
        }


    }

    public void setupDialogWatermarkOption() {

        dialogWatermarkOption = new Dialog(context, R.style.MyAlertDialog);
        dialogWatermarkOption.setContentView(R.layout.dialog_layout_watermark_option);

        ImageView close = dialogWatermarkOption.findViewById(R.id.iv_close);
        LinearLayout premium = dialogWatermarkOption.findViewById(R.id.cv_no);

        Window window = dialogWatermarkOption.getWindow();


        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ProgressBar progressBar = dialogWatermarkOption.findViewById(R.id.pb_loading);

        close.setOnClickListener(view -> dialogWatermarkOption.dismiss());


        premium.setOnClickListener(view -> {

            startActivity(new Intent(context, SubsPlanActivity.class));
        });

        LinearLayout withouWatermark = dialogWatermarkOption.findViewById(R.id.cv_yes);

        withouWatermark.setOnClickListener(view -> {

            progressBar.setVisibility(View.VISIBLE);

            new RewardAdsManager(context, new RewardAdsManager.OnAdLoaded() {
                @Override
                public void onAdClosed() {

                    removeWatermarkAndProcessOption(progressBar, true);

                }

                @Override
                public void onAdWatched() {
                    isAdWatchedFull = true;
                    removeWatermarkAndProcessOption(progressBar, false);
                }
            });


        });

    }


    private void exportBtnDone() {


        stopMusic();


        if (isAdWatchedFull) {

            findViewById(R.id.logo_ll).setVisibility(GONE);

        } else {

            binding.ivWatermark.setImageDrawable(getResources().getDrawable(R.drawable.watermark));
            findViewById(R.id.logo_ll).setVisibility(View.VISIBLE);
        }


        boolean businessUpdated = preferenceManager.getBoolean(Constant.BUSINESS_UPDATED);

        File tempVideoFile;

        String imageVideoOutPath = MyUtils.getStoreVideoExternalStorage(context) + "/" + getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".mp4";

        if (isAnimatedFrame && backgroundPosterPath.contains("mp4")) {

            String tempVideoPath = MyUtils.getFolderPath(context, "VideoOutput/tmp") + "/" + templateModel.getCode() + ".mp4";
            tempVideoFile = new File(tempVideoPath);

            if (!tempVideoFile.exists() || businessUpdated) {

                new VideoRecorder(context, lottieAnimationFrame, lottieAnimationFrame, tempVideoFile.getAbsolutePath()).setInterfaceRenderEngine(new RenderInterface(progressBarPremium));

                isExportingWithFrame = false;

            } else {

                animatedFramePath = tempVideoFile.getAbsolutePath();
                isExportingWithFrame = true;

            }

        } else {

            if (isAnimatedFrame) {
                new VideoRecorder(context, mainRel, lottieAnimationFrame, imageVideoOutPath).setInterfaceRenderEngine(new RenderInterface(progressBarPremium));
                isExportingWithFrame = false;
            }
        }

        if (playerView != null) {
            playerView.hideController();
        }


        Handler handler = new Handler();
        handler.postDelayed(() -> {
            bitmap = viewToBitmap(mainRel);
            saveImage(bitmap, true);
        }, 200);


    }

    private void saveImage(Bitmap bitmap, boolean z) {

        prgDialog.show();

        File file = new File(MyUtils.getFolderPath(context, "cache"));

        String extension = z ? ".png" : ".jpg";
        String fileName = "Photo_" + System.currentTimeMillis() + extension;
        String filePath = file.getPath() + File.separator + fileName;

        if (isSaved(bitmap, filePath)) {
            startSavingAction(filePath);
        } else {
            prgDialog.dismiss();
            MyUtils.showToast(this, getString(R.string.error));
        }

    }

    private boolean isSaved(Bitmap bitmap, String filePath) {
        boolean success;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            bitmap.recycle();
            fileOutputStream.flush();

            MediaScannerConnection.scanFile(this, new String[]{filePath}, null, (path, uri) -> {
                StringBuilder sb = new StringBuilder();
                sb.append("-> uri=");
                sb.append(uri);
                sb.append("-> FILE=");
                sb.append(path);
                Uri muri = Uri.parse(path);
            });
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    private void startSavingAction(String framePath) {

        if (musicPath != null && !musicPath.isEmpty()) {
            if (musicPath.startsWith("http")) {

                File directory = new File(MyUtils.getFolderPath(context, "music"));

                String filename = MyUtils.extractFilename(musicPath);


                if (new File(directory.getAbsolutePath(), filename).exists()) {

                    musicPath = new File(directory, filename).getAbsolutePath();

                    if (isMovie) {
                        demoPresenter.setMusic(musicPath);

                    }
                    movieAction(framePath);


                } else {


                    Log.d("export", "music download : " + musicPath);

                    PRDownloader.download(musicPath, directory.toString(), filename).build().start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {


                            musicPath = new File(directory, filename).getAbsolutePath();

                            Log.d("export", "music path Saved : " + musicPath);


                            if (isMovie) {
                                demoPresenter.setMusic(musicPath);
                            }
                            movieAction(framePath);

                        }

                        @Override
                        public void onError(Error error) {


                            MyUtils.showToast(context, "Error Downlading..." + error.getServerErrorMessage());

                            prgDialog.dismiss();

                        }
                    });


                }


            } else {
                movieAction(framePath);
            }
        } else {

            movieAction(framePath);
        }
    }

    private void movieAction(String framePath) {
        if (isMovie) {

            demoPresenter.saveVideo(s -> {
                backgroundPosterPath = s;
                filename = s;
                applyFrameOnVideo(framePath);
            });

        } else {
            if (backgroundPosterPath.endsWith(".mp4")) {

                if (backgroundPosterPath.startsWith("https")) {

                    Log.d("export", "Video url: " + backgroundPosterPath);

                    File directory = new File(MyUtils.getFolderPath(context, "video"));

                    String filename = MyUtils.extractFilename(backgroundPosterPath);

                    if (new File(directory.getAbsolutePath(), filename).exists()) {

                        backgroundPosterPath = new File(directory.getAbsolutePath(), filename).getAbsolutePath();

                        applyFrameOnVideo(framePath);


                    } else {

                        PRDownloader.download(backgroundPosterPath, directory.toString(), filename).build().start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                                backgroundPosterPath = new File(directory.getAbsolutePath(), filename).getAbsolutePath();

                                applyFrameOnVideo(framePath);

                            }

                            @Override
                            public void onError(Error error) {

                                MyUtils.showToast(context, "Error Downlading Video : " + error.getConnectionException().getMessage());

                                prgDialog.dismiss();

                            }
                        });

                    }

                } else {

                    Log.d("export", "Video Mp4 : " + backgroundPosterPath);


                    if (musicPath != null && !musicPath.isEmpty()) {

                        applyMusicToVideo(backgroundPosterPath, framePath, false);

                    } else {

                        applyFrameOnVideo(framePath);

                    }
                }
            } else {


                filename = framePath;

                if (isAnimatedFrame) {

                    Handler handler = new Handler();
                    Runnable checkExportingStatus = new Runnable() {
                        @Override
                        public void run() {
                            if (isAnimatedFrame && isExportingWithFrame) {

                                Log.d("export", "animatedFramePath : " + animatedFramePath);

                                filename = animatedFramePath;

                                if (musicPath != null && !musicPath.isEmpty()) {


                                    Log.d("export", " applying music : " + musicPath);

                                    applyMusicToVideo(filename, filename, true);

                                } else {
                                    imageSavedSuccess();
                                }

                                // isExportingWithFrame is true, execute the desired action

                            } else {
                                // isExportingWithFrame is false, continue checking after a delay
                                handler.postDelayed(this, 1000); // Check every second
                            }
                        }
                    };

                    handler.postDelayed(checkExportingStatus, 1000);

                } else {

                    if (musicPath != null && !musicPath.isEmpty()) {
                        imageToVideo(framePath);
                    } else {

                        filename = storeImage();

                        imageSavedSuccess();

                    }
                }

            }
        }

    }

    private String storeImage() {
        Bitmap image = BitmapFactory.decodeFile(filename);

        File pictureFile = new File(MyUtils.getStoreVideoExternalStorage(context), new File(filename).getName());
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        new File(filename).delete();

        return pictureFile.getAbsolutePath();
    }

    private void imageToVideo(String framePath) {
        String outputDir = MyUtils.getAppFolder(context) + "/image_video" + System.currentTimeMillis() + ".mp4";

        runOnUiThread(() -> {

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(musicPath);
            int duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            int height = background_img.getHeight();
            int width = background_img.getWidth();

            String[] ffmpegCommand = {
                    "-loop", "1", // Loop the image
                    "-i", framePath, // Input image file path
                    "-c:v", "libx264", // Video codec
                    "-t", String.valueOf(duration / 1000.0), // Duration of the output video (in seconds)
                    "-s", width + "x" + height, // Output video dimensions
                    "-vf", "fps=30", // Frame rate (adjust as needed)
                    "-pix_fmt", "yuv420p", // Pixel format
                    outputDir // Output video file path
            };

            FFmpeg.executeAsync(ffmpegCommand, (executionId, returnCode) -> {
                if (returnCode == 1) {
                    FFmpeg.cancel(executionId);
                    prgDialog.dismiss();
                    MyUtils.showToast(context, "Try Again!!");

                }
                if (returnCode == 0) {

                    applyMusicToVideo(outputDir, framePath, true);

                } else if (returnCode == 255) {
                    Log.e("finalProcess__", "Command execution cancelled by user.");
                } else {
                    String str = String.format("Command execution failed with rc=%d and the output below.", Arrays.copyOf(new Object[]{Integer.valueOf(returnCode)}, 1));
                    Log.i("finalProcess__", str);
                }

            });
        });
    }

    private void applyMusicToVideo(String path, String framePath, boolean isFrameApplied) {
        Log.e("finalProcess__", "applyMp3OnVideo " + path);
        runOnUiThread(() -> {

            String outputDir = MyUtils.getStoreVideoExternalStorage(context) + "/" +

                    getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".mp4";

            FFmpeg.executeAsync(new String[]{"-i", path, "-i", musicPath, "-c:v", "copy", "-c:a", "copy", "-map", "0:v:0", "-map", "1:a:0", "-shortest", outputDir}, (executionId, returnCode) -> {
                if (returnCode == 1) {
                    FFmpeg.cancel(executionId);
                    prgDialog.dismiss();
                    MyUtils.showToast(context, "Try Again!!");


                }
                if (returnCode == 0) {

                    if (isFrameApplied) {
                        universalDialog.cancel();
                        filename = outputDir;
                        imageSavedSuccess();
                    } else {
                        applyFrameOnVideo(framePath);
                    }


                } else if (returnCode == 255) {
                    Log.e("finalProcess__", "Command execution cancelled by user.");
                } else {
                    String str = String.format("Command execution failed with rc=%d and the output below.", Arrays.copyOf(new Object[]{Integer.valueOf(returnCode)}, 1));
                    Log.i("finalProcess__", str);
                }
            });
        });
    }

    private void applyFrameOnVideo(String framePath) {
        Log.e("export", "apply videoFrame method : " + backgroundPosterPath);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isAnimatedFrame) {
                    Handler handler = new Handler();
                    Runnable checkExportingStatus = new Runnable() {
                        @Override
                        public void run() {
                            if (isAnimatedFrame && isExportingWithFrame) {

                                Log.d("export", "saved Animated Frame " + animatedFramePath);
                                applyFrameNMusicProcess(framePath);

                                // isExportingWithFrame is true, execute the desired action

                            } else {
                                // isExportingWithFrame is false, continue checking after a delay
                                handler.postDelayed(this, 1000); // Check every second
                            }
                        }
                    };


                    handler.postDelayed(checkExportingStatus, 1000);


                } else {

                    applyFrameNMusicProcess(framePath);

                }


            }
        });
    }

    private String convertViewToPng(View mainLayout) {

        lottieAnimationFrame.setVisibility(GONE);
        // Create a bitmap with the same dimensions as the view
        Bitmap bitmap = Bitmap.createBitmap(mainLayout.getWidth(), mainLayout.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a canvas with the bitmap
        Canvas canvas = new Canvas(bitmap);

        // Draw the view onto the canvas
        mainLayout.draw(canvas);

        // Generate a file path for the PNG image
        String outputPath = MyUtils.getFolderPath(context, "cache") + "/viewimage.png";

        try {
            // Create a file output stream
            FileOutputStream outputStream = new FileOutputStream(outputPath);

            // Compress the bitmap as PNG and write it to the output stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            // Close the output stream
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        lottieAnimationFrame.setVisibility(VISIBLE);
        return outputPath;
    }

    public static String convertViewToPng(Activity context, View mainLayout, int desiredWidth, int desiredHeight) {
        // Create a bitmap with the same dimensions as the view
        Bitmap originalBitmap = Bitmap.createBitmap(mainLayout.getWidth(), mainLayout.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a canvas with the original bitmap
        Canvas originalCanvas = new Canvas(originalBitmap);

        // Draw the view onto the original canvas
        mainLayout.draw(originalCanvas);

        // Enlarge the original image to the desired dimensions
        Bitmap enlargedBitmap = enlargeImage(originalBitmap, desiredWidth, desiredHeight);

        // Generate a file path for the PNG image
        String outputPath = MyUtils.getFolderPath(context, "cache") + "/viewimage.png";

        try {
            // Create a file output stream
            FileOutputStream outputStream = new FileOutputStream(outputPath);

            // Compress the enlarged bitmap as PNG and write it to the output stream
            enlargedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            // Close the output stream
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputPath;
    }

    public static Bitmap enlargeImage(Bitmap original, int desiredWidth, int desiredHeight) {
        // Create a new bitmap with the desired dimensions
        Bitmap enlargedBitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Bitmap.Config.ARGB_8888);

        // Create a canvas with the new bitmap
        Canvas canvas = new Canvas(enlargedBitmap);

        // Calculate scaling factors to fit the original image into the new dimensions
        float scaleX = (float) desiredWidth / original.getWidth();
        float scaleY = (float) desiredHeight / original.getHeight();

        // Choose the smaller scale to fit the entire original image within the new dimensions
        float scale = Math.min(scaleX, scaleY);

        // Calculate translation (to center the original image)
        float translateX = (desiredWidth - original.getWidth() * scale) / 2;
        float translateY = (desiredHeight - original.getHeight() * scale) / 2;

        // Apply scaling and translation to the canvas
        canvas.scale(scale, scale);
        canvas.translate(translateX, translateY);

        // Draw the original image onto the new canvas
        canvas.drawBitmap(original, 0, 0, null);

        return enlargedBitmap;
    }
    private void applyFrameNMusicProcess(String framePath) {

        String outputDir = MyUtils.getStoreVideoExternalStorage(context) + File.separator + System.currentTimeMillis() + ".mp4";

        Log.d("export", "finalVideo+Music :" + backgroundPosterPath + " : animated :" + animatedFramePath + "::");

        String pngImageFrame = convertViewToPng(mainRel);

        Log.d("export", "main layout png " + pngImageFrame);

        List<String> ffmpegCommandList = new ArrayList<>();
        ffmpegCommandList.add("-i");
        ffmpegCommandList.add(backgroundPosterPath);
        ffmpegCommandList.add("-i");
        ffmpegCommandList.add(pngImageFrame);

        if (isAnimatedFrame) {

            ffmpegCommandList.add("-i");
            ffmpegCommandList.add(animatedFramePath);

        }


        if (musicPath != null && !musicPath.isEmpty()) {
            ffmpegCommandList.add("-i");
            ffmpegCommandList.add(musicPath);
        }

        ffmpegCommandList.add("-filter_complex");

        if (isAnimatedFrame) {

            ffmpegCommandList.add("[0:v][1:v]overlay[bg];[2:v]colorkey=0x000000:0.1:0.1[ckout];[bg][ckout]overlay[out]");

        } else {

            ffmpegCommandList.add("[0:v][1:v]overlay[out]");

        }

        if (musicPath != null && !musicPath.isEmpty()) {
            ffmpegCommandList.add("-map");
            ffmpegCommandList.add("[out]");
            ffmpegCommandList.add("-map");

            if (isAnimatedFrame) {

                ffmpegCommandList.add("3:a:0");
            } else {
                ffmpegCommandList.add("2:a:0");

            }

            ffmpegCommandList.add("-c:a");
            ffmpegCommandList.add("copy");
        } else {
            ffmpegCommandList.add("-map");
            ffmpegCommandList.add("[out]");
        }

        ffmpegCommandList.add("-c:v");
        ffmpegCommandList.add("libx264");
        ffmpegCommandList.add("-preset");
        ffmpegCommandList.add("ultrafast");
        ffmpegCommandList.add("-crf");
        ffmpegCommandList.add("15");
        ffmpegCommandList.add("-shortest");
        ffmpegCommandList.add("-y");
        ffmpegCommandList.add(outputDir);

        String[] ffmpegCommand = ffmpegCommandList.toArray(new String[ffmpegCommandList.size()]);

        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int returnCode) {
                if (returnCode == 1) {
                    FFmpeg.cancel(executionId);
                    prgDialog.dismiss();
                    MyUtils.showToast(context, "Try Again!!");
                } else if (returnCode == 0) {
                    FFmpeg.cancel(executionId);
                    prgDialog.dismiss();
                    filename = outputDir;
                    imageSavedSuccess();
                } else if (returnCode == 255) {
                    Log.e("finalProcess__", "Command execution cancelled by user.");
                } else {
                    String str = String.format("Command execution failed with rc=%d and the output below.", Arrays.copyOf(new Object[]{Integer.valueOf(returnCode)}, 1));
                    Log.i("finalProcess__", str);
                }
            }
        });
    }

    public void imageSavedSuccess() {

        if (prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
        Log.d("dataexported", ThumbnailActivity.this.filename);

        if (filename.contains("mp4")) {

            compressVideo();

        } else {

            openShareActivity();
        }


        prgDialog.dismiss();


    }

    public void openShareActivity() {

        MediaScannerConnection.scanFile(context, new String[]{filename}, new String[]{filename.contains("mp4") ? "video/mp4" : "image/png"}, null);

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filename))));

        Intent intent = new Intent(ThumbnailActivity.this, ShareImageActivity.class);
        intent.putExtra("uri", ThumbnailActivity.this.filename);
        intent.putExtra("way", "Poster");
        intent.putExtra("pos_id", postId);
        ThumbnailActivity.this.startActivity(intent);
    }

    private void compressVideo() {


        universalDialog.showLoadingDialog(context, "Compressing the video");

        String outputDir = filename.replace(".mp4", "") + "output.mp4";

        String[] ffmpegCommand = {
                "-i",
                filename,
                "-c:v",
                "libx264",
                "-c:a",
                "copy",
                "-crf",
                "15",
                outputDir
        };

        FFmpeg.executeAsync(ffmpegCommand, (executionId, returnCode) -> {
            if (returnCode == 1) {

                FFmpeg.cancel(executionId);
                universalDialog.dissmissLoadingDialog();
                MyUtils.showToast(context, "Try Again!!");

            } else if (returnCode == 0) {
                FFmpeg.cancel(executionId);
                universalDialog.dissmissLoadingDialog();

                if (new File(filename).exists()) {
                    // delete Previous file.
                    new File(filename).delete();

                }
                filename = outputDir;

                openShareActivity();

            } else if (returnCode == 255) {
                Log.e("finalProcess__", "Command execution cancelled by user.");
            } else {
                String str = String.format("Command execution failed with rc=%d and the output below.", Arrays.copyOf(new Object[]{Integer.valueOf(returnCode)}, 1));
                Log.i("finalProcess__", str);
            }
        });

    }


    private void selectControl9() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
    }


    private void setRightShadow() {
        this.leftRightShadow += 4;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setLeftRightShadow((float) this.leftRightShadow);
                }
            }
        }
    }

    private void setLeftShadow() {
        this.leftRightShadow -= 4;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setLeftRightShadow((float) this.leftRightShadow);
                }
            }
        }
    }

    private void setBottomShadow() {
        this.topBottomShadow += 4;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTopBottomShadow((float) this.topBottomShadow);
                }
            }
        }
    }

    private void setTopShadow() {
        this.topBottomShadow -= 4;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTopBottomShadow((float) this.topBottomShadow);
                }
            }
        }
    }

    private void mainControlcolorPickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                ThumbnailActivity.this.updateColor(i);
            }

            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Log.e(ThumbnailActivity.TAG, "onCancel: ");
            }
        }).show();
    }

    private void mainControlShadowPickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                ThumbnailActivity.this.updateShadow(i);
            }

            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Log.e(ThumbnailActivity.TAG, "onCancel: ");
            }
        }).show();
    }

    private void mainControlOutlinePickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                ThumbnailActivity.this.updateOutline(i);
            }

            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Log.e(ThumbnailActivity.TAG, "onCancel: ");
            }
        }).show();
    }

    private void mainControlBgPickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                ThumbnailActivity.this.updateBgColor(i);
            }

            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Log.e(ThumbnailActivity.TAG, "onCancel: ");
            }
        }).show();
    }

    private void showResContainer() {
        this.btnUpDown.animate().setDuration(500).start();
        this.btnUpDown.setBackgroundResource(R.drawable.textlib_down);
        this.seekbarContainer.setVisibility(View.VISIBLE);
        this.layStkrMain.startAnimation(this.animSlideUp);
        this.layStkrMain.requestLayout();
        this.layStkrMain.postInvalidate();
        this.layStkrMain.post(() -> {
            ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
            thumbnailActivity.stickerScrollView(thumbnailActivity.focusedView);
        });
    }

    private void hideResContainer() {
        this.btnUpDown.animate().setDuration(500).start();
        this.btnUpDown.setBackgroundResource(R.drawable.textlib_up);
        this.seekbarContainer.setVisibility(GONE);
        this.layStkrMain.startAnimation(this.animSlideDown);
        this.layStkrMain.requestLayout();
        this.layStkrMain.postInvalidate();
        this.layStkrMain.post(() -> {
            ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
            thumbnailActivity.stickerScrollView(thumbnailActivity.focusedView);
        });
    }

    private void showTextResContainer() {
        this.btnUpDown1.animate().setDuration(500).start();
        this.btnUpDown1.setBackgroundResource(R.drawable.textlib_down);
        this.layTextedit.setVisibility(View.VISIBLE);
        this.layTextMain.startAnimation(this.animSlideUp);
        this.layTextMain.requestLayout();
        this.layTextMain.postInvalidate();
        this.layTextMain.post(() -> {
            ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
            thumbnailActivity.stickerScrollView(thumbnailActivity.focusedView);
        });
    }

    private void hideTextResContainer() {
        this.btnUpDown1.animate().setDuration(500).start();
        this.btnUpDown1.setBackgroundResource(R.drawable.textlib_up);
        this.layTextMain.startAnimation(this.animSlideDown);
        this.layTextedit.setVisibility(GONE);
        this.layTextMain.requestLayout();
        this.layTextMain.postInvalidate();
        this.layTextMain.post(() -> {
            ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
            thumbnailActivity.stickerScrollView(thumbnailActivity.focusedView);
        });
    }

    public void stickerScrollView(View view) {
        float f;
        if (view != null) {
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            float width = (float) view.getWidth();
            float height = (float) view.getHeight();
            boolean z = view instanceof StickerView;
            if (z) {
                f = view.getRotation();
            } else {
                f = view.getRotation();
            }
            int[] iArr2 = new int[2];
            this.layScroll.getLocationOnScreen(iArr2);
            this.parentY = (float) iArr2[1];
            float x = view.getX();
            float y = view.getY();
            float f2 = this.parentY;
            float f3 = y + f2;
            this.distance = f2 - ((float) ImageUtils.dpToPx(this, 50.0f));
            Matrix matrix = new Matrix();
            RectF rectF = new RectF(x, f3, x + width, f3 + height);
            matrix.postRotate(f, x + (width / 2.0f), f3 + (height / 2.0f));
            matrix.mapRect(rectF);
            int i = iArr[1];
            float max = Math.max(rectF.top, rectF.bottom);
            float scrollY = (float) this.layScroll.getScrollY();
            if (scrollY > 0.0f) {
                max -= scrollY;
            }
            int[] iArr3 = new int[2];
            if (z) {
                this.seekbarContainer.getLocationOnScreen(iArr3);
            } else {
                this.layTextedit.getLocationOnScreen(iArr3);
            }
            float f4 = (float) iArr3[1];
            if (this.parentY + ((float) this.layScroll.getHeight()) < max) {
                max = this.parentY + ((float) this.layScroll.getHeight());
            }
            if (max > f4) {
                this.distanceScroll = (int) (max - f4);
                int i2 = this.distanceScroll;
                this.dsfc = i2;
                if (((float) i2) < this.distance) {
                    this.layScroll.setY((this.parentY - ((float) ImageUtils.dpToPx(this, 50.0f))) - ((float) this.distanceScroll));
                } else {
                    int scrollY2 = this.layScroll.getScrollY();
                    this.layScroll.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
                    this.layScroll.postInvalidate();
                    this.layScroll.requestLayout();
                    int i3 = (int) ((max - this.distance) - f4);
                    this.distanceScroll = scrollY2 + i3;
                    this.layScroll.getLayoutParams().height = this.layScroll.getHeight() - i3;
                    this.layScroll.postInvalidate();
                    this.layScroll.requestLayout();
                }
                this.layScroll.post(() -> {
                    if (ThumbnailActivity.this.layScroll.getY() < 0.0f) {
                        ThumbnailActivity.this.layScroll.setY(0.0f);
                    }
                    ThumbnailActivity.this.btn_bck1.performClick();
                });
            }
        }
    }

    public void setTextBgTexture(String str) {
        getResources().getIdentifier(str, "drawable", getPackageName());
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setBgDrawable(str);
                    autofitTextRel.setBgAlpha(this.seekBar3.getProgress());
                    this.bgColor = 0;
                    ((AutofitTextRel) txtStkrRel.getChildAt(i)).getTextInfo().setBG_DRAWABLE(str);
                    this.bgDrawable = autofitTextRel.getBgDrawable();
                    this.bgAlpha = this.seekBar3.getProgress();
                }
            }
        }
    }

    public void setTextFonts(String str) {
        this.fontName = str;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {

                    autofitTextRel.setTextFont(str);

                    saveBitmapUndu();

                }
            }
        }
    }

    private void setLetterApacing() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.applyLetterSpacing(this.letterSpacing);
                }
            }
        }
    }

    private void setLineApacing() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.applyLineSpacing(this.lineSpacing);
                }
            }
        }
    }

    private void setBoldFonts() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setBoldFont();
                }
            }
        }
    }

    private void setCapitalFont() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setCapitalFont();
                }
            }
        }
    }

    private void setUnderLineFont() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setUnderLineFont();
                }
            }
        }
    }

    private void setItalicFont() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setItalicFont();
                }
            }
        }
    }

    private void setLeftAlignMent() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setLeftAlignMent();
                }
            }
        }
    }

    private void setCenterAlignMent() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setCenterAlignMent();
                }
            }
        }
    }

    private void setRightAlignMent() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setRightAlignMent();
                }
            }
        }
    }

    public void setBitmapOverlay(int i) {
        this.layFilter.setVisibility(View.VISIBLE);
        this.transImg.setVisibility(View.VISIBLE);
        try {
            this.transImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), i));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), i, options2);
            BitmapFactory.Options options3 = new BitmapFactory.Options();
            options3.inSampleSize = ImageUtils.getClosestResampleSize(options2.outWidth, options2.outHeight, this.mainRel.getWidth() < this.mainRel.getHeight() ? this.mainRel.getWidth() : this.mainRel.getHeight());
            options2.inJustDecodeBounds = false;
            this.transImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), i, options3));
        }
    }

    public void updateColor(int i) {
        int childCount = txtStkrRel.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = txtStkrRel.getChildAt(i2);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTextColor(i);
                    this.tColor = i;
                    this.textColorSet = i;
                    this.horizontalPicker.setSelectedColor(i);
                    saveBitmapUndu();
                }
            }
            if (childAt instanceof StickerView) {
                StickerView stickerView = (StickerView) childAt;
                if (stickerView.getBorderVisbilty()) {
                    stickerView.setColor(i);
                    this.stkrColorSet = i;
                    this.horizontalPickerColor.setSelectedColor(i);
                    saveBitmapUndu();
                }
            }
        }
    }


    public void updateShadow(int i) {
        int childCount = txtStkrRel.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = txtStkrRel.getChildAt(i2);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTextShadowColor(i);
                    this.shadowColor = i;
                    saveBitmapUndu();
                }
            }
        }
    }


    public void updateOutline(int i) {
        int childCount = txtStkrRel.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = txtStkrRel.getChildAt(i2);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTextOutlineColor(i);
                    this.shadowColor = i;
                    saveBitmapUndu();
                }
            }
        }
    }


    public void updateBgColor(int i) {
        int childCount = txtStkrRel.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = txtStkrRel.getChildAt(i2);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setBgAlpha(this.seekBar3.getProgress());
                    autofitTextRel.setBgColor(i);
                    this.bgColor = i;
                    this.bgDrawable = "0";
                    saveBitmapUndu();
                }
            }
        }
    }


    public void updatePositionSticker(String str) {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    if (str.equals("incrX")) {
                        autofitTextRel.incrX();
                    }
                    if (str.equals("decX")) {
                        autofitTextRel.decX();
                    }
                    if (str.equals("incrY")) {
                        autofitTextRel.incrY();
                    }
                    if (str.equals("decY")) {
                        autofitTextRel.decY();
                    }
                }
            }
            if (childAt instanceof StickerView) {
                StickerView stickerView = (StickerView) childAt;
                if (stickerView.getBorderVisbilty()) {
                    if (str.equals("incrX")) {
                        stickerView.incrX();
                    }
                    if (str.equals("decX")) {
                        stickerView.decX();
                    }
                    if (str.equals("incrY")) {
                        stickerView.incrY();
                    }
                    if (str.equals("decY")) {
                        stickerView.decY();
                    }
                }
            }
        }
    }

    private boolean closeViewAll() {
        this.mViewAllFrame.removeAllViews();
        this.mViewAllFrame.setVisibility(GONE);
        return false;
    }


    public void saveComponent1(long j, DatabaseHandler databaseHandler) {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                TextInfo textInfo = ((AutofitTextRel) childAt).getTextInfo();
                textInfo.setTEMPLATE_ID((int) j);
                textInfo.setORDER(i);
                textInfo.setTYPE("TEXT");
                databaseHandler.insertTextRow(textInfo);
            } else {
                saveShapeAndSticker(j, i, TYPE_STICKER, databaseHandler);
            }
        }
    }

    public void saveShapeAndSticker(long j, int i, int i2, DatabaseHandler databaseHandler) {

        ElementInfoPoster componentInfo = ((StickerView) txtStkrRel.getChildAt(i)).getComponentInfo();
        componentInfo.setTEMPLATE_ID((int) j);
        componentInfo.setTYPE("STICKER");
        componentInfo.setORDER(i);
        databaseHandler.insertComponentInfoRow(componentInfo);

    }

    public void addTextDialog(final TextInfo originTextInfo) {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.add_text_dialog);
        dialog.setCancelable(false);
        final AutoFitEditText autoFitEditText = (AutoFitEditText) dialog.findViewById(R.id.auto_fit_edit_text);
        LinearLayout button = (LinearLayout) dialog.findViewById(R.id.btnCancelDialog);
        LinearLayout button2 = (LinearLayout) dialog.findViewById(R.id.btnAddTextSDialog);
        if (originTextInfo != null) {
            autoFitEditText.setText(originTextInfo.getTEXT());
        } else {
            autoFitEditText.setText("");
        }
        button.setOnClickListener(view -> dialog.dismiss());
        button2.setOnClickListener(view -> {
            if (autoFitEditText.getText().toString().trim().length() > 0) {
                String replace = autoFitEditText.getText().toString().replace("\n", " ");
                TextInfo textInfo = new TextInfo();
                if (ThumbnailActivity.this.editMode) {
                    textInfo.setTEXT(replace);
                    try {
                        if (originTextInfo != null) {
                            textInfo.setFONT_NAME(originTextInfo.getFONT_NAME());
                            textInfo.setTEXT_COLOR(originTextInfo.getTEXT_COLOR());
                            textInfo.setTEXT_ALPHA(originTextInfo.getTEXT_ALPHA());
                            textInfo.setSHADOW_COLOR(originTextInfo.getSHADOW_COLOR());
                            textInfo.setSHADOW_PROG(originTextInfo.getSHADOW_PROG());
                            textInfo.setBG_COLOR(originTextInfo.getBG_COLOR());
                            textInfo.setBG_DRAWABLE(originTextInfo.getBG_DRAWABLE());
                            textInfo.setBG_ALPHA(originTextInfo.getBG_ALPHA());
                            textInfo.setROTATION(originTextInfo.getROTATION());
                            textInfo.setFIELD_TWO("");
                            textInfo.setPOS_X(originTextInfo.getPOS_X());
                            textInfo.setPOS_Y(originTextInfo.getPOS_Y());
                            textInfo.setWIDTH(originTextInfo.getWIDTH());
                            textInfo.setHEIGHT(originTextInfo.getHEIGHT());
                        } else {
                            textInfo.setFONT_NAME(ThumbnailActivity.this.fontName);
                            textInfo.setTEXT_COLOR(ViewCompat.MEASURED_STATE_MASK);
                            textInfo.setTEXT_ALPHA(100);
                            textInfo.setSHADOW_COLOR(ViewCompat.MEASURED_STATE_MASK);
                            textInfo.setSHADOW_PROG(0);
                            textInfo.setBG_COLOR(ViewCompat.MEASURED_STATE_MASK);
                            textInfo.setBG_DRAWABLE("0");
                            textInfo.setBG_ALPHA(0);
                            textInfo.setROTATION(0.0f);
                            textInfo.setFIELD_TWO("");
                            textInfo.setPOS_X((float) ((ThumbnailActivity.txtStkrRel.getWidth() / 2) - ImageUtils.dpToPx(ThumbnailActivity.this, 100.0f)));
                            textInfo.setPOS_Y((float) ((ThumbnailActivity.txtStkrRel.getHeight() / 2) - ImageUtils.dpToPx(ThumbnailActivity.this, 100.0f)));
                            textInfo.setWIDTH(ImageUtils.dpToPx(ThumbnailActivity.this, 200.0f));
                            textInfo.setHEIGHT(ImageUtils.dpToPx(ThumbnailActivity.this, 200.0f));
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        textInfo.setFONT_NAME(ThumbnailActivity.this.fontName);
                        textInfo.setTEXT_COLOR(ViewCompat.MEASURED_STATE_MASK);
                        textInfo.setTEXT_ALPHA(100);
                        textInfo.setSHADOW_COLOR(ViewCompat.MEASURED_STATE_MASK);
                        textInfo.setSHADOW_PROG(0);
                        textInfo.setBG_COLOR(ViewCompat.MEASURED_STATE_MASK);
                        textInfo.setBG_DRAWABLE("0");
                        textInfo.setBG_ALPHA(0);
                        textInfo.setROTATION(0.0f);
                        textInfo.setFIELD_TWO("");
                        textInfo.setPOS_X((float) ((ThumbnailActivity.txtStkrRel.getWidth() / 2) - ImageUtils.dpToPx(ThumbnailActivity.this, 100.0f)));
                        textInfo.setPOS_Y((float) ((ThumbnailActivity.txtStkrRel.getHeight() / 2) - ImageUtils.dpToPx(ThumbnailActivity.this, 100.0f)));
                        textInfo.setWIDTH(ImageUtils.dpToPx(ThumbnailActivity.this, 200.0f));
                        textInfo.setHEIGHT(ImageUtils.dpToPx(ThumbnailActivity.this, 200.0f));
                    }
                    int childCount = ThumbnailActivity.txtStkrRel.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childAt = ThumbnailActivity.txtStkrRel.getChildAt(i);
                        if (childAt instanceof AutofitTextRel) {
                            AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                            if (autofitTextRel.getBorderVisibility()) {
                                autofitTextRel.setTextInfo(textInfo, false);
                                autofitTextRel.setBorderVisibility(true);
                                boolean unused = ThumbnailActivity.this.editMode = false;
                            }
                        }
                    }
                } else {
                    textInfo.setTEXT(replace);
                    textInfo.setFONT_NAME(ThumbnailActivity.this.fontName);
                    textInfo.setTEXT_COLOR(ViewCompat.MEASURED_STATE_MASK);
                    textInfo.setTEXT_ALPHA(100);
                    textInfo.setSHADOW_COLOR(ViewCompat.MEASURED_STATE_MASK);
                    textInfo.setSHADOW_PROG(0);
                    textInfo.setBG_COLOR(ViewCompat.MEASURED_STATE_MASK);
                    textInfo.setBG_DRAWABLE("0");
                    textInfo.setBG_ALPHA(0);
                    textInfo.setROTATION(0.0f);
                    textInfo.setFIELD_TWO("");
                    textInfo.setPOS_X((float) ((ThumbnailActivity.txtStkrRel.getWidth() / 2) - ImageUtils.dpToPx(ThumbnailActivity.this, 100.0f)));
                    textInfo.setPOS_Y((float) ((ThumbnailActivity.txtStkrRel.getHeight() / 2) - ImageUtils.dpToPx(ThumbnailActivity.this, 100.0f)));
                    textInfo.setWIDTH(ImageUtils.dpToPx(ThumbnailActivity.this, 200.0f));
                    textInfo.setHEIGHT(ImageUtils.dpToPx(ThumbnailActivity.this, 200.0f));
                    try {
                        ThumbnailActivity.this.verticalSeekBar.setProgress(100);
                        ThumbnailActivity.this.seekbarShadow.setProgress(0);
                        ThumbnailActivity.this.seekBar3.setProgress(255);
                        AutofitTextRel autofitTextRel2 = new AutofitTextRel(ThumbnailActivity.this);
                        ThumbnailActivity.txtStkrRel.addView(autofitTextRel2);
                        autofitTextRel2.setTextInfo(textInfo, false);
                        autofitTextRel2.setId(ViewIdGenerator.generateViewId());
                        autofitTextRel2.setOnTouchCallbackListener(ThumbnailActivity.this);
                        autofitTextRel2.setBorderVisibility(true);
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        e2.printStackTrace();
                    }
                }
                if (ThumbnailActivity.this.layTextMain.getVisibility() == GONE) {
                    ThumbnailActivity.this.layTextMain.setVisibility(View.VISIBLE);
                    ThumbnailActivity.this.layTextMain.startAnimation(ThumbnailActivity.this.animSlideUp);
                }
                ThumbnailActivity.this.saveBitmapUndu();
                dialog.dismiss();
                return;
            }
            Toast.makeText(ThumbnailActivity.this, "Please enter text here.", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }


    public void onTouchApply() {

        removeScroll();

        if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            this.layStkrMain.startAnimation(this.animSlideDown);
            this.layStkrMain.setVisibility(GONE);
        }
        if (this.layTextMain.getVisibility() == View.VISIBLE) {
            this.layTextMain.startAnimation(this.animSlideDown);
            this.layTextMain.setVisibility(GONE);
        }
        if (this.showtailsSeek) {
            this.layHandletails.setVisibility(View.VISIBLE);
        }
        if (this.seekbarContainer.getVisibility() == GONE) {
            this.seekbarContainer.clearAnimation();
            this.layTextMain.clearAnimation();
            this.seekbarContainer.setVisibility(View.VISIBLE);
            this.seekbarContainer.startAnimation(this.animSlideUp);
        }
        this.layStkrMain.clearAnimation();
        this.layTextMain.clearAnimation();
        removeImageViewControll();
        hideSlideBar();
    }

    public void onSnapFilter(int i, int i2, String str) {
        this.laySticker.setVisibility(GONE);
        btn_layControls.setVisibility(View.VISIBLE);
        if (this.layTextMain.getVisibility() == View.VISIBLE) {
            this.layTextMain.startAnimation(this.animSlideDown);
            this.layTextMain.setVisibility(GONE);
        }
        if (i2 == 104) {
            if (str != null) {
                this.isBackground = true;
                Uri fromFile = Uri.fromFile(new File(str));
                Uri fromFile2 = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                String[] split = this.ratio.split(":");
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                UCrop.Options options2 = new UCrop.Options();
                options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
                options2.setToolbarColor(getResources().getColor(R.color.white));
                options2.setToolbarWidgetColor(getResources().getColor(R.color.color_add_btn));
                options2.setFreeStyleCropEnabled(false);
                UCrop.of(fromFile, fromFile2).withOptions(options2).withAspectRatio((float) parseInt, (float) parseInt2).start(this);
            }
        } else if (!str.equals("")) {
            this.imgOK.setVisibility(View.VISIBLE);
            this.btnErase.setVisibility(View.VISIBLE);
            if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
                getSupportFragmentManager().popBackStack();
            }
            if (i2 == 50) {
                this.colorType = "white";
            } else {
                this.colorType = "white";
            }
            addSticker("", str, null);
        }
    }

    public void onSnapFilter(ArrayList<BackgroundImage> arrayList, int i) {
        if (i == 0) {
            seeMoreSticker(arrayList);
        } else {
            seeMore(arrayList);
        }
    }

    private void seeMoreSticker(ArrayList<BackgroundImage> arrayList) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        StickerFragmentMore stickerFragmentMore = (StickerFragmentMore) supportFragmentManager.findFragmentByTag("sticker_list");
        if (stickerFragmentMore != null) {
            beginTransaction.remove(stickerFragmentMore);
        }
        StickerFragmentMore newInstance = StickerFragmentMore.newInstance(arrayList);
        this.mFragments.add(new WeakReference(newInstance));
        beginTransaction.setCustomAnimations(R.anim.push_left_in_no_alpha, R.anim.push_left_out_no_alpha);
        beginTransaction.add(R.id.frameContainerSticker, newInstance, "sticker_list");
        beginTransaction.addToBackStack("sticker_list");
        try {
            beginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seeMore(ArrayList<BackgroundImage> arrayList) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        BackgroundFragment2 backgroundFragment2 = (BackgroundFragment2) supportFragmentManager.findFragmentByTag("back_category_frgm_2");
        if (backgroundFragment2 != null) {
            beginTransaction.remove(backgroundFragment2);
        }
        BackgroundFragment2 newInstance = BackgroundFragment2.newInstance(arrayList);
        this.mFragments.add(new WeakReference(newInstance));
        beginTransaction.setCustomAnimations(R.anim.push_left_in_no_alpha, R.anim.push_left_out_no_alpha);
        beginTransaction.add(R.id.frameContainerBackground, newInstance, "back_category_frgm_2");
        beginTransaction.addToBackStack("back_category_frgm_2");
        try {
            beginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSticker(String str, String str2, Bitmap bitmap2) {
        if (this.layStkrMain.getVisibility() == GONE) {
            this.layStkrMain.setVisibility(View.VISIBLE);
            this.layStkrMain.startAnimation(this.animSlideUp);
        }
        if (this.colorType.equals("white")) {
            this.layColor.setVisibility(View.VISIBLE);
            this.layHue.setVisibility(GONE);
        } else {
            this.layColor.setVisibility(GONE);
            this.layHue.setVisibility(View.VISIBLE);
        }
        this.hueSeekbar.setProgress(1);
        removeImageViewControll();
        ElementInfoPoster elementInfoPoster = new ElementInfoPoster();
        elementInfoPoster.setPOS_X((float) ((this.mainRel.getWidth() / 2) - ImageUtils.dpToPx(this, 70.0f)));
        elementInfoPoster.setPOS_Y((float) ((this.mainRel.getHeight() / 2) - ImageUtils.dpToPx(this, 70.0f)));
        elementInfoPoster.setWIDTH(ImageUtils.dpToPx(this, 140.0f));
        elementInfoPoster.setHEIGHT(ImageUtils.dpToPx(this, 140.0f));
        elementInfoPoster.setROTATION(0.0f);
        elementInfoPoster.setRES_ID(str);
        elementInfoPoster.setBITMAP(bitmap2);
        elementInfoPoster.setCOLORTYPE(this.colorType);
        elementInfoPoster.setTYPE("STICKER");
        elementInfoPoster.setSTC_OPACITY(255);
        elementInfoPoster.setSTC_COLOR(0);
        elementInfoPoster.setSTKR_PATH(str2);
        elementInfoPoster.setSTC_HUE(this.hueSeekbar.getProgress());
        elementInfoPoster.setFIELD_TWO("0,0");
        StickerView stickerView = new StickerView(this);
        stickerView.optimizeScreen(this.screenWidth, this.screenHeight);
        stickerView.setViewWH((float) this.mainRel.getWidth(), (float) this.mainRel.getHeight());
        stickerView.setComponentInfo(elementInfoPoster);
        stickerView.setId(ViewIdGenerator.generateViewId());
        txtStkrRel.addView(stickerView);
        stickerView.setOnTouchCallbackListener(this);
        stickerView.setBorderVisibility(true);
        currentStickerView = stickerView;
        if (this.seekbarContainer.getVisibility() == GONE) {
            this.seekbarContainer.setVisibility(View.VISIBLE);
            this.seekbarContainer.startAnimation(this.animSlideUp);
        }
    }

    private Bitmap viewToBitmap(View view) {
        try {

            Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(createBitmap));
            return createBitmap;
        } finally {
            view.destroyDrawingCache();
        }
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int id = seekBar.getId();
        int i2 = 0;
        if (id == R.id.alpha_seekBar) {
            int childCount = txtStkrRel.getChildCount();
            while (i2 < childCount) {
                View childAt = txtStkrRel.getChildAt(i2);
                if (childAt instanceof StickerView) {
                    StickerView stickerView = (StickerView) childAt;
                    if (stickerView.getBorderVisbilty()) {
                        stickerView.setAlphaProg(i);
                    }
                }
                i2++;
            }
        } else if (id != R.id.hue_seekBar) {
            switch (id) {
                case R.id.seek:
                    this.alpha = i;
                    if (Build.VERSION.SDK_INT >= 16) {
                        this.transImg.setImageAlpha(this.alpha);
                        return;
                    } else {
                        this.transImg.setAlpha(this.alpha);
                        return;
                    }
                case R.id.seekBar2:
                    this.processs = i;
                    int childCount2 = txtStkrRel.getChildCount();
                    while (i2 < childCount2) {
                        View childAt2 = txtStkrRel.getChildAt(i2);
                        if (childAt2 instanceof AutofitTextRel) {
                            AutofitTextRel autofitTextRel = (AutofitTextRel) childAt2;
                            if (autofitTextRel.getBorderVisibility()) {
                                autofitTextRel.setTextAlpha(i);
                            }
                        }
                        i2++;
                    }
                    return;
                case R.id.seekBar3:
                    int childCount3 = txtStkrRel.getChildCount();
                    while (i2 < childCount3) {
                        View childAt3 = txtStkrRel.getChildAt(i2);
                        if (childAt3 instanceof AutofitTextRel) {
                            AutofitTextRel autofitTextRel2 = (AutofitTextRel) childAt3;
                            if (autofitTextRel2.getBorderVisibility()) {
                                autofitTextRel2.setBgAlpha(i);
                                this.bgAlpha = i;
                            }
                        }
                        i2++;
                    }
                    return;
                case R.id.seekBar_shadow:
                    int childCount4 = txtStkrRel.getChildCount();
                    while (i2 < childCount4) {
                        View childAt4 = txtStkrRel.getChildAt(i2);
                        if (childAt4 instanceof AutofitTextRel) {
                            AutofitTextRel autofitTextRel3 = (AutofitTextRel) childAt4;
                            if (autofitTextRel3.getBorderVisibility()) {
                                autofitTextRel3.setTextShadowProg(i);
                                this.shadowProg = i;
                            }
                        }
                        i2++;
                    }
                    return;
                case R.id.seekLetterSpacing:
                    this.letterSpacing = (float) (i / 3);
                    setLetterApacing();
                    return;
                case R.id.seekLineSpacing:
                    this.lineSpacing = (float) (i / 2);
                    setLineApacing();
                    return;
                case R.id.seekOutlineSize:
                    int childCount5 = txtStkrRel.getChildCount();
                    while (i2 < childCount5) {
                        View childAt5 = txtStkrRel.getChildAt(i2);
                        if (childAt5 instanceof AutofitTextRel) {
                            AutofitTextRel autofitTextRel4 = (AutofitTextRel) childAt5;
                            if (autofitTextRel4.getBorderVisibility()) {
                                autofitTextRel4.setTextOutlLine(i);
                            }
                        }
                        i2++;
                    }
                    return;
                case R.id.seekShadowBlur:
                    int childCount6 = txtStkrRel.getChildCount();
                    while (i2 < childCount6) {
                        View childAt6 = txtStkrRel.getChildAt(i2);
                        if (childAt6 instanceof AutofitTextRel) {
                            AutofitTextRel autofitTextRel5 = (AutofitTextRel) childAt6;
                            if (autofitTextRel5.getBorderVisibility()) {
                                autofitTextRel5.setTextShadowOpacity(i);
                            }
                        }
                        i2++;
                    }
                    return;
                case R.id.seekTextCurve:
                    mRadius = seekBar.getProgress() - 360;
                    int i3 = mRadius;
                    if (i3 <= 0 && i3 >= -8) {
                        mRadius = -8;
                    }
                    int childCount7 = txtStkrRel.getChildCount();
                    while (i2 < childCount7) {
                        View childAt7 = txtStkrRel.getChildAt(i2);
                        if (childAt7 instanceof AutofitTextRel) {
                            AutofitTextRel autofitTextRel6 = (AutofitTextRel) childAt7;
                            if (autofitTextRel6.getBorderVisibility()) {
                                autofitTextRel6.setDrawParams();
                            }
                        }
                        i2++;
                    }
                    return;
                case R.id.seek_blur:
                    if (i != 0) {
                        this.backgroundBlur.setVisibility(View.VISIBLE);
                        this.min = i;
                        if (Build.VERSION.SDK_INT >= 16) {
                            this.backgroundBlur.setImageAlpha(i);
                            return;
                        } else {
                            this.backgroundBlur.setAlpha(i);
                            return;
                        }
                    } else {
                        this.backgroundBlur.setVisibility(GONE);
                        return;
                    }
                case R.id.seek_tailys:
                    this.backgroundBlur.setVisibility(GONE);
                    this.seekValue = i;
                    addTilesBG(this.curTileId);
                    return;
                default:
                    return;
            }
        } else {
            int childCount8 = txtStkrRel.getChildCount();
            while (i2 < childCount8) {
                View childAt8 = txtStkrRel.getChildAt(i2);
                if (childAt8 instanceof StickerView) {
                    StickerView stickerView2 = (StickerView) childAt8;
                    if (stickerView2.getBorderVisbilty()) {
                        stickerView2.setHueProg(i);
                    }
                }
                i2++;
            }
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.alpha_seekBar:
            case R.id.hue_seekBar:
            case R.id.seekBar2:
                saveBitmapUndu();
                return;
            case R.id.seek_tailys:
                if (this.min != 0) {
                    this.backgroundBlur.setVisibility(View.VISIBLE);
                    return;
                } else {
                    this.backgroundBlur.setVisibility(GONE);
                    return;
                }
            default:
                return;
        }
    }

    private void addTilesBG(int i) {
        if (i != 0) {
            setImageBitmapAndResizeLayout1(Constant.getTiledBitmap(this, i, imgBtmap));
        }
    }

    private void setImageBitmapAndResizeLayout1(Bitmap bitmap2) {
        this.mainRel.getLayoutParams().width = bitmap2.getWidth();
        this.mainRel.getLayoutParams().height = bitmap2.getHeight();
        this.mainRel.postInvalidate();
        this.mainRel.requestLayout();
        background_img.setImageBitmap(bitmap2);
        imgBtmap = bitmap2;
    }


    public void oneTimeLayerAdjust() {
        if (this.PreferenceManager.getInt(Constant.onTimeRecentHint, 0) == 0) {
            this.PreferenceManager.setInt(Constant.onTimeRecentHint, 1);
            new Handler(getMainLooper()).postDelayed(() -> Constant.showRecentHindDialog(ThumbnailActivity.btn_layControls, ThumbnailActivity.this), 1000);
        }
    }

    public void oneTimeScrollLayer() {
        if (this.PreferenceManager.getInt(Constant.onTimeLayerScroll, 0) == 0) {
            this.PreferenceManager.setInt(Constant.onTimeLayerScroll, 1);
            new Handler(getMainLooper()).postDelayed(() -> Constant.showScrollLayerDialog(ListFragment.HintView, ThumbnailActivity.this), 1000);
        }
    }

    private void touchDown(View view, String str) {
        this.leftRightShadow = 0;
        this.topBottomShadow = 0;
        this.focusedView = view;
        if (str.equals("hideboder")) {
            removeImageViewControll();
        }
        hideSlideBar();
        if (view instanceof StickerView) {
            this.layEffects.setVisibility(GONE);
            this.layTextMain.setVisibility(GONE);
            this.layStkrMain.setVisibility(GONE);
            StickerView stickerView = (StickerView) view;
            this.stkrColorSet = stickerView.getColor();
            this.alphaSeekbar.setProgress(stickerView.getAlphaProg());
            this.hueSeekbar.setProgress(stickerView.getHueProg());
            this.lay_frames.setVisibility(GONE);

        }
        if (view instanceof AutofitTextRel) {
            this.layEffects.setVisibility(GONE);
            this.layStkrMain.setVisibility(GONE);
            this.layTextMain.setVisibility(GONE);
            this.lay_frames.setVisibility(GONE);

        }

    }

    private void hideSlideBar() {
        if (lay_container.getVisibility() == View.VISIBLE) {
            lay_container.animate().translationX((float) (-lay_container.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
            new Handler().postDelayed(() -> {
                ThumbnailActivity.lay_container.setVisibility(GONE);
                ThumbnailActivity.btn_layControls.setVisibility(View.VISIBLE);
            }, 200);
        }
    }

    private void touchMove(View view) {
        boolean z = view instanceof StickerView;
        if (z) {
            StickerView stickerView = (StickerView) view;
            this.alphaSeekbar.setProgress(stickerView.getAlphaProg());
            this.hueSeekbar.setProgress(stickerView.getHueProg());
        } else {
            this.layTextMain.setVisibility(GONE);
        }
        if (z) {
            this.layEffects.setVisibility(GONE);
            this.layTextMain.setVisibility(GONE);
            this.layStkrMain.setVisibility(GONE);
            this.lay_frames.setVisibility(GONE);

        }
        if (view instanceof AutofitTextRel) {
            this.layEffects.setVisibility(GONE);
            this.layTextMain.setVisibility(GONE);
            this.layStkrMain.setVisibility(GONE);
            this.lay_frames.setVisibility(GONE);

        }
    }

    private void touchUp(final View view) {


        Log.d("delete", "touch up");

        if (this.focusedCopy != this.focusedView) {
            this.seekbarContainer.setVisibility(View.VISIBLE);
            this.layTextedit.setVisibility(View.VISIBLE);
        }
        if (view instanceof AutofitTextRel) {
            this.rotation = view.getRotation();


            if (this.layTextMain.getVisibility() == GONE) {
                this.layTextMain.setVisibility(View.VISIBLE);
                this.layTextMain.startAnimation(this.animSlideUp);
                this.layTextMain.post(() -> ThumbnailActivity.this.stickerScrollView(view));
            }
            int i = this.processs;
            if (i != 0) {
                this.verticalSeekBar.setProgress(i);
            }
        }
        if ((view instanceof StickerView) && this.layStkrMain.getVisibility() == GONE) {
            if (("" + ((StickerView) view).getColorType()).equals("white")) {
                this.layColor.setVisibility(View.VISIBLE);
                this.layHue.setVisibility(GONE);
            } else {
                this.layColor.setVisibility(GONE);
                this.layHue.setVisibility(View.VISIBLE);
            }
            currentStickerView = (StickerView) view;
            this.layStkrMain.setVisibility(View.VISIBLE);
            this.layStkrMain.startAnimation(this.animSlideUp);
            this.layStkrMain.post(() -> ThumbnailActivity.this.stickerScrollView(view));
        }
        if (this.seekbarContainer.getVisibility() == GONE) {
            this.seekbarContainer.startAnimation(this.animSlideDown);
            this.seekbarContainer.setVisibility(GONE);
        }
    }

    public void onDelete() {
        removeScroll();
        if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            this.layStkrMain.startAnimation(this.animSlideDown);
            this.layStkrMain.setVisibility(GONE);
        }
        if (this.layTextMain.getVisibility() == GONE) {
            this.layTextMain.startAnimation(this.animSlideDown);
            this.layTextMain.setVisibility(GONE);
        }
        saveBitmapUndu();
    }

    public void onRotateDown(View view) {
        touchDown(view, "viewboder");
    }

    public void onRotateMove(View view) {
        touchMove(view);
    }

    public void onRotateUp(View view) {
        touchUp(view);
    }

    public void onScaleDown(View view) {
        touchDown(view, "viewboder");
    }

    public void onScaleMove(View view) {
        touchMove(view);
    }

    public void onScaleUp(View view) {
        touchUp(view);
    }

    public void onTouchDown(View view) {
        touchDown(view, "hideboder");


        if (this.checkTouchContinue) {
            this.layStkrMain.post(() -> {
                ThumbnailActivity.this.checkTouchContinue = true;
                ThumbnailActivity.this.mHandler.post(ThumbnailActivity.this.mStatusChecker);
            });
        }
    }

    public void onTouchMove(View view) {
        touchMove(view);
    }

    public void onTouchUp(View view) {
        this.checkTouchContinue = false;
        this.mHandler.removeCallbacks(this.mStatusChecker);
        touchUp(view);
    }

    public void onTouchMoveUpClick(View view) {
        saveBitmapUndu();
    }

    public void onDoubleTap() {
        doubleTabPrass();
    }

    private void doubleTabPrass() {


        this.editMode = true;

        try {

            int childCount3 = txtStkrRel.getChildCount();

            for (int i3 = 0; i3 < childCount3; i3++) {
                View childAt3 = txtStkrRel.getChildAt(i3);
                if (childAt3 instanceof AutofitTextRel) {
                    AutofitTextRel autofitTextRel = (AutofitTextRel) childAt3;
                    if (autofitTextRel.getBorderVisibility()) {
                        TextInfo textInfo = autofitTextRel.getTextInfo();

                        this.layEffects.setVisibility(GONE);
                        this.layStkrMain.setVisibility(GONE);
                        this.layTextMain.setVisibility(GONE);
                        this.lay_frames.setVisibility(GONE);


                        addTextDialog(textInfo);

                    }
                }
            }


        } catch (NullPointerException e) {

            e.printStackTrace();

        }
    }

    private void removeScroll() {
       /* int[] iArr = new int[2];
        this.layScroll.getLocationOnScreen(iArr);
        final float f = (float) iArr[1];
        final float dpToPx = (float) ImageUtils.dpToPx(this, 50.0f);
        if (f != dpToPx) {
            this.layScroll.setY(this.yAtLayoutCenter - ((float) ImageUtils.dpToPx(this, 50.0f)));
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.layScroll.setLayoutParams(layoutParams);
        this.layScroll.postInvalidate();
        this.layScroll.requestLayout();
        this.layScroll.post(() -> {
            if (f != dpToPx) {
                ThumbnailActivity.this.layScroll.setY(ThumbnailActivity.this.yAtLayoutCenter - ((float) ImageUtils.dpToPx(ThumbnailActivity.this, 50.0f)));
            }
        });
*/

    }

    public void removeImageViewControll() {
        RelativeLayout relativeLayout = txtStkrRel;
        if (relativeLayout != null) {
            int childCount = relativeLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = txtStkrRel.getChildAt(i);
                if (childAt instanceof AutofitTextRel) {
                    ((AutofitTextRel) childAt).setBorderVisibility(false);
                }

                if (childAt instanceof StickerView) {
                    Log.e("remove", "yessss==");
                    ((StickerView) childAt).setBorderVisibility(false);
                }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (file != null)
            outState.putString("uri_file_path", file.toString());
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        int i3 = i;
        int i4 = i2;
        Intent intent2 = intent;
        super.onActivityResult(i, i2, intent);
        if (i4 == -1) {
            this.layStkrMain.setVisibility(GONE);
            if (intent2 != null || i3 == SELECT_PICTURE_FROM_CAMERA || i3 == 4 || i3 == TEXT_ACTIVITY) {
                Bundle bundle = null;
                if (i3 == TEXT_ACTIVITY) {
                    bundle = intent.getExtras();
                    TextInfo textInfo = new TextInfo();
                    textInfo.setPOS_X(bundle.getFloat("X", 0.0f));
                    textInfo.setPOS_Y(bundle.getFloat("Y", 0.0f));
                    textInfo.setWIDTH(bundle.getInt("wi", ImageUtils.dpToPx(this, 200.0f)));
                    textInfo.setHEIGHT(bundle.getInt("he", ImageUtils.dpToPx(this, 200.0f)));
                    textInfo.setTEXT(bundle.getString("text", ""));
                    textInfo.setFONT_NAME(bundle.getString("fontName", ""));
                    textInfo.setTEXT_COLOR(bundle.getInt("tColor", Color.parseColor("#4149b6")));
                    textInfo.setTEXT_ALPHA(bundle.getInt("tAlpha", 100));
                    textInfo.setSHADOW_COLOR(bundle.getInt("shadowColor", Color.parseColor("#7641b6")));
                    textInfo.setSHADOW_PROG(bundle.getInt("shadowProg", 5));
                    textInfo.setBG_COLOR(bundle.getInt("bgColor", 0));
                    textInfo.setBG_DRAWABLE(bundle.getString("bgDrawable", "0"));
                    textInfo.setBG_ALPHA(bundle.getInt("bgAlpha", 255));
                    textInfo.setROTATION(bundle.getFloat("rotation", 0.0f));
                    textInfo.setFIELD_TWO(bundle.getString("field_two", ""));
                    Log.e("double tab 22", "" + bundle.getFloat("X", 0.0f) + " ," + bundle.getFloat("Y", 0.0f));
                    this.fontName = bundle.getString("fontName", "");
                    this.tColor = bundle.getInt("tColor", Color.parseColor("#4149b6"));
                    this.shadowColor = bundle.getInt("shadowColor", Color.parseColor("#7641b6"));
                    this.shadowProg = bundle.getInt("shadowProg", 0);
                    this.tAlpha = bundle.getInt("tAlpha", 100);
                    this.bgDrawable = bundle.getString("bgDrawable", "0");
                    this.bgAlpha = bundle.getInt("bgAlpha", 255);
                    this.rotation = bundle.getFloat("rotation", 0.0f);
                    this.bgColor = bundle.getInt("bgColor", 0);
                    if (this.editMode) {
                        RelativeLayout relativeLayout = txtStkrRel;
                        ((AutofitTextRel) relativeLayout.getChildAt(relativeLayout.getChildCount() - 1)).setTextInfo(textInfo, false);
                        RelativeLayout relativeLayout2 = txtStkrRel;
                        ((AutofitTextRel) relativeLayout2.getChildAt(relativeLayout2.getChildCount() - 1)).setBorderVisibility(true);
                        this.editMode = false;
                    } else {
                        this.verticalSeekBar.setProgress(100);
                        this.seekbarShadow.setProgress(0);
                        this.seekBar3.setProgress(255);
                        AutofitTextRel autofitTextRel = new AutofitTextRel(this);
                        txtStkrRel.addView(autofitTextRel);
                        autofitTextRel.setTextInfo(textInfo, false);
                        autofitTextRel.setId(ViewIdGenerator.generateViewId());
                        autofitTextRel.setOnTouchCallbackListener(this);
                        autofitTextRel.setBorderVisibility(true);
                    }
                    if (this.layTextMain.getVisibility() == GONE) {
                        this.layTextMain.setVisibility(View.VISIBLE);
                        this.layTextMain.startAnimation(this.animSlideUp);
                    }
                }
                if (i3 == 1020) {

                    int childCount = txtStkrRel.getChildCount();
                    int i5 = intent.getIntExtra("id", 0);
                    for (int i6 = 0; i6 < childCount; i6++) {
                        View childAt = txtStkrRel.getChildAt(i6);
                        if ((childAt instanceof StickerView) && childAt.getId() == i5) {

                            ((StickerView) childAt).setStrPath(intent.getStringExtra(CropActivity.BG_IMAGE_PATH));

                            saveBitmapUndu();
                        }
                    }
                }
                if (i3 == SELECT_PICTURE_FROM_GALLERY) {

                    isChangeSticker = false;
                    isBackground = false;
                    try {
                        Uri fromFile = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                        UCrop.Options options2 = new UCrop.Options();
                        options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
                        options2.setToolbarColor(getResources().getColor(R.color.white));
                        options2.setToolbarWidgetColor(getResources().getColor(R.color.color_add_btn));
                        options2.setFreeStyleCropEnabled(true);

                        UCrop.of(intent.getData(), fromFile).withOptions(options2).start(this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i3 == SELECT_PICTURE_FROM_GALLERY_FOR_STICKER_CHANGE) {
                    try {
                        isChangeSticker = true;
                        isBackground = false;
                        Uri fromFile = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                        UCrop.Options options2 = new UCrop.Options();
                        options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
                        options2.setToolbarColor(getResources().getColor(R.color.white));
                        options2.setToolbarWidgetColor(getResources().getColor(R.color.color_add_btn));
                        options2.setFreeStyleCropEnabled(true);
                        options2.withAspectRatio(currentStickerView.getWidth(), currentStickerView.getHeight());
                        UCrop.of(intent.getData(), fromFile).withOptions(options2).start(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i3 == SELECT_PICTURE_FROM_CAMERA) {

                    isChangeSticker = false;
                    isBackground = false;
                    try {
                        Uri fromFile2 = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".jpg"));
                        UCrop.Options options3 = new UCrop.Options();
                        options3.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        options3.setCompressionQuality(80);
                        options3.setToolbarColor(getResources().getColor(R.color.white));
                        options3.setToolbarWidgetColor(getResources().getColor(R.color.color_add_btn));
                        options3.setFreeStyleCropEnabled(true);

                        if (file != null) {

                            UCrop.of(Uri.fromFile(file), fromFile2).withOptions(options3).start(this);

                        }


                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (i3 == SELECT_PICTURE_FROM_GALLERY_BACKGROUND) {


                    try {
                        isBackground = true;
                        isChangeSticker = false;
                        setBackgroundData(intent.getData());

                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                if (i4 == -1 && i3 == 69) {

                    if (isGreeting) {
                        colorType = "";

                        ImageView imageView = new ImageView(context);

                        String path = MyUtils.getPathFromURI(context, UCrop.getOutput(intent));

                        Glide.with(context).load(path).into(imageView);

                        greetingZoomLay.setVisibility(View.VISIBLE);
                        greetingZoomLay.addView(imageView);

                        findViewById(R.id.greeting_btn).setVisibility(View.GONE);

                        isGreeting = false;

                    } else {


                        if (this.isBackground) {

                            try {

                                setBackgroundData(UCrop.getOutput(intent));
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }


                        } else {
                            this.colorType = "colored";

                            new ImageCropperFragment(currentStickerView != null ? currentStickerView.getId() : 0, MyUtils.getPathFromURI(context, UCrop.getOutput(intent)), (id, out) -> {
                                if (isChangeSticker) {
                                    int childCount = txtStkrRel.getChildCount();
                                    int i5 = id;
                                    for (int i6 = 0; i6 < childCount; i6++) {
                                        View childAt = txtStkrRel.getChildAt(i6);
                                        if ((childAt instanceof StickerView) && childAt.getId() == i5) {
                                            ((StickerView) childAt).setStrPath(out);
                                            saveBitmapUndu();
                                        }
                                    }
                                } else {
                                    ThumbnailActivity.this.colorType = "colored";
                                    addSticker("", out, null);
                                }
                            }).show(getSupportFragmentManager(), "");
                        }
                    }

                } else if (i4 == 96) {
                    UCrop.getError(intent);
                }
                if (i3 == 4) {
                    openCustomActivity(bundle, intent2);
                    return;
                }
                return;
            }
            new AlertDialog.Builder(this, 16974126).setMessage(Constant.getSpannableString(this, Typeface.DEFAULT, R.string.picUpImg)).setPositiveButton(Constant.getSpannableString(this, Typeface.DEFAULT, R.string.ok), (dialogInterface, i1) -> dialogInterface.cancel()).create().show();
        } else if (i3 == TEXT_ACTIVITY) {
            this.editMode = false;
        }
    }

    private void setBackgroundData(Uri uri) throws IOException {
        this.layBackground.setVisibility(View.GONE);
        this.imgOK.setVisibility(View.VISIBLE);
        this.btnErase.setVisibility(View.VISIBLE);
        if (this.layBackground.getVisibility() == View.VISIBLE) {
            this.layBackground.startAnimation(this.animSlideDown);
            this.layBackground.setVisibility(View.GONE);
        }
        this.screenWidth = (float) background_img.getWidth();
        this.screenHeight = (float) background_img.getHeight();
        Constant.bitmap = ImageUtils.scaleCenterCrop(Constant.getBitmapFromUri(this, uri, this.screenWidth, this.screenHeight), (int) this.screenHeight, (int) this.screenWidth);
        this.showtailsSeek = false;
        this.position = "1";
        this.profile = "Temp_Path";
        this.hex = "";
        setImageBitmapAndResizeLayout(Constant.bitmap, "nonCreated");
    }


    private int gcd(int i, int i2) {
        return i2 == 0 ? i : gcd(i2, i % i2);
    }

    public void openCustomActivity(Bundle bundle, Intent intent) {
        Bundle extras = intent.getExtras();
        this.profile = "no";
        if (this.profile.equals("no")) {
            this.showtailsSeek = false;
            this.position = "1";
            this.profile = "Temp_Path";
            this.hex = "";
            setImageBitmapAndResizeLayout(ImageUtils.resizeBitmap(Constant.bitmap, (int) this.screenWidth, (int) this.screenHeight), "nonCreated");
            return;
        }
        if (this.profile.equals("Texture")) {
            this.showtailsSeek = true;
            this.layHandletails.setVisibility(View.VISIBLE);
        } else {
            this.showtailsSeek = false;
            this.layHandletails.setVisibility(GONE);
        }
        this.hex = extras.getString("color");
        drawBackgroundImageFromDp(this.ratio, this.profile, "nonCreated");
    }

    public void onGalleryButtonClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), SELECT_PICTURE_FROM_GALLERY);
    }

    public void onCameraButtonClick() {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File tmp = new File(MyUtils.getFolderPath(context, ".bgTemp"));
        this.file = new File(tmp.getAbsolutePath(), "temp.jpg");
        intent.putExtra("output", FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", this.file));
        startActivityForResult(intent, SELECT_PICTURE_FROM_CAMERA);
    }

    public void onGalleryBackground() {
        isBackground = true;
        isChangeSticker = false;

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), SELECT_PICTURE_FROM_GALLERY_BACKGROUND);
    }

    private void colorPickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }

            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                ThumbnailActivity.this.updateBackgroundColor(i);
            }
        }).show();
    }


    public void updateBackgroundColor(int i) {
        this.layBackground.setVisibility(GONE);
        this.imgOK.setVisibility(View.VISIBLE);
        this.btnErase.setVisibility(View.VISIBLE);
        if (this.layBackground.getVisibility() == View.VISIBLE) {
            this.layBackground.startAnimation(this.animSlideDown);
            this.layBackground.setVisibility(GONE);
        }
        Bitmap createBitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
        createBitmap.eraseColor(i);
        Log.e(TAG, "updateColor: ");
        try {
            this.screenWidth = (float) background_img.getWidth();
            this.screenHeight = (float) background_img.getHeight();
            Constant.bitmap = ImageUtils.scaleCenterCrop(createBitmap, (int) this.screenHeight, (int) this.screenWidth);
            this.showtailsSeek = false;
            this.position = "1";
            this.profile = "Temp_Path";
            this.hex = "";
            setImageBitmapAndResizeLayout(Constant.bitmap, "nonCreated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        } else if (this.mViewAllFrame.getVisibility() == View.VISIBLE) {
            closeViewAll();
        } else if (this.layTextMain.getVisibility() == View.VISIBLE) {
            if (this.layTextedit.getVisibility() == View.VISIBLE) {
                hideTextResContainer();
                removeScroll();
                return;
            }
            showBackDialog();
        } else if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                hideResContainer();
                removeScroll();
                return;
            }
            showBackDialog();
        } else if (this.laySticker.getVisibility() == View.VISIBLE) {
            this.laySticker.setVisibility(GONE);
            laySticker.startAnimation(animSlideDown);


        } else if (this.layVideoFilter.getVisibility() == View.VISIBLE) {
            this.layVideoFilter.setVisibility(GONE);
            layVideoFilter.startAnimation(animSlideDown);

        } else if (this.layVideoAnimation.getVisibility() == View.VISIBLE) {
            this.layVideoAnimation.setVisibility(GONE);
            layVideoAnimation.startAnimation(animSlideDown);

        } else if (this.layMusic.getVisibility() == View.VISIBLE) {
            this.layMusic.setVisibility(GONE);
            layMusic.startAnimation(animSlideDown);
            this.imgOK.setVisibility(View.VISIBLE);
            this.btnErase.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);

            stopMusic();


        } else if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
            this.seekbarContainer.startAnimation(this.animSlideDown);
            this.seekbarContainer.setVisibility(GONE);
        } else if (this.layEffects.getVisibility() == View.VISIBLE) {
            this.layEffects.startAnimation(this.animSlideDown);
            this.layEffects.setVisibility(GONE);
        } else if (this.lay_frames.getVisibility() == View.VISIBLE) {
            this.lay_frames.startAnimation(this.animSlideDown);
            this.lay_frames.setVisibility(GONE);
        } else if (this.layBackground.getVisibility() == View.VISIBLE) {
            this.layBackground.startAnimation(this.animSlideDown);
            this.imgOK.setVisibility(View.VISIBLE);
            this.btnErase.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
            this.layBackground.setVisibility(GONE);
        } else if (lay_container.getVisibility() == View.VISIBLE) {
            lay_container.animate().translationX((float) (-lay_container.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
            new Handler().postDelayed(() -> {
                ThumbnailActivity.lay_container.setVisibility(GONE);
                ThumbnailActivity.btn_layControls.setVisibility(View.VISIBLE);
            }, 200);
        } else {
            showBackDialog();
        }
    }

    private void showBackDialog() {

        dialog = new Dialog(this);
        this.dialog.requestWindowFeature(1);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.discard_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.findViewById(R.id.iv_close).setVisibility(VISIBLE);
        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());

        LinearLayout button = dialog.findViewById(R.id.btn_yes);

        LinearLayout button2 = dialog.findViewById(R.id.btn_no);

        button.setOnClickListener(view -> {

            musicPath = "";
            movieImageList.clear();
            freeMemory();
            dialog.dismiss();

            ThumbnailActivity.this.finish();

        });

        button2.setOnClickListener(view -> {
            removeScroll();
            hideSlideBar();
            layEffects.setVisibility(View.GONE);
            layStkrMain.setVisibility(View.GONE);
            layBackground.setVisibility(View.GONE);
            layTextMain.setVisibility(View.GONE);
            laySticker.setVisibility(View.GONE);
            removeImageViewControll();
            if (seekbarContainer.getVisibility() == View.VISIBLE) {
                seekbarContainer.startAnimation(animSlideDown);
                seekbarContainer.setVisibility(View.GONE);
            }
            if (layTextMain.getVisibility() == View.VISIBLE) {
                layTextMain.startAnimation(animSlideDown);
                layTextMain.setVisibility(View.GONE);
            }
            if (layStkrMain.getVisibility() == View.VISIBLE) {
                layStkrMain.startAnimation(animSlideDown);
                layStkrMain.setVisibility(View.GONE);
            }

            dialog.dismiss();

        });
        dialog.show();
    }


    private void showPicImageDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_image_dialog);

        dialog.findViewById(R.id.btnCancel).setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnGallery).setOnClickListener(view -> {
            ThumbnailActivity.this.onGalleryButtonClick();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCamera).setOnClickListener(view -> {
            ThumbnailActivity.this.onCameraButtonClick();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void ongetSticker() {
        this.colorType = "colored";
        addSticker("", "", Constant.bitmap);
    }

    public void onColor(int i, String str, int i2) {
        if (i != 0) {
            int childCount = txtStkrRel.getChildCount();
            int i3 = 0;
            if (str.equals("txtShadow")) {
                while (i3 < childCount) {
                    View childAt = txtStkrRel.getChildAt(i3);
                    if (childAt instanceof AutofitTextRel) {
                        ((AutofitTextRel) txtStkrRel.getChildAt(i2)).setBorderVisibility(true);
                        AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                        if (autofitTextRel.getBorderVisibility()) {
                            this.shadowColor = i;
                            autofitTextRel.setTextShadowColor(i);
                        }
                    }
                    i3++;
                }
            } else if (str.equals("txtBg")) {
                while (i3 < childCount) {
                    View childAt2 = txtStkrRel.getChildAt(i3);
                    if (childAt2 instanceof AutofitTextRel) {
                        ((AutofitTextRel) txtStkrRel.getChildAt(i2)).setBorderVisibility(true);
                        AutofitTextRel autofitTextRel2 = (AutofitTextRel) childAt2;
                        if (autofitTextRel2.getBorderVisibility()) {
                            this.bgColor = i;
                            this.bgDrawable = "0";
                            autofitTextRel2.setBgColor(i);
                            autofitTextRel2.setBgAlpha(this.seekBar3.getProgress());
                        }
                    }
                    i3++;
                }
            } else {
                View childAt3 = txtStkrRel.getChildAt(i2);
                if (childAt3 instanceof AutofitTextRel) {
                    ((AutofitTextRel) txtStkrRel.getChildAt(i2)).setBorderVisibility(true);
                    AutofitTextRel autofitTextRel3 = (AutofitTextRel) childAt3;
                    if (autofitTextRel3.getBorderVisibility()) {
                        this.tColor = i;
                        this.textColorSet = i;
                        autofitTextRel3.setTextColor(i);
                    }
                }
                if (childAt3 instanceof StickerView) {
                    ((StickerView) txtStkrRel.getChildAt(i2)).setBorderVisibility(true);
                    StickerView stickerView = (StickerView) childAt3;
                    if (stickerView.getBorderVisbilty()) {
                        this.stkrColorSet = i;
                        stickerView.setColor(i);
                    }
                }
            }
        } else {
            removeScroll();
            if (this.layTextMain.getVisibility() == View.VISIBLE) {
                this.layTextMain.startAnimation(this.animSlideDown);
                this.layTextMain.setVisibility(View.GONE);
            }
            if (this.layStkrMain.getVisibility() == View.VISIBLE) {
                this.layStkrMain.startAnimation(this.animSlideDown);
                this.layStkrMain.setVisibility(View.GONE);
            }
        }
    }

    public void errorDialogTempInfo() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.error_dialog);
        ((TextView) dialog.findViewById(R.id.txterorr)).setTypeface(this.ttfHeader);
        ((TextView) dialog.findViewById(R.id.txt)).setTypeface(this.ttf);
        Button button = dialog.findViewById(R.id.btn_ok_e);
        button.setTypeface(this.ttf);
        button.setOnClickListener(view -> ThumbnailActivity.this.finish());
        Button button2 = dialog.findViewById(R.id.btn_continue);
        button2.setTypeface(this.ttf);
        button2.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public Bitmap gaussinBlur(Activity activity2, Bitmap bitmap2) {
        try {
            GPUImage gPUImage = new GPUImage(activity2);
            GPUImageGaussianBlurFilter gPUImageGaussianBlurFilter = new GPUImageGaussianBlurFilter();
            gPUImage.setFilter(gPUImageGaussianBlurFilter);
            new FilterAdjuster(gPUImageGaussianBlurFilter).adjust(150);
            gPUImage.requestRender();
            return gPUImage.getBitmapWithFilterApplied(bitmap2);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiver);
        freeMemory();
        stopMusic();
    }

    public void freeMemory() {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.bitmap = null;
        }
        Bitmap bitmap3 = imgBtmap;
        if (bitmap3 != null) {
            bitmap3.recycle();
            imgBtmap = null;
        }
        Bitmap bitmap4 = withoutWatermark;
        if (bitmap4 != null) {
            bitmap4.recycle();
            withoutWatermark = null;
        }
        Bitmap bitmap5 = btmSticker;
        if (bitmap5 != null) {
            bitmap5.recycle();
            btmSticker = null;
        }
        try {
            new Thread(() -> {
                runOnUiThread(() -> Glide.get(context).clearMemory());
                try {
                    Glide.get(ThumbnailActivity.this).clearDiskCache();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public void onResume() {
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        registerReceiver(broadcastReceiver2, new IntentFilter(getPackageName() + ".USER_ACTION"));
        super.onResume();

        if (musicPlayer != null && musicPlayer.isPlaying()) {

            musicPlayer.setPlayWhenReady(true);

        }

        if (isMovie) {
            demoPresenter.onResume();
        }

        if (preferenceManager != null && preferenceManager.getBoolean(IS_SUBSCRIBE)) {

            binding.ivWatermark.setVisibility(GONE);

        } else {

            binding.ivWatermark.setImageDrawable(getResources().getDrawable(R.drawable.watermark_remove));

        }


    }

    private void fackClick() {
        this.layEffects.setOnClickListener(view -> {
        });
        this.layTextedit.setOnClickListener(view -> {
        });
        this.seekbarContainer.setOnClickListener(view -> {
        });
        this.seekbarHandle.setOnClickListener(view -> {
        });
        this.seekLetterSpacing.setOnClickListener(view -> {
        });
        this.seekLineSpacing.setOnClickListener(view -> {
        });
        this.verticalSeekBar.setOnClickListener(view -> {
        });
        this.seekbarShadow.setOnClickListener(view -> {
        });
        this.seekShadowBlur.setOnClickListener(view -> {
        });
        this.seekOutlineSize.setOnClickListener(view -> {
        });
        this.seekBar3.setOnClickListener(view -> {
        });
        this.seek_blur.setOnClickListener(view -> {
        });
        this.seek.setOnClickListener(view -> {
        });
    }

    public void selectControl1() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
    }

    public void selectControl2() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
    }

    public void selectControl3() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
    }

    public void selectControl4() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
    }

    public void selectControl5() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
    }

    public void selectControl6() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
    }

    public void selectControl7() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
    }

    public void selectControl8() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.tabtextcolor_selected));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.tabtextcolor_normal));
    }

    @Override
    public void selectedImage(int drawable) {
        setBitmapOverlay(drawable);

    }

    public void saveBitmapUndu() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tempID++;
                    TemplateInfo templateInfo = new TemplateInfo();
                    templateInfo.setTHUMB_URI("");
                    templateInfo.setTEMPLATE_ID(tempID);
                    templateInfo.setFRAME_NAME(frameName);
                    templateInfo.setRATIO(ratio);
                    templateInfo.setPROFILE_TYPE(profile);
                    templateInfo.setSEEK_VALUE(String.valueOf(seekValue));
                    templateInfo.setTYPE("USER");
                    templateInfo.setTEMP_PATH(tempPath);
                    templateInfo.setTEMPCOLOR(hex);
                    templateInfo.setOVERLAY_NAME(overlay_Name);
                    templateInfo.setOVERLAY_OPACITY(seek.getProgress());
                    templateInfo.setOVERLAY_BLUR(seek_blur.getProgress());
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    int childCount = txtStkrRel.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childAt = txtStkrRel.getChildAt(i);
                        if (childAt instanceof AutofitTextRel) {
                            TextInfo textInfo = ((AutofitTextRel) childAt).getTextInfo();
                            textInfo.setTEMPLATE_ID(templateId);
                            textInfo.setORDER(i);
                            textInfo.setTYPE("TEXT");
                            arrayList.add(textInfo);
                        } else {
                            ElementInfoPoster componentInfo = ((StickerView) txtStkrRel.getChildAt(i)).getComponentInfo();
                            componentInfo.setTEMPLATE_ID(templateId);
                            componentInfo.setTYPE("STICKER");
                            componentInfo.setORDER(i);
                            arrayList2.add(componentInfo);
                        }
                    }
                    templateInfo.setTextInfoArrayList(arrayList);
                    templateInfo.setElementInfoArrayList(arrayList2);
                    templateListUR.add(templateInfo);

                } catch (Exception e) {
                    Log.i("testing", "Exception " + e.getMessage());
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // Update the UI on the main thread when the background work is done
                iconVisibility();
            }
        }.execute();

/*
        try {

            iconVisibility();
        } catch (Exception e) {
            Log.i("testing", "Exception " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable th) {
        }*/
    }

    public void loadSaveUnduRedo(TemplateInfo templateInfo) {
        this.templateId = templateInfo.getTEMPLATE_ID();
        this.frameName = templateInfo.getFRAME_NAME();
        this.tempPath = templateInfo.getTEMP_PATH();
        this.ratio = templateInfo.getRATIO();
        this.profile = templateInfo.getPROFILE_TYPE();
        this.tempID = templateInfo.getTEMPLATE_ID();
        String seek_value = templateInfo.getSEEK_VALUE();
        this.hex = templateInfo.getTEMPCOLOR();
        this.overlay_Name = templateInfo.getOVERLAY_NAME();
        this.overlay_opacty = templateInfo.getOVERLAY_OPACITY();
        this.overlay_blur = templateInfo.getOVERLAY_BLUR();
        this.seekValue = Integer.parseInt(seek_value);
        this.textInfosUR = templateInfo.getTextInfoArrayList();
        this.elementInfoPosters = templateInfo.getElementInfoArrayList();
        this.progressBarUndo.setVisibility(View.VISIBLE);
        this.btnRedo.setVisibility(View.GONE);
        this.btnUndo.setVisibility(View.GONE);
        LoadStickersAsyncUR loadStickersAsyncUR = new LoadStickersAsyncUR();
        loadStickersAsyncUR.execute("" + this.tempID);
    }

    public void undo() {
        if (this.layTextMain.getVisibility() == View.VISIBLE) {
            this.layTextMain.setVisibility(View.GONE);
        }
        if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            this.layStkrMain.setVisibility(View.GONE);
        }
        if (this.templateListUR.size() > 2) {
            this.btnUndo.setImageResource(R.drawable.undo_disable2);
        } else {
            this.btnUndo.setImageResource(R.drawable.undo_disable);
        }

        if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
            this.seekbarContainer.setVisibility(View.GONE);
        }
        if (this.templateListUR.size() > 1) {
            ArrayList<TemplateInfo> arrayList = this.templateListUR;
            loadSaveUnduRedo(arrayList.get(arrayList.size() - 2));
            ArrayList<TemplateInfo> arrayList2 = this.templateListRU;
            ArrayList<TemplateInfo> arrayList3 = this.templateListUR;
            arrayList2.add(arrayList3.get(arrayList3.size() - 1));
            ArrayList<TemplateInfo> arrayList4 = this.templateListUR;
            arrayList4.remove(arrayList4.get(arrayList4.size() - 1));
        }
        iconVisibility();
    }

    public void redo() {
        if (this.layTextMain.getVisibility() == View.VISIBLE) {
            this.layTextMain.setVisibility(View.GONE);
        }
        if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            this.layStkrMain.setVisibility(View.GONE);
        }
        if (this.templateListRU.size() > 1) {
            this.btnRedo.setImageResource(R.drawable.redo_disable2);
        } else {
            this.btnRedo.setImageResource(R.drawable.redo_disable);
        }

        if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
            this.seekbarContainer.setVisibility(View.GONE);
        }

        if (this.templateListRU.size() > 0) {
            ArrayList<TemplateInfo> arrayList = this.templateListRU;
            loadSaveUnduRedo(arrayList.get(arrayList.size() - 1));
            ArrayList<TemplateInfo> arrayList2 = this.templateListUR;
            ArrayList<TemplateInfo> arrayList3 = this.templateListRU;
            arrayList2.add(arrayList3.get(arrayList3.size() - 1));
            ArrayList<TemplateInfo> arrayList4 = this.templateListRU;
            arrayList4.remove(arrayList4.get(arrayList4.size() - 1));
        }
        iconVisibility();
    }

    public void iconVisibility() {
        if (this.templateListUR.size() > 1) {
            this.btnUndo.setImageResource(R.drawable.undo_disable);
        } else {
            this.btnUndo.setImageResource(R.drawable.redo_disable2);
        }
        if (this.templateListRU.size() > 0) {
            this.btnRedo.setImageResource(R.drawable.redo_disable);
        } else {
            this.btnRedo.setImageResource(R.drawable.redo_disable2);
        }
    }

    @Override
    public GLTextureView getGLView() {
        return glTextureView;
    }

    @Override
    public Context getActivity() {
        return context;
    }


    interface FrameRemovalCallback {
        void onFrameRemovalComplete();
    }

    public class RenderInterface implements RenderEngine.RenderEngienInterface {


        public ProgressBar progressBar;

        public RenderInterface(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }


        public void onProgress(float f) {
            int progress = (int) f;

            this.progressBar.setProgress(progress);

            Log.i("POIU", "onProgressChange: " + progress);

        }

        public void onExporteed(File file) {

            progressBar.setProgress(50);
            animatedFramePath = file.getAbsolutePath();
            isExportingWithFrame = true;
            preferenceManager.setBoolean(Constant.BUSINESS_UPDATED, false);
        }

    }


    public class BlurOperationTwoAsync extends AsyncTask<String, Void, String> {
        ImageView background_blur;
        Bitmap btmp;
        Activity context;

        public BlurOperationTwoAsync(ThumbnailActivity thumbnailActivity, Bitmap bitmap, ImageView imageView) {
            this.context = thumbnailActivity;
            this.btmp = bitmap;
            this.background_blur = imageView;
        }

        @Override
        public void onPreExecute() {
        }

        public String doInBackground(String... strArr) {
            this.btmp = gaussinBlur(this.context, this.btmp);
            return "yes";
        }

        @Override
        public void onPostExecute(String str) {


               /* if (!(txtStkrRel.getChildCount() > 0)) {
                    if (isTamplate != 0) {
                        txtStkrRel.removeAllViews();
                        LoadStickersAsync loadStickersAsync2 = new LoadStickersAsync();
                        loadStickersAsync2.execute();
                    }
                }*/


            Bitmap bitmap = this.btmp;
            if (bitmap != null) {
                this.background_blur.setImageBitmap(bitmap);
            }
            ThumbnailActivity.txtStkrRel.removeAllViews();
            if (ThumbnailActivity.this.tempPath.equals("")) {
                LoadStickersAsync loadStickersAsync = new LoadStickersAsync();
                loadStickersAsync.execute("" + ThumbnailActivity.this.templateId);
                return;
            }

            File file = new StorageUtils(context).getPackageStorageDir(getString(R.string.sticker_directory));


            if (file.exists()) {
                try {
                    if (file.listFiles().length >= 7) {
                        LoadStickersAsync loadStickersAsync2 = new LoadStickersAsync();
                        loadStickersAsync2.execute("" + ThumbnailActivity.this.templateId);
                    } else if (new File(ThumbnailActivity.this.tempPath).exists()) {
                        LoadStickersAsync loadStickersAsync3 = new LoadStickersAsync();
                        loadStickersAsync3.execute("" + ThumbnailActivity.this.templateId);
                    } else {
                        LoadStickersAsync loadStickersAsync4 = new LoadStickersAsync();
                        loadStickersAsync4.execute("" + ThumbnailActivity.this.templateId);
                    }
                } catch (NullPointerException e) {
                    LoadStickersAsync loadStickersAsync5 = new LoadStickersAsync();
                    loadStickersAsync5.execute("" + ThumbnailActivity.this.templateId);
                    e.printStackTrace();
                }
            } else if (new File(ThumbnailActivity.this.tempPath).exists()) {
                LoadStickersAsync loadStickersAsync6 = new LoadStickersAsync();
                loadStickersAsync6.execute("" + ThumbnailActivity.this.templateId);
            } else {
                LoadStickersAsync loadStickersAsync7 = new LoadStickersAsync();
                loadStickersAsync7.execute("" + ThumbnailActivity.this.templateId);
            }
        }
    }

    private class LoadStickersAsync extends AsyncTask<String, String, Boolean> {
        private LoadStickersAsync() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }


        public Boolean doInBackground(String... strArr) {
            ArrayList<ElementInfoPoster> stickerArrayList;
            ArrayList<TextInfo> textInfoArrayList;
            String str;

            textInfoArrayList = new ArrayList<>();
            stickerArrayList = new ArrayList<>();


            for (int i = 0; i < ThumbnailActivity.this.stickerInfoArrayList.size(); i++) {
                ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
                int newWidht = thumbnailActivity.getNewWidht(Float.valueOf(thumbnailActivity.stickerInfoArrayList.get(i).getSt_x_pos()).floatValue(), Float.valueOf(ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_width()).floatValue());
                ThumbnailActivity thumbnailActivity2 = ThumbnailActivity.this;
                int newHeight = thumbnailActivity2.getNewHeight(Float.valueOf(thumbnailActivity2.stickerInfoArrayList.get(i).getSt_y_pos()).floatValue(), Float.valueOf(ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_height()).floatValue());
                int i2 = newWidht < 10 ? 20 : (newWidht <= 10 || newWidht > 20) ? newWidht : 35;
                int i3 = newHeight < 10 ? 20 : (newHeight <= 10 || newHeight > 20) ? newHeight : 35;
                if (ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_field2() != null) {
                    str = ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_field2();
                } else {
                    str = "";
                }
                float parseInt = (ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_rotation() == null || ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_rotation().equals("")) ? 0.0f : (float) Integer.parseInt(ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_rotation());
                int i4 = ThumbnailActivity.this.postId;
                ThumbnailActivity thumbnailActivity3 = ThumbnailActivity.this;
                float xpos = thumbnailActivity3.getXpos(Float.valueOf(thumbnailActivity3.stickerInfoArrayList.get(i).getSt_x_pos()).floatValue());
                ThumbnailActivity thumbnailActivity4 = ThumbnailActivity.this;

                Log.d("ediablearyya : ", "" + ThumbnailActivity.this.stickerInfoArrayList.get(i).getEditable());


                stickerArrayList.add(new ElementInfoPoster(i4, ThumbnailActivity.this.stickerInfoArrayList.get(i).getEditable(), xpos, thumbnailActivity4.getYpos(Float.valueOf(thumbnailActivity4.stickerInfoArrayList.get(i).getSt_y_pos()).floatValue()), i2, i3, parseInt, 0.0f, "", "STICKER", Integer.parseInt(ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_order()), 0, 255, 0, 0, 0, 0, ThumbnailActivity.this.stickerInfoArrayList.get(i).getSt_image(), "colored", 1, 0, str, "", "", null, null));
            }
            for (int i5 = 0; i5 < ThumbnailActivity.this.textInfoArrayList.size(); i5++) {
                int i6 = ThumbnailActivity.this.postId;


                String text = ThumbnailActivity.this.textInfoArrayList.get(i5).getText();


                String font_family = ThumbnailActivity.this.textInfoArrayList.get(i5).getFont_family();


                int parseColor = Color.parseColor(ThumbnailActivity.this.textInfoArrayList.get(i5).getTxt_color());
                ThumbnailActivity thumbnailActivity5 = ThumbnailActivity.this;
                float xpos2 = thumbnailActivity5.getXpos(Float.valueOf(thumbnailActivity5.textInfoArrayList.get(i5).getTxt_x_pos()).floatValue());
                ThumbnailActivity thumbnailActivity6 = ThumbnailActivity.this;
                float ypos = thumbnailActivity6.getYpos(Float.valueOf(thumbnailActivity6.textInfoArrayList.get(i5).getTxt_y_pos()).floatValue());
                ThumbnailActivity thumbnailActivity7 = ThumbnailActivity.this;

                int newWidht2 = thumbnailActivity7.getNewWidht(Float.valueOf(thumbnailActivity7.textInfoArrayList.get(i5).getTxt_x_pos()).floatValue(), Float.valueOf(ThumbnailActivity.this.textInfoArrayList.get(i5).getTxt_width()).floatValue());

                ThumbnailActivity thumbnailActivity8 = ThumbnailActivity.this;

                textInfoArrayList.add(new TextInfo(i6, text, font_family, parseColor, 100, ViewCompat.MEASURED_STATE_MASK, 0, "0", ViewCompat.MEASURED_STATE_MASK, 0, xpos2, ypos, newWidht2, thumbnailActivity8.getNewHeightText(Float.valueOf(thumbnailActivity8.textInfoArrayList.get(i5).getTxt_y_pos()).floatValue(), Float.valueOf(ThumbnailActivity.this.textInfoArrayList.get(i5).getTxt_height()).floatValue()), Float.parseFloat(ThumbnailActivity.this.textInfoArrayList.get(i5).getTxt_rotation()), "TEXT", Integer.parseInt(ThumbnailActivity.this.textInfoArrayList.get(i5).getTxt_order()), 0, 0, 0, 0, 0, "", "", "", 0.0f, 0.0f, 0, 0));


            }


            ThumbnailActivity.this.txtShapeList = new HashMap<>();
            Iterator<TextInfo> it = textInfoArrayList.iterator();
            while (it.hasNext()) {
                TextInfo next = it.next();
                ThumbnailActivity.this.txtShapeList.put(Integer.valueOf(next.getORDER()), next);
            }
            Iterator<ElementInfoPoster> it2 = stickerArrayList.iterator();
            while (it2.hasNext()) {
                ElementInfoPoster next2 = it2.next();
                ThumbnailActivity.this.txtShapeList.put(Integer.valueOf(next2.getORDER()), next2);
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            ThumbnailActivity.this.dialogIs.dismiss();
            ArrayList arrayList = new ArrayList(ThumbnailActivity.this.txtShapeList.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();


            for (int i = 0; i < size; i++) {
                Object obj = ThumbnailActivity.this.txtShapeList.get(arrayList.get(i));
                if (obj instanceof ElementInfoPoster) {
                    ElementInfoPoster elementInfoPoster = (ElementInfoPoster) obj;


                    String stkr_path = elementInfoPoster.getSTKR_PATH();
                    if (stkr_path.equals("")) {
                        StickerView stickerView = new StickerView(ThumbnailActivity.this);
                        ThumbnailActivity.txtStkrRel.addView(stickerView);
                        stickerView.optimizeScreen(ThumbnailActivity.this.screenWidth, ThumbnailActivity.this.screenHeight);
                        stickerView.setViewWH((float) ThumbnailActivity.this.mainRel.getWidth(), (float) ThumbnailActivity.this.mainRel.getHeight());
                        stickerView.setComponentInfo(elementInfoPoster);
                        stickerView.setId(ViewIdGenerator.generateViewId());
                        stickerView.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                        stickerView.setOnTouchCallbackListener(ThumbnailActivity.this);
                        stickerView.setBorderVisibility(false);
                        ThumbnailActivity.this.sizeFull++;


                    } else {
                        File file = new StorageUtils(context).getPackageStorageDir(getString(R.string.sticker_directory));

                        if (!file.exists() && !file.mkdirs()) {
                            Log.d("", "Can't create directory to save image.");
                            ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
                            Toast.makeText(thumbnailActivity, thumbnailActivity.getResources().getString(R.string.create_dir_err), Toast.LENGTH_SHORT).show();
                            return;
                        } else if (new StorageUtils(context).getPackageStorageDir(getString(R.string.sticker_directory)).exists()) {
                            File file2 = new File(stkr_path);
                            if (file2.exists()) {
                                StickerView stickerView2 = new StickerView(ThumbnailActivity.this);
                                ThumbnailActivity.txtStkrRel.addView(stickerView2);
                                stickerView2.optimizeScreen(ThumbnailActivity.this.screenWidth, ThumbnailActivity.this.screenHeight);
                                stickerView2.setViewWH((float) ThumbnailActivity.this.mainRel.getWidth(), (float) ThumbnailActivity.this.mainRel.getHeight());
                                stickerView2.setComponentInfo(elementInfoPoster);
                                stickerView2.setId(ViewIdGenerator.generateViewId());
                                stickerView2.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                                stickerView2.setOnTouchCallbackListener(ThumbnailActivity.this);
                                stickerView2.setBorderVisibility(false);

                                if (elementInfoPoster.getEditable() == 0 || elementInfoPoster.getNAME().equals("bg") || elementInfoPoster.getNAME().equals("background")) {

                                    stickerView2.isMultiTouchEnabled = stickerView2.setDefaultTouchListener(false);

                                }

                                ThumbnailActivity.this.sizeFull++;
                            } else if (file2.getName().replace(".png", "").length() < 7) {
                                ThumbnailActivity thumbnailActivity2 = ThumbnailActivity.this;
                                thumbnailActivity2.dialogShow = false;
                                new SaveStickersAsync(obj).execute(stkr_path);
                            } else {
                                if (ThumbnailActivity.this.OneShow) {
                                    ThumbnailActivity thumbnailActivity3 = ThumbnailActivity.this;
                                    thumbnailActivity3.dialogShow = true;
                                    thumbnailActivity3.errorDialogTempInfo();
                                    ThumbnailActivity.this.OneShow = false;
                                }
                                ThumbnailActivity.this.sizeFull++;
                            }
                        } else {
                            File file3 = new File(stkr_path);
                            if (file3.exists()) {
                                StickerView stickerView3 = new StickerView(ThumbnailActivity.this);
                                ThumbnailActivity.txtStkrRel.addView(stickerView3);
                                stickerView3.optimizeScreen(ThumbnailActivity.this.screenWidth, ThumbnailActivity.this.screenHeight);
                                stickerView3.setViewWH((float) ThumbnailActivity.this.mainRel.getWidth(), (float) ThumbnailActivity.this.mainRel.getHeight());
                                stickerView3.setComponentInfo(elementInfoPoster);
                                stickerView3.setId(ViewIdGenerator.generateViewId());
                                stickerView3.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                                stickerView3.setOnTouchCallbackListener(ThumbnailActivity.this);
                                stickerView3.setBorderVisibility(false);
                                ThumbnailActivity.this.sizeFull++;
                            } else if (file3.getName().replace(".png", "").length() < 7) {
                                ThumbnailActivity thumbnailActivity4 = ThumbnailActivity.this;
                                thumbnailActivity4.dialogShow = false;
                                new SaveStickersAsync(obj).execute(stkr_path);
                            } else {
                                if (ThumbnailActivity.this.OneShow) {
                                    ThumbnailActivity thumbnailActivity5 = ThumbnailActivity.this;
                                    thumbnailActivity5.dialogShow = true;
                                    thumbnailActivity5.errorDialogTempInfo();
                                    ThumbnailActivity.this.OneShow = false;
                                }
                                ThumbnailActivity.this.sizeFull++;
                            }
                        }
                    }
                } else {


                    AutofitTextRel autofitTextRel = new AutofitTextRel(ThumbnailActivity.this);
                    ThumbnailActivity.txtStkrRel.addView(autofitTextRel);
                    TextInfo textInfo = (TextInfo) obj;


                    if (textInfo.getFONT_NAME() != null) {

                        ThumbnailActivity.this.setTextFonts(textInfo.getFONT_NAME());

                    }

                    autofitTextRel.setTextInfo(textInfo, false);
                    autofitTextRel.setId(ViewIdGenerator.generateViewId());
                    autofitTextRel.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                    autofitTextRel.setOnTouchCallbackListener(ThumbnailActivity.this);
                    autofitTextRel.setBorderVisibility(false);
                    ThumbnailActivity.this.fontName = textInfo.getFONT_NAME();
                    ThumbnailActivity.this.tColor = textInfo.getTEXT_COLOR();
                    ThumbnailActivity.this.shadowColor = textInfo.getSHADOW_COLOR();
                    ThumbnailActivity.this.shadowProg = textInfo.getSHADOW_PROG();
                    ThumbnailActivity.this.tAlpha = textInfo.getTEXT_ALPHA();
                    ThumbnailActivity.this.bgDrawable = textInfo.getBG_DRAWABLE();
                    ThumbnailActivity.this.bgAlpha = textInfo.getBG_ALPHA();
                    ThumbnailActivity.this.rotation = textInfo.getROTATION();
                    ThumbnailActivity.this.bgColor = textInfo.getBG_COLOR();
                    ThumbnailActivity.this.outerColor = textInfo.getOutLineColor();
                    ThumbnailActivity.this.outerSize = textInfo.getOutLineSize();
                    ThumbnailActivity.this.leftRightShadow = (int) textInfo.getLeftRighShadow();
                    ThumbnailActivity.this.topBottomShadow = (int) textInfo.getTopBottomShadow();
                    ThumbnailActivity.this.topBottomShadow = (int) textInfo.getTopBottomShadow();
                    ThumbnailActivity.this.sizeFull++;
                }
            }
            if (ThumbnailActivity.this.txtShapeList.size() == ThumbnailActivity.this.sizeFull && ThumbnailActivity.this.dialogShow) {
                try {
                    ThumbnailActivity.this.dialogIs.dismiss();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            if (!ThumbnailActivity.this.overlay_Name.equals("")) {
                ThumbnailActivity thumbnailActivity6 = ThumbnailActivity.this;
                thumbnailActivity6.setBitmapOverlay(getResources().getIdentifier(ThumbnailActivity.this.overlay_Name, "drawable", ThumbnailActivity.this.getPackageName()));
            }
            ThumbnailActivity.this.saveBitmapUndu();
        }
    }

    private class SaveStickersAsync extends AsyncTask<String, String, Boolean> {
        Object objk;
        String stkrPath;

        public SaveStickersAsync(Object obj) {
            this.objk = obj;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }


        public Boolean doInBackground(String... strArr) {
            String str = strArr[0];
            this.stkrPath = ((ElementInfoPoster) this.objk).getSTKR_PATH();
            try {
                Bitmap decodeResource = BitmapFactory.decodeResource(ThumbnailActivity.this.getResources(), ThumbnailActivity.this.getResources().getIdentifier(str, "drawable", ThumbnailActivity.this.getPackageName()));
                if (decodeResource != null) {
                    return Boolean.valueOf(Constant.saveBitmapObject(ThumbnailActivity.this, decodeResource, this.stkrPath));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }


        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            ThumbnailActivity.this.sizeFull++;
            if (ThumbnailActivity.this.txtShapeList.size() == ThumbnailActivity.this.sizeFull) {
                ThumbnailActivity.this.dialogShow = true;
            }
            if (bool.booleanValue()) {
                StickerView stickerView = new StickerView(ThumbnailActivity.this);
                ThumbnailActivity.txtStkrRel.addView(stickerView);
                stickerView.optimizeScreen(ThumbnailActivity.this.screenWidth, ThumbnailActivity.this.screenHeight);
                stickerView.setViewWH((float) ThumbnailActivity.this.mainRel.getWidth(), (float) ThumbnailActivity.this.mainRel.getHeight());
                stickerView.setComponentInfo((ElementInfoPoster) this.objk);
                stickerView.setId(ViewIdGenerator.generateViewId());
                stickerView.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                stickerView.setOnTouchCallbackListener(ThumbnailActivity.this);
                stickerView.setBorderVisibility(false);
            }
            if (ThumbnailActivity.this.dialogShow) {
                ThumbnailActivity.this.dialogIs.dismiss();
            }
        }
    }

    private class SavebackgrundAsync extends AsyncTask<String, String, Boolean> {
        private String crted;
        private String profile;
        private String ratio;

        private SavebackgrundAsync() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }


        public Boolean doInBackground(String... strArr) {
            String str = strArr[0];
            this.ratio = strArr[1];
            this.profile = strArr[2];
            this.crted = strArr[3];
            try {
                Bitmap decodeResource = BitmapFactory.decodeResource(ThumbnailActivity.this.getResources(), ThumbnailActivity.this.getResources().getIdentifier(str, "drawable", ThumbnailActivity.this.getPackageName()));
                if (decodeResource != null) {
                    return Boolean.valueOf(Constant.saveBitmapObject(ThumbnailActivity.this, decodeResource, ThumbnailActivity.this.tempPath));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool.booleanValue()) {
                try {
                    ThumbnailActivity.this.bitmapRatio(this.ratio, this.profile, ImageUtils.getResampleImageBitmap(Uri.parse(ThumbnailActivity.this.tempPath), ThumbnailActivity.this, (int) (ThumbnailActivity.this.screenWidth > ThumbnailActivity.this.screenHeight ? ThumbnailActivity.this.screenWidth : ThumbnailActivity.this.screenHeight)), this.crted);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ThumbnailActivity.txtStkrRel.removeAllViews();
            }
        }
    }

    private class LoadStickersAsyncUR extends AsyncTask<String, String, Boolean> {
        private LoadStickersAsyncUR() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }


        public Boolean doInBackground(String... strArr) {
            ThumbnailActivity.this.txtShapeList = new HashMap<>();
            Iterator<TextInfo> it = ThumbnailActivity.this.textInfosUR.iterator();
            while (it.hasNext()) {
                TextInfo next = it.next();
                ThumbnailActivity.this.txtShapeList.put(Integer.valueOf(next.getORDER()), next);
            }
            Iterator<ElementInfoPoster> it2 = ThumbnailActivity.this.elementInfoPosters.iterator();
            while (it2.hasNext()) {
                ElementInfoPoster next2 = it2.next();
                ThumbnailActivity.this.txtShapeList.put(Integer.valueOf(next2.getORDER()), next2);
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            ThumbnailActivity.txtStkrRel.removeAllViews();
            ArrayList arrayList = new ArrayList(ThumbnailActivity.this.txtShapeList.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                Object obj = ThumbnailActivity.this.txtShapeList.get(arrayList.get(i));
                if (obj instanceof ElementInfoPoster) {
                    ElementInfoPoster elementInfoPoster = (ElementInfoPoster) obj;
                    String stkr_path = elementInfoPoster.getSTKR_PATH();
                    if (stkr_path.equals("")) {
                        StickerView stickerView = new StickerView(ThumbnailActivity.this, true);
                        ThumbnailActivity.txtStkrRel.addView(stickerView);
                        stickerView.optimizeScreen(ThumbnailActivity.this.screenWidth, ThumbnailActivity.this.screenHeight);
                        stickerView.setViewWH((float) ThumbnailActivity.this.mainRel.getWidth(), (float) ThumbnailActivity.this.mainRel.getHeight());
                        stickerView.setComponentInfo(elementInfoPoster);
                        stickerView.setId(ViewIdGenerator.generateViewId());
                        stickerView.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                        stickerView.setOnTouchCallbackListener(ThumbnailActivity.this);
                        stickerView.setBorderVisibility(false);
                        ThumbnailActivity.this.sizeFull++;
                    } else {
                        File file = new StorageUtils(context).getPackageStorageDir(getString(R.string.invitaition_directory));

                        if (!file.exists() && !file.mkdirs()) {
                            Log.d("", "Can't create directory to save image.");
                            ThumbnailActivity thumbnailActivity = ThumbnailActivity.this;
                            Toast.makeText(thumbnailActivity, thumbnailActivity.getResources().getString(R.string.create_dir_err), Toast.LENGTH_SHORT).show();
                            return;
                        } else if (new StorageUtils(context).getPackageStorageDir(getString(R.string.invitaition_directory)).exists()) {
                            File file2 = new File(stkr_path);
                            if (file2.exists()) {
                                StickerView stickerView2 = new StickerView(ThumbnailActivity.this, true);
                                ThumbnailActivity.txtStkrRel.addView(stickerView2);
                                stickerView2.optimizeScreen(ThumbnailActivity.this.screenWidth, ThumbnailActivity.this.screenHeight);
                                stickerView2.setViewWH((float) ThumbnailActivity.this.mainRel.getWidth(), (float) ThumbnailActivity.this.mainRel.getHeight());
                                stickerView2.setComponentInfo(elementInfoPoster);
                                stickerView2.setId(ViewIdGenerator.generateViewId());
                                stickerView2.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                                stickerView2.setOnTouchCallbackListener(ThumbnailActivity.this);
                                stickerView2.setBorderVisibility(false);
                                ThumbnailActivity.this.sizeFull++;
                            } else if (file2.getName().replace(".png", "").length() < 7) {
                                ThumbnailActivity.this.dialogShow = false;
                            } else {
                                if (ThumbnailActivity.this.OneShow) {
                                    ThumbnailActivity thumbnailActivity2 = ThumbnailActivity.this;
                                    thumbnailActivity2.dialogShow = true;
                                    thumbnailActivity2.errorDialogTempInfo();
                                    ThumbnailActivity.this.OneShow = false;
                                }
                                ThumbnailActivity.this.sizeFull++;
                            }
                        } else {
                            File file3 = new File(stkr_path);
                            if (file3.exists()) {
                                StickerView stickerView3 = new StickerView(ThumbnailActivity.this, true);
                                ThumbnailActivity.txtStkrRel.addView(stickerView3);
                                stickerView3.optimizeScreen(ThumbnailActivity.this.screenWidth, ThumbnailActivity.this.screenHeight);
                                stickerView3.setViewWH((float) ThumbnailActivity.this.mainRel.getWidth(), (float) ThumbnailActivity.this.mainRel.getHeight());
                                stickerView3.setComponentInfo(elementInfoPoster);
                                stickerView3.setId(ViewIdGenerator.generateViewId());
                                stickerView3.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                                stickerView3.setOnTouchCallbackListener(ThumbnailActivity.this);
                                stickerView3.setBorderVisibility(false);
                                ThumbnailActivity.this.sizeFull++;
                            } else if (file3.getName().replace(".png", "").length() < 7) {
                                ThumbnailActivity.this.dialogShow = false;
                            } else {
                                if (ThumbnailActivity.this.OneShow) {
                                    ThumbnailActivity thumbnailActivity3 = ThumbnailActivity.this;
                                    thumbnailActivity3.dialogShow = true;
                                    thumbnailActivity3.errorDialogTempInfo();
                                    ThumbnailActivity.this.OneShow = false;
                                }
                                ThumbnailActivity.this.sizeFull++;
                            }
                        }
                    }
                } else {
                    AutofitTextRel autofitTextRel = new AutofitTextRel(ThumbnailActivity.this, true);
                    ThumbnailActivity.txtStkrRel.addView(autofitTextRel);
                    TextInfo textInfo = (TextInfo) obj;
                    autofitTextRel.setTextInfo(textInfo, false);
                    autofitTextRel.setId(ViewIdGenerator.generateViewId());
                    autofitTextRel.optimize(ThumbnailActivity.this.wr, ThumbnailActivity.this.hr);
                    autofitTextRel.setOnTouchCallbackListener(ThumbnailActivity.this);
                    autofitTextRel.setBorderVisibility(false);
                    ThumbnailActivity.this.fontName = textInfo.getFONT_NAME();
                    ThumbnailActivity.this.tColor = textInfo.getTEXT_COLOR();
                    ThumbnailActivity.this.shadowColor = textInfo.getSHADOW_COLOR();
                    ThumbnailActivity.this.shadowProg = textInfo.getSHADOW_PROG();
                    ThumbnailActivity.this.tAlpha = textInfo.getTEXT_ALPHA();
                    ThumbnailActivity.this.bgDrawable = textInfo.getBG_DRAWABLE();
                    ThumbnailActivity.this.bgAlpha = textInfo.getBG_ALPHA();
                    ThumbnailActivity.this.rotation = textInfo.getROTATION();
                    ThumbnailActivity.this.bgColor = textInfo.getBG_COLOR();
                    ThumbnailActivity.this.sizeFull++;
                }
            }
            ThumbnailActivity.this.progressBarUndo.setVisibility(View.GONE);
            ThumbnailActivity.this.btnRedo.setVisibility(View.VISIBLE);
            ThumbnailActivity.this.btnUndo.setVisibility(View.VISIBLE);
        }
    }

    private class LoadFrameAsync extends AsyncTask<String, String, Boolean> {

        private LoadFrameAsync() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }


        public Boolean doInBackground(String... strArr) {


            ArrayList<ElementInfoPoster> stickerArrayList;
            ArrayList<TextInfo> textInfoArrayList;
            String str;

            textInfoArrayList = new ArrayList<>();
            stickerArrayList = new ArrayList<>();

            for (int i = 0; i < frameInfoArrayList.size(); i++) {

                int newWidht = getNewWidht(Float.valueOf(frameInfoArrayList.get(i).getSt_x_pos()).floatValue(), Float.valueOf(frameInfoArrayList.get(i).getSt_width()).floatValue());
                int newHeight = getNewHeight(Float.valueOf(frameInfoArrayList.get(i).getSt_y_pos()).floatValue(), Float.valueOf(frameInfoArrayList.get(i).getSt_height()).floatValue());

                int i2 = newWidht < 10 ? 20 : (newWidht <= 10 || newWidht > 20) ? newWidht : 35;
                int i3 = newHeight < 10 ? 20 : (newHeight <= 10 || newHeight > 20) ? newHeight : 35;
                if (frameInfoArrayList.get(i).getSt_field2() != null) {
                    str = frameInfoArrayList.get(i).getSt_field2();
                } else {
                    str = "";
                }
                float parseInt = (frameInfoArrayList.get(i).getSt_rotation() == null || frameInfoArrayList.get(i).getSt_rotation().equals("")) ? 0.0f : (float) Integer.parseInt(frameInfoArrayList.get(i).getSt_rotation());


                float xpos = getXpos(Float.valueOf(frameInfoArrayList.get(i).getSt_x_pos()).floatValue());
                float ypos = getYpos(Float.valueOf(frameInfoArrayList.get(i).getSt_y_pos()).floatValue());

                stickerArrayList.add(new ElementInfoPoster(frameInfoArrayList.get(i).getNAME(), xpos, ypos, i2, i3, parseInt, 0.0f, "", "STICKER", Integer.parseInt(frameInfoArrayList.get(i).getSt_order()), 0, 255, 0, 0, 0, 0, frameInfoArrayList.get(i).getSt_image(), "colored", 1, 0, str, "", "", null, null));
            }
            for (int i5 = 0; i5 < ThumbnailActivity.this.textFrameInfoArrayList.size(); i5++) {


                String justification = ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getTxt_justification();
                String weight = ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getTxt_weight();


                int i6 = ThumbnailActivity.this.postId;


                String text = ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getText();


                String font_family = ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getFont_family();


                int parseColor = Color.parseColor(ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getTxt_color());
                ThumbnailActivity thumbnailActivity5 = ThumbnailActivity.this;
                float xpos2 = thumbnailActivity5.getXpos(Float.valueOf(thumbnailActivity5.textFrameInfoArrayList.get(i5).getTxt_x_pos()).floatValue());
                ThumbnailActivity thumbnailActivity6 = ThumbnailActivity.this;
                float ypos = thumbnailActivity6.getYpos(Float.valueOf(thumbnailActivity6.textFrameInfoArrayList.get(i5).getTxt_y_pos()).floatValue());
                ThumbnailActivity thumbnailActivity7 = ThumbnailActivity.this;

                int newWidht2 = thumbnailActivity7.getNewWidht(Float.valueOf(thumbnailActivity7.textFrameInfoArrayList.get(i5).getTxt_x_pos()).floatValue(), Float.valueOf(ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getTxt_width()).floatValue());

                ThumbnailActivity thumbnailActivity8 = ThumbnailActivity.this;

                textInfoArrayList.add(new TextInfo(i6, text, font_family, parseColor, 100, ViewCompat.MEASURED_STATE_MASK, 0, "0", ViewCompat.MEASURED_STATE_MASK, 0, xpos2, ypos, newWidht2, thumbnailActivity8.getNewHeightText(Float.valueOf(thumbnailActivity8.textFrameInfoArrayList.get(i5).getTxt_y_pos()).floatValue(), Float.valueOf(ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getTxt_height()).floatValue()), Float.parseFloat(ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getTxt_rotation()), "TEXT", Integer.parseInt(ThumbnailActivity.this.textFrameInfoArrayList.get(i5).getTxt_order()), 0, 0, 0, 0, 0, "", "", "", 0.0f, 0.0f, 0, 0));

            }

            txtShapeListFrame = new HashMap<>();
            Iterator<TextInfo> it = textInfoArrayList.iterator();
            while (it.hasNext()) {
                TextInfo next = it.next();
                txtShapeListFrame.put(Integer.valueOf(next.getORDER()), next);
            }
            Iterator<ElementInfoPoster> it2 = stickerArrayList.iterator();
            while (it2.hasNext()) {
                ElementInfoPoster next2 = it2.next();
                txtShapeListFrame.put(Integer.valueOf(next2.getORDER()), next2);
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);

            ArrayList arrayList = new ArrayList(txtShapeListFrame.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();

            for (int i = 0; i < size; i++) {
                Object obj = txtShapeListFrame.get(arrayList.get(i));
                if (obj instanceof ElementInfoPoster) {
                    ElementInfoPoster elementInfo = (ElementInfoPoster) obj;

                    StickerView stickerView = new StickerView(context);

                    txtStkrRel.addView(stickerView);

                    stickerView.setFrameItem(true);
                    stickerView.optimizeScreen(screenWidth, screenHeight);
                    stickerView.setViewWH((float) mainRel.getWidth(), (float) mainRel.getHeight());
                    stickerView.setComponentInfo(elementInfo);
                    stickerView.setId(ViewIdGenerator.generateViewId());
                    stickerView.optimize(wr, hr);
                    stickerView.setOnTouchCallbackListener(ThumbnailActivity.this);
                    stickerView.setBorderVisibility(false);


                    if ((elementInfo.getNAME() != null && elementInfo.getNAME().equals("background"))) {

                        stickerView.isMultiTouchEnabled = stickerView.setDefaultTouchListener(false);

                    }


                } else {
                    AutofitTextRel autofitTextRel = new AutofitTextRel(context);
                    txtStkrRel.addView(autofitTextRel);
                    TextInfo textInfo = (TextInfo) obj;

                    autofitTextRel.setTextInfo(textInfo, false);
                    autofitTextRel.setFrameItem(true);
                    autofitTextRel.setId(ViewIdGenerator.generateViewId());
                    autofitTextRel.optimize(wr, hr);
                    autofitTextRel.setOnTouchCallbackListener(ThumbnailActivity.this);
                    autofitTextRel.setBorderVisibility(false);


                    try {
                        if (textInfo.getFONT_WEIGHT().equals("bold")) {
                            autofitTextRel.setBoldFont();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("left")) {
                            autofitTextRel.setLeftAlignMent();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("center")) {
                            autofitTextRel.setCenterAlignMent();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("right")) {
                            autofitTextRel.setRightAlignMent();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("onFrameLoaded__", "EDITOR _> " + e.getMessage());
                    }


                    Log.d("fontsssss", "" + textInfo.getFONT_NAME());

                    if (textInfo.getFONT_NAME() != null) {
                        setTextFonts(textInfo.getFONT_NAME());

                    }

                    fontName = textInfo.getFONT_NAME();
                    tColor = textInfo.getTEXT_COLOR();
                    shadowColor = textInfo.getSHADOW_COLOR();
                    shadowProg = textInfo.getSHADOW_PROG();
                    tAlpha = textInfo.getTEXT_ALPHA();
                    bgDrawable = textInfo.getBG_DRAWABLE();
                    bgAlpha = textInfo.getBG_ALPHA();
                    rotation = textInfo.getROTATION();
                    bgColor = textInfo.getBG_COLOR();
                    outerColor = textInfo.getOutLineColor();
                    outerSize = textInfo.getOutLineSize();
                    leftRightShadow = (int) textInfo.getLeftRighShadow();
                    topBottomShadow = (int) textInfo.getTopBottomShadow();
                    topBottomShadow = (int) textInfo.getTopBottomShadow();
                }
                sizeFull++;
            }

            if (!overlay_Name.equals("")) {
                setBitmapOverlay(getResources().getIdentifier(overlay_Name, "drawable", getPackageName()));
            }
            saveBitmapUndu();

            universalDialog.dissmissLoadingDialog();

        }
    }

    private void getMusicCategoryData() {

        setupViewPager(binding.musicViewpager);
        binding.musicTabLayout.setupWithViewPager(binding.musicViewpager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(MusicFragment.newInstance(true), "For You");
        adapter.addFragment(MusicFragment.newInstance(false), "Device");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


    private void playOnDisk() {
        disk_lottie = findViewById(R.id.disk_lottie);
        play_pause_btn = findViewById(R.id.play_pause_btn);
        disk_lottie.playAnimation();
        findViewById(R.id.disk_lay).setVisibility(View.VISIBLE);
        findViewById(R.id.remove_music_btn).setOnClickListener(view -> {
            musicPath = "";
            stopMusic();
            findViewById(R.id.disk_lay).setVisibility(View.GONE);
            if (exoplayer != null) {
                exoplayer.getAudioComponent().setVolume(1f);
            }
        });
        play_pause_btn.setOnClickListener(view -> {
            if (musicPlayer != null && musicPlayer.isPlaying()) {
                stopMusic();
            } else {
                if (musicPlayer != null) {
                    musicPlayer.setPlayWhenReady(true);
                } else {
                    //  playMusic(musicPath);
                }

            }
        });
    }


    private void playMusic(String path) {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.setPlayWhenReady(false);
            musicPlayer.stop();
            musicPlayer.release();
        }
        TrackSelector trackSelectorDef = new DefaultTrackSelector();
        musicPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef);
        int appNameStringRes = R.string.app_name;
        String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
        Uri uriOfContentUrl = Uri.parse(path);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source
        musicPlayer.prepare(mediaSource);
        musicPlayer.setPlayWhenReady(true);
        musicPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        musicPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    if (disk_lottie != null) {
                        play_pause_btn.setImageDrawable(getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_pause));
                        disk_lottie.playAnimation();
                    }
                } else {
                    if (disk_lottie != null) {
                        play_pause_btn.setImageDrawable(getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_play));
                        disk_lottie.pauseAnimation();
                    }
                }
            }
        });
    }

    private void stopMusic() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.setPlayWhenReady(false);
        }
    }

    private void releaseMusic() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.setPlayWhenReady(false);
            musicPlayer.stop();
            musicPlayer.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMusic();
        if (isMovie) {
            demoPresenter.onPause();
        }

    }

}
