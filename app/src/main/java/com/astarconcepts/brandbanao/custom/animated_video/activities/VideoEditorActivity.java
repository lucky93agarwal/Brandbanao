package com.astarconcepts.brandbanao.custom.animated_video.activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static com.astarconcepts.brandbanao.utils.MyUtils.readFromFile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.RenderMode;
import com.airbnb.lottie.TextDelegate;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.AdsUtils.RewardAdsManager;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.Recorder.renderer.RenderEngine;
import com.astarconcepts.brandbanao.Recorder.renderer.VideoRecorder;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.ImageAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.TextChangeAdapter;
import com.astarconcepts.brandbanao.custom.poster.activity.ShareImageActivity;
import com.astarconcepts.brandbanao.model.AdsModel;
import com.astarconcepts.brandbanao.ui.activities.SubsPlanActivity;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoEditorActivity extends AppCompatActivity {
    public static int position = -1;
    public static int WIDTH_RATIO;
    public static int HEIGHT_RATIO;
    public static int FOR_ORDERING = 1252;
    public static int FOR_MUSIC = 1253;

    public static String musicPath;
    public static String musicPathOriginal;
    public static boolean isMute = false;

    public static ArrayList<String> files = new ArrayList<>();
    public static EditText report;
    public static LottieAnimationView lottieAnimationView;
    public static TextDelegate textDelegate;
    public AdsModel model;
    String imagePath;
    String templateJsonPath;
    int fileCount = 0;
    int tempCount = 0;
    LinearLayout btnExport;
    ImageView iv_save;
    ArrayList<String> tempimageIds = new ArrayList<>();
    ArrayList<String> imageIds = new ArrayList<>();
    ProgressDialog progressDialog;
    SeekBar seekTemplateDuration;
    int animDuration = 0;
    MediaPlayer mediaPlayer;
    String animationJsonPath;
    RecyclerView image_rv;
    ImageAdapter imageAdapter;
    ArrayList<String> imageWidths = new ArrayList<>();
    ArrayList<String> imageHeight = new ArrayList<>();
    String rootFolder;
    boolean isTemplatePremium = false;
    UniversalDialog dialogMsg;
    private ImageView btn_play;
    private String tempVideoPath;
    private boolean taskSaveRunning = false;
    private ImageView back;
    private Dialog dialogExporting;
    private ProgressBar progressBar;
    private Activity context;
    private Dialog dialogPremium;
    private ProgressBar progressBarPremium;
    private CardView btnChangeText;
    private LinearLayout llText;
    private String strJsonAnim;
    private int progressData;
    private String musicTempPath;
    private String filename;
    private ImageView ivWatermark;
    private InterstitialAd googleAds;
    private CardView llMusic;
    private CardView btnImages;
    private ImageView ivPremium;
    private List<String> nameList = new ArrayList<>();
    private boolean isWatermarkEnabled = true;
    private RoundedBottomSheetDialog dialogtext, imageAddDialog, musicAddDialog;
    private Dialog dialogdiscard;
    private PreferenceManager prefManager;
    private boolean isDirectBack = false;
    private Dialog dialogWatermarkOption;
    private InterstitialsAdsManager interstitialsAdsManager;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_template);

        initView();

        context = this;
        prefManager = new PreferenceManager(context);


        dialogMsg = new UniversalDialog(this, false);

        interstitialsAdsManager = new InterstitialsAdsManager(context);


        textDialog();
        setAddImagesDialog();
        setAddMusicDialog();
        setDialogPremium();

        exitDialog();
        setupDialogWatermarkOption();


        back.setOnClickListener(view -> onBackPressed());

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        btn_play = findViewById(R.id.btn_play);

        btnExport = findViewById(R.id.ll_save);
        ivPremium = findViewById(R.id.iv_premium);
        btnExport.setVisibility(View.VISIBLE);


        Intent intentData = getIntent();

        if (intentData.getExtras() != null) {
            isTemplatePremium = prefManager.getBoolean("premium");
        }

        if (isTemplatePremium) {

            ivPremium.setVisibility(View.VISIBLE);
            btnExport.setBackgroundResource(R.drawable.premium_bg);

        } else {

            ivPremium.setVisibility(View.GONE);
            btnExport.setBackgroundResource(R.drawable.button_save);
        }


        ivWatermark.setOnClickListener(view -> {

            if (!prefManager.getBoolean(Constant.IS_SUBSCRIBE)) {

                dialogWatermarkOption.show();

            } else {

                isWatermarkEnabled = false;
                ivWatermark.setVisibility(View.GONE);
            }


        });

        if (prefManager.getBoolean(Constant.IS_SUBSCRIBE)) {

            isWatermarkEnabled = false;
            ivWatermark.setVisibility(View.GONE);
        }

        configureLottieAnimation();

    }

    @Override
    public void onBackPressed() {


        if (!isDirectBack) {

            if (dialogdiscard != null) {
                dialogdiscard.show();

            }
        } else {
            super.onBackPressed();
        }

    }

    private void exitDialog() {

        dialogdiscard = new Dialog(this);
        this.dialogdiscard.requestWindowFeature(1);
        dialogdiscard.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogdiscard.setContentView(R.layout.discard_dialog);
        dialogdiscard.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogdiscard.setCanceledOnTouchOutside(false);
        dialogdiscard.setCancelable(false);


        LinearLayout yes = dialogdiscard.findViewById(R.id.btn_yes);
        LinearLayout no = dialogdiscard.findViewById(R.id.btn_no);


        yes.setOnClickListener(view12 -> {
            isDirectBack = true;

            dialogdiscard.dismiss();
            onBackPressed();


        });

        no.setOnClickListener(view12 -> {

            if (dialogdiscard.isShowing())
                dialogdiscard.dismiss();

        });

    }

    private void configureLottieAnimation() {

        rootFolder = getIntent().getStringExtra("filepath") + "/" + getIntent().getStringExtra("code");

        musicTempPath = rootFolder;

        animationJsonPath = rootFolder + "/res/data.json";

        imagePath = rootFolder + "/res/images";

        templateJsonPath = rootFolder + "/template.json";

        String textFile = rootFolder + "/res/text.txt";


        // Text Feature -> to change files...

        if (new File(textFile).exists()) {

            textFile = readFromFile(textFile);

            String[] templateTextList = textFile.split("\\|");


            for (int i = 0; i < templateTextList.length; i++) {

                nameList.add(templateTextList[i]);

            }

        }


        lottieAnimationView = findViewById(R.id.lottieAnimationView);


        lottieAnimationView.setRenderMode(RenderMode.HARDWARE);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);

        lottieAnimationView.setOnClickListener(v -> {

            if (lottieAnimationView.isAnimating()) {
                lottieAnimationView.pauseAnimation();

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                }

                btn_play.setVisibility(View.VISIBLE);
            }

        });

        if (new File(musicTempPath, "audio.mp3").exists()) {

            musicPath = musicPathOriginal = new File(musicTempPath, "audio.mp3").getAbsolutePath();
            setupMusic(musicPath, false);


        } else {

            musicPath = musicPathOriginal = musicTempPath = null;

        }


        String jsonForTemplate = "";
        files.clear();

        if (SelectImageActivity.replaceImageList.isEmpty()) {

            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < SelectImageActivity.replaceImageList.size(); i++) {

            if (SelectImageActivity.replaceImageList.get(i).getPath().equals("")) {
                files.add(SelectImageActivity.templateImagesPath.get(i));
            } else {
                files.add(SelectImageActivity.replaceImageList.get(i).getPath());
            }

        }

        strJsonAnim = readFromFile(animationJsonPath);

        try {

            jsonForTemplate = SelectImageActivity.file2String(templateJsonPath);


            JSONArray jsonArray1 = new JSONObject(jsonForTemplate).getJSONArray("elements");

            int numbersOfImages = jsonArray1.length();


            for (int i = 0; i < numbersOfImages; i++) {

                JSONObject object = jsonArray1.getJSONObject(i);

                int editable = object.getInt("editable");

                if (editable == 1) {

                    JSONObject jsonObject = new JSONObject(strJsonAnim);
                    JSONArray jsonArray = jsonObject.getJSONArray("assets");

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    imageWidths.add(jsonObject1.getString("w"));
                    imageHeight.add(jsonObject1.getString("h"));


                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        lottieAnimationView.setImageAssetsFolder(String.valueOf(new File(imagePath).lastModified()));

        imageAdapter = new ImageAdapter(VideoEditorActivity.this, files, imageWidths, imageHeight, lottieAnimationView);

        image_rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        image_rv.setAdapter(imageAdapter);


        // text Adapter & Text Change feature Manage

        try {

            JSONObject templateJson = new JSONObject(strJsonAnim);

            if (templateJson.has("fonts")) {


                lottieAnimationView.setFontAssetDelegate(new FontAssetDelegate() {

                    @Override
                    public Typeface fetchFont(String fontFamily) {

                        try {

                            return Typeface.createFromFile(context.getIntent().getStringExtra("filepath") + "/" + context.getIntent().getStringExtra("code") + "/res/fonts/" + fontFamily + ".ttf");

                        } catch (Exception e) {
                            e.printStackTrace();
                            return Typeface.createFromAsset(getAssets(), "font/Times New Roman.ttf");

                        }
                    }


                });


                textDelegate = new TextDelegate(lottieAnimationView);


                for (String name : nameList) {

                    textDelegate.setText(name, name);

                }


                lottieAnimationView.setTextDelegate(textDelegate);

                btnChangeText.setVisibility(View.VISIBLE);
            } else {

                llText.setVisibility(View.GONE);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            JSONObject jsonObject = new JSONObject(jsonForTemplate);
            JSONArray jsonArray = jsonObject.getJSONArray("elements");

            tempCount = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int editable = jsonObject1.getInt("editable");
                String key = jsonObject1.getString("key");
                if (editable == 1) {
                    imageIds.add(key);
                    tempCount++;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LottieComposition.Factory.fromJsonString(strJsonAnim, composition -> {
            lottieAnimationView.setComposition(composition);

            seekTemplateDuration.setMax(1000);

            seekTemplateDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    if (b) {

                        lottieAnimationView.setProgress(((float) i) / 1000.0f);

                    }


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    lottieAnimationView.resumeAnimation();

                    if (mediaPlayer != null) {


                        long progress = (long) seekBar.getProgress();
                        mediaPlayer.seekTo((int) (((lottieAnimationView.getDuration() * progress) / 1000)));
                        mediaPlayer.start();

                    }

                }
            });

            lottieAnimationView.addAnimatorUpdateListener(valueAnimator -> {

                if (btn_play.getVisibility() == View.VISIBLE) {
                    btn_play.setVisibility(View.GONE);
                }

                if (seekTemplateDuration != null && valueAnimator != null) {
                    seekTemplateDuration.setProgress((int) (valueAnimator.getAnimatedFraction() * 1000.0f));
                }


            });

        });

        fileCount = files.size();
        tempCount = 0;
        ArrayList<String> tempArrayListImgIds = imageIds;

        lottieAnimationView.setImageAssetDelegate(asset -> {
            Bitmap b = null;
            try {
                File f;
                if (imageIds.contains(asset.getId())) {
                    f = new File(files.get(tempArrayListImgIds.indexOf(asset.getId())));
                    tempCount++;
                } else {
                    f = new File(
                            rootFolder + "/res/images",
                            asset.getFileName());
                }

                b = BitmapFactory.decodeStream(new FileInputStream(f));
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                if (MyUtils.getFileExtension(f.getAbsolutePath()).contains("PNG")) {

                    b.compress(Bitmap.CompressFormat.PNG, 90, out);

                } else {

                    b.compress(Bitmap.CompressFormat.JPEG, 90, out);

                }

                b = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                b = Bitmap.createScaledBitmap(b, asset.getWidth(), asset.getHeight(), false);
                lottieAnimationView.updateBitmap(asset.getId(), b);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return b;
        });

        animDuration = (int) lottieAnimationView.getDuration();

        imageAdapter.notifyDataSetChanged();

        btn_play.setOnClickListener(v -> {

            taskSaveRunning = false;

            lottieAnimationView.resumeAnimation();

            btn_play.setVisibility(View.GONE);

            if (!taskSaveRunning) {
                if (musicPath != null) {
                    if (mediaPlayer != null)
                        mediaPlayer.start();

                }

            }

        });


        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);


                if (mediaPlayer != null) {

                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                }

                btn_play.setVisibility(View.VISIBLE);
                position = 0;


            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);


                btn_play.setVisibility(View.GONE);

            }
        });

        btnExport.setOnClickListener(v -> {

            if (!prefManager.getBoolean(Constant.IS_SUBSCRIBE) && isTemplatePremium) {
                dialogPremium.show();
                return;
            }

            if (!isFinishing()) {
                dialogExporting.show();
            }
            exportVideo();

        });

        //Music dialog

        llMusic.setOnClickListener(v -> {

            if (!isFinishing()) {

                musicAddDialog.show();


            }

        });

        btnImages.setOnClickListener(v -> {

            if (!isFinishing()) {

                imageAddDialog.show();

            }

        });

        btnChangeText.setOnClickListener(view -> {

            if (!isFinishing()) {

                dialogtext.show();

            }

        });

        lottieAnimationView.playAnimation();

        if (mediaPlayer != null) {

            mediaPlayer.start();

        }

        btn_play.setVisibility(View.GONE);

        setExportingDialog();


    }

    public void setExportingDialog() {

        dialogExporting = new Dialog(this);

        this.dialogExporting.requestWindowFeature(1);
        dialogExporting.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogExporting.setContentView(R.layout.dialog_export_file);
        dialogExporting.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dialogExporting.setCanceledOnTouchOutside(false);
        dialogExporting.setCancelable(false);

        progressBar = dialogExporting.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        Window window = dialogExporting.getWindow();

        window.setLayout(MATCH_PARENT, MATCH_PARENT);

        new AdsUtils(context).loadNativeAd(context, dialogExporting.findViewById(R.id.fl_adplaceholder));

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.pause();

        lottieAnimationView.pauseAnimation();
        btn_play.setVisibility(View.VISIBLE);

    }

    private void beginCrop(Uri uri) {
        if (uri != null) {
            try {

                Uri destinationUri = Uri.fromFile(new File(context.getCacheDir(), new File(uri.getPath()).getName()));

                UCrop.of(uri, destinationUri)
                        .withAspectRatio(WIDTH_RATIO, HEIGHT_RATIO)
                        .start(context);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getPercentages(int curnt_time, int duration) {

        float progress_f;
        int progress;

        int vid_dur = duration;

        progress_f = ((float) curnt_time / (float) vid_dur) * 100;

        progress = (int) progress_f;

        if (progress >= 100) {
            return 100;
        } else {
            return progress;
        }
    }

    private void openNextActivity(String pasth) {

        File file = new File(pasth);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        sendBroadcast(intent);
        intent = new Intent(context, ShareImageActivity.class);
        intent.putExtra("uri", pasth);
        startActivity(intent);

    }


    private void compressVideo(String path) {

        String outputDir = path.replace(".mp4", "") + "output.mp4";

        progressBar.setMax(100);
        progressBar.setProgress(0);

        TextView tv_title = dialogExporting.findViewById(R.id.tv_title);

        tv_title.setText("Compressing Video...");

        String[] ffmpegCommand = {
                "-i",
                path,
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
                dialogExporting.dismiss();
                MyUtils.showToast(context, "Try Again!!");

            } else if (returnCode == 0) {
                FFmpeg.cancel(executionId);
                dialogExporting.dismiss();

                if (new File(path).exists()) {
                    // delete Previous file.
                    new File(path).delete();

                }
                Toast.makeText(context, "Saved to Gallery!", Toast.LENGTH_SHORT).show();
                interstitialsAdsManager.showInterstitialAd(() -> openNextActivity(outputDir));


            } else if (returnCode == 255) {
                Log.e("finalProcess__", "Command execution cancelled by user.");
            } else {
                String str = String.format("Command execution failed with rc=%d and the output below.",
                        Arrays.copyOf(new Object[]{Integer.valueOf(returnCode)}, 1));
                Log.i("finalProcess__", str);
            }
        });

    }

    public void setupDialogWatermarkOption() {

        dialogWatermarkOption = new Dialog(context, R.style.MyAlertDialog);
        dialogWatermarkOption.setContentView(R.layout.dialog_layout_watermark_option);

        ImageView close = dialogWatermarkOption.findViewById(R.id.iv_close);
        LinearLayout premium = dialogWatermarkOption.findViewById(R.id.cv_no);

        Window window = dialogWatermarkOption.getWindow();

        window.getAttributes().windowAnimations = R.style.VideoQualityDialogAnimation;

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ProgressBar progressBar = dialogWatermarkOption.findViewById(R.id.pb_loading);

        close.setOnClickListener(view -> dialogWatermarkOption.dismiss());


        premium.setOnClickListener(view -> startActivity(new Intent(context, SubsPlanActivity.class)));


        LinearLayout yes = dialogWatermarkOption.findViewById(R.id.cv_yes);

        yes.setOnClickListener(view -> {

            progressBar.setVisibility(View.VISIBLE);

            new RewardAdsManager(context, new RewardAdsManager.OnAdLoaded() {
                @Override
                public void onAdClosed() {

                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(VideoEditorActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdWatched() {

                    progressBar.setVisibility(View.GONE);
                    removeWatermarkAndProcessOption(progressBar);
                }
            });


        });

    }

    private void removeWatermarkAndProcessOption(ProgressBar progressBar) {
        Toast.makeText(context, "Congratulations, You Removed watermark.", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.GONE);
        isWatermarkEnabled = false;
        ivWatermark.setVisibility(View.GONE);
        dialogWatermarkOption.dismiss();

    }

    public void exportVideo() {

        lottieAnimationView.pauseAnimation();

        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

        animDuration = (int) lottieAnimationView.getDuration();


        dialogExporting.show();

        String tempVideoPath = MyUtils.getFolderPath(context, "VideoOutput/tmp") + "/" + getString(R.string.app_name) + "_animated_" + System.currentTimeMillis() + ".mp4";

        VideoRecorder recorder = new VideoRecorder(this, findViewById(R.id.lottieAnimationView), lottieAnimationView, tempVideoPath);

        recorder.setInterfaceRenderEngine(new RenderInterface(progressBar));


    }

    @SuppressLint("WrongThread")

    private void initView() {
        back = findViewById(R.id.iv_back);
        btnChangeText = findViewById(R.id.btnText);
        llText = findViewById(R.id.llText);
        llMusic = findViewById(R.id.btnAudio);
        ivWatermark = findViewById(R.id.ivWatermark);
        btnImages = findViewById(R.id.btnImages);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        seekTemplateDuration = findViewById(R.id.seek_template_duration);

    }

    public void setupMusic(String path, boolean isPlay) {

        if (path != null) {

            mediaPlayer = MediaPlayer.create(context, Uri.parse(path));

            if (isPlay) {

                lottieAnimationView.playAnimation();
                btn_play.setVisibility(View.GONE);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();

            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1005) {

                beginCrop(data.getData());

            } else if (requestCode == UCrop.REQUEST_CROP) {

                files.set(position, MyUtils.getPathFromURI(VideoEditorActivity.this, UCrop.getOutput(data)));

                imageAdapter = new ImageAdapter(VideoEditorActivity.this, files, imageWidths, imageHeight, lottieAnimationView);
                imageAdapter.notifyDataSetChanged();
                image_rv.setAdapter(imageAdapter);
                tempimageIds = imageIds;
                Bitmap b = null;

                try {
                    Log.d("FilePathCheck", "" + files.get(position));
                    b = BitmapFactory.decodeStream(new FileInputStream(files.get(position)));
                    ByteArrayOutputStream out = new ByteArrayOutputStream();


                    if (MyUtils.getFileExtension(files.get(position)).contains("PNG")) {

                        b.compress(Bitmap.CompressFormat.PNG, 90, out);

                    } else {

                        b.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    }

                    b = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                    lottieAnimationView.updateBitmap(tempimageIds.get(position), Bitmap.createScaledBitmap(b, Integer.parseInt(imageWidths.get(position)), Integer.parseInt(imageHeight.get(position)), false));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                taskSaveRunning = false;

                lottieAnimationView.setProgress(0);
                lottieAnimationView.playAnimation();
                btn_play.setVisibility(View.GONE);

                if (mediaPlayer != null) {

                    if (!taskSaveRunning) {

                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();

                    }

                }


            } else if (requestCode == VideoEditorActivity.FOR_ORDERING) {
                position = 0;

                for (position = 0; position < files.size(); position++) {
                    Bitmap b = null;
                    try {


                        b = BitmapFactory.decodeStream(new FileInputStream(files.get(position)));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();


                        if (MyUtils.getFileExtension(files.get(position)).contains("PNG")) {


                            b.compress(Bitmap.CompressFormat.PNG, 90, out);

                        } else {

                            b.compress(Bitmap.CompressFormat.JPEG, 90, out);

                        }

                        b = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                        lottieAnimationView.updateBitmap("image_" + position, Bitmap.createScaledBitmap(b, Integer.parseInt(imageWidths.get(position)), Integer.parseInt(imageHeight.get(position)), false));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                imageAdapter.notifyDataSetChanged();

                taskSaveRunning = false;

                lottieAnimationView.setProgress(0);
                lottieAnimationView.playAnimation();
                btn_play.setVisibility(View.GONE);

                if (!taskSaveRunning) {

                    if (mediaPlayer != null) {

                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    }


                }

            } else if (requestCode == VideoEditorActivity.FOR_MUSIC) {


                try {


                    Log.d("musicfiless", musicPath);

                    mediaPlayer = new MediaPlayer();

                    mediaPlayer.setDataSource(context, Uri.parse(musicPath));
                    mediaPlayer.prepare();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                taskSaveRunning = false;

                lottieAnimationView.playAnimation();
                btn_play.setVisibility(View.GONE);

                if (mediaPlayer != null) {
                    if (!taskSaveRunning) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    }
                }
            }
        }
    }

    private void ffmpegProcess(String tempVideoPath, String outputPath) {

        progressBar.setProgress(50);

        File fileWatermark = new File(context.getExternalCacheDir(), "water_mark.png");

        List<String> commands = ApiClient.getCommands2(tempVideoPath, fileWatermark.getAbsolutePath(), isWatermarkEnabled, musicPath, isMute, outputPath);

        String[] ffmpegRenderArray = new String[commands.size()];

        commands.toArray(ffmpegRenderArray);


        FFmpeg.executeAsync(ffmpegRenderArray, (executionId, returnCode) -> {

            if (returnCode == RETURN_CODE_SUCCESS) {

                new File(tempVideoPath).delete();

                compressVideo(outputPath);


            } else {


                Toast.makeText(context, "Video Export Failed.", Toast.LENGTH_SHORT).show();

                if (!isFinishing())
                    dialogExporting.dismiss();

                finish();
                onBackPressed();

            }


        });

        Config.enableStatisticsCallback(statistics -> {

            int ffmpegprogress = (getPercentages(statistics.getTime(), animDuration)) / 2;

            new Handler(Looper.getMainLooper()).postDelayed(() -> progressBar.setProgress(progressData + ffmpegprogress), 100);


        });
    }

    private void textDialog() {

        dialogtext = new RoundedBottomSheetDialog(context);
        this.dialogtext.requestWindowFeature(1);
        dialogtext.setContentView(R.layout.texts_container);
        dialogtext.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        RecyclerView textChangeRv = dialogtext.findViewById(R.id.textChangeRv);

        TextChangeAdapter textChangeAdapter = new TextChangeAdapter(context, nameList, lottieAnimationView);

        textChangeRv.setLayoutManager(new LinearLayoutManager(this));
        textChangeRv.setAdapter(textChangeAdapter);

        TextView confirm = dialogtext.findViewById(R.id.confirm_button);

        confirm.setOnClickListener(v -> dialogtext.dismiss());


    }

    private void setAddImagesDialog() {

        imageAddDialog = new RoundedBottomSheetDialog(VideoEditorActivity.this);

        imageAddDialog.setContentView(R.layout.images_container);
        imageAddDialog.setCanceledOnTouchOutside(true);
        imageAddDialog.setCancelable(true);

        image_rv = imageAddDialog.findViewById(R.id.image_rv);
        imageAdapter = new ImageAdapter(VideoEditorActivity.this, files, imageWidths, imageHeight, lottieAnimationView);

        image_rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        image_rv.setAdapter(imageAdapter);


    }

    private void setAddMusicDialog() {

        musicAddDialog = new RoundedBottomSheetDialog(VideoEditorActivity.this);

        musicAddDialog.setContentView(R.layout.audio_container);
        musicAddDialog.setCanceledOnTouchOutside(true);
        musicAddDialog.setCancelable(true);

        LinearLayout llAdd_Music = musicAddDialog.findViewById(R.id.btnLocal);
        LinearLayout default_Music = musicAddDialog.findViewById(R.id.btnDefault);
        LinearLayout llMute = musicAddDialog.findViewById(R.id.btnRemove);
        llMute.setOnClickListener(view -> {


            if (musicPath != null) {


                if (isMute) {

                    isMute = false;

                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(1.0f, 1.0f);
                        if (!mediaPlayer.isPlaying()) {
                            setupMusic(musicPath, true);
                        }
                    }

                } else {

                    isMute = true;
                    // mute music

                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0, 0);
                    }

                }
            } else {

                Toast.makeText(this, "Select Music First", Toast.LENGTH_SHORT).show();

            }


        });


        default_Music.setOnClickListener(view -> {

            lottieAnimationView.setProgress(0);
            lottieAnimationView.playAnimation();
            btn_play.setVisibility(View.GONE);

            musicPath = musicPathOriginal;

            if (musicPath != null) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                mediaPlayer.release();

                setupMusic(musicPath, true);

            } else {

                if (mediaPlayer != null) {

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }

            }


        });

        llAdd_Music.setOnClickListener(v -> {

            context.startActivityForResult(new Intent(VideoEditorActivity.this, SelectMusicActivity.class), FOR_MUSIC);
            musicAddDialog.dismiss();

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


            progressBarPremium.setVisibility(View.GONE);

        });

        premium.setOnClickListener(view -> {

            startActivity(new Intent(context, SubsPlanActivity.class));
            dialogPremium.dismiss();

        });

        LinearLayout yes = dialogPremium.findViewById(R.id.cv_yes);

        yes.setOnClickListener(view -> {

            progressBarPremium.setVisibility(View.VISIBLE);

            new RewardAdsManager(context, new RewardAdsManager.OnAdLoaded() {
                @Override
                public void onAdClosed() {
                    Toast.makeText(context, "Ad Not loaded", Toast.LENGTH_SHORT).show();
                    progressBarPremium.setVisibility(View.GONE);
                }

                @Override
                public void onAdWatched() {
                    Toast.makeText(context, "Congratulations, You unlocked it", Toast.LENGTH_SHORT).show();
                    progressBarPremium.setVisibility(View.GONE);
                    dialogPremium.dismiss();

                    if (!isFinishing()) {
                        dialogExporting.show();
                    }
                    exportVideo();
                }
            });


        });

    }

    public class RenderInterface implements RenderEngine.RenderEngienInterface {


        public final ProgressBar progressBar;

        public RenderInterface(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }


        public void onProgress(float f) {
            int progress = (int) f;

            progressData = progress / 2;

            this.progressBar.setProgress(progressData);

            Log.i("POIU", "onProgressChange: " + progress);

        }

        public void onExporteed(File file) {

            progressBar.setProgress(50);

            filename = getString(R.string.app_name) + "_" + System.currentTimeMillis();

            String location = MyUtils.getStoreVideoExternalStorage(context) + "/" + filename + "p.mp4";

            String pasth = location;

            tempVideoPath = file.getAbsolutePath();

            if (new File(tempVideoPath).exists()) {

                ffmpegProcess(tempVideoPath, pasth);

            }


        }

    }
}
