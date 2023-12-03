package com.astarconcepts.brandbanao.custom.animated_video.activities;


import static com.astarconcepts.brandbanao.utils.MyUtils.readFromFile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.RenderMode;
import com.airbnb.lottie.TextDelegate;
import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;
import com.astarconcepts.brandbanao.model.AdsModel;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.Priority;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PreviewVideoTemplate extends AppCompatActivity {
    public static int fileDownloadingId;
    String templateCode;
    Activity context;
    String videoId = "";

    private int CHOOSE_IMAGE = 1003;
    private boolean DOWNLOADED_ZIP = false;
    private Dialog dialogDownloading;
    private String zipLink = "";
    boolean isTemplatePremium = false;
    private ProgressBar progressBar;
    private ImageView back;
    private TextView title;
    private LottieAnimationView lottieAnimationView;
    private ImageView btnPlay;
    private TextView createStoryButton;

    SeekBar seekTemplateDuration;
    private ImageView btn_play;


    TemplateModel templateModel;

    AdsModel model;
    private MediaPlayer mediaPlayer;
    int fileCount = 0;

    public static ArrayList<String> files = new ArrayList<>();
    int tempCount = 0;
    ArrayList<String> imageIds = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private File checkFile = null;
    PreferenceManager preferenceManager;
    private int selectedReplcePos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_template);

        context = this;
        preferenceManager = new PreferenceManager(context);


        initView();

        title.setText(R.string.preview);


        back.setOnClickListener(v -> onBackPressed());

        templateModel = MyUtils.templateModel;

        new AdsUtils(context).showBannerAds(context);

        videoId = templateModel.getId();
        zipLink = templateModel.getZip_link();
        isTemplatePremium = templateModel.isPremium();

        templateCode = zipLink.substring(zipLink.lastIndexOf('/') + 1).split("\\.")[0];

        try {

            Constant.addTemplateCreationCount(Integer.parseInt(videoId), Constant.FRAME_TYPE_ANIMATED);

        } catch (Exception e) {
            e.printStackTrace();
        }

        setDownloadDialog();

        checkFile = new File(MyUtils.getFolderPath(context, "ae"), templateModel.getCode());

        createStoryButton.setOnClickListener(v -> {
            createVideoProcess();

        });

        if (checkFile.exists()) {

            setUpLottieAnimation(checkFile.getAbsolutePath());

        } else {

            downloadFile(MyUtils.getFolderPath(context, "ae"));

        }

    }


    private void setUpLottieAnimation(String path) {

        lottieAnimationView.setRenderMode(RenderMode.HARDWARE);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);

        lottieAnimationView.setOnClickListener(v -> {

            if (lottieAnimationView.isAnimating()) {
                lottieAnimationView.pauseAnimation();

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                }

                btnPlay.setVisibility(View.VISIBLE);
            }

        });

        String animationJsonPath = path + "/res/data.json";
        String templateJsonPath = path + "/template.json";


        String strJsonAnim = readFromFile(animationJsonPath);


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


                String textFile = path + "/res/text.txt";

                if (new File(textFile).exists()) {

                    textFile = readFromFile(textFile);

                    String[] templateTextList = textFile.split("\\|");


                    for (int i = 0; i < templateTextList.length; i++) {

                        nameList.add(templateTextList[i]);

                    }

                }

                TextDelegate textDelegate = new TextDelegate(lottieAnimationView);
                for (String name : nameList) {

                    textDelegate.setText(name, name);

                }


                lottieAnimationView.setTextDelegate(textDelegate);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonForTemplate = "";

        files.clear();
        imageIds.clear();
        nameList.clear();

        try {

            jsonForTemplate = SelectImageActivity.file2String(templateJsonPath);

            JSONArray jsonArray1 = new JSONObject(jsonForTemplate).getJSONArray("elements");

            int numbersOfImages = jsonArray1.length();


            for (int i = 0; i < numbersOfImages; i++) {

                JSONObject object = jsonArray1.getJSONObject(i);

                int editable = object.getInt("editable");

                String key = object.getString("key");
                if (editable == 1) {

                    files.add(path + "/" + object.getString("image"));
                    imageIds.add(key);
                    tempCount++;
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        btnPlay.setOnClickListener(v -> {


            lottieAnimationView.resumeAnimation();

            btnPlay.setVisibility(View.GONE);

            setupMusic(new File(path, "audio.mp3").getAbsolutePath(), true);


        });

        LottieCompositionFactory.fromJsonString(strJsonAnim, templateCode).addListener(composition -> {

            lottieAnimationView.setComposition(composition);

            lottieAnimationView.setProgress(0);
            lottieAnimationView.playAnimation();

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


                if (seekTemplateDuration != null && valueAnimator != null) {
                    seekTemplateDuration.setProgress((int) (valueAnimator.getAnimatedFraction() * 1000.0f));
                }


            });
        });


        fileCount = files.size();
        tempCount = 0;
        ArrayList<String> tempArrayListImgIds = imageIds;

        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);


                if (mediaPlayer != null) {

                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                }
                lottieAnimationView.setProgress(0);

                btnPlay.setVisibility(View.VISIBLE);
                // position = 0;


            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                btnPlay.setVisibility(View.GONE);
                setupMusic(new File(path, "audio.mp3").getAbsolutePath(), true);
            }
        });

        lottieAnimationView.setImageAssetDelegate(asset -> {
            Bitmap b = null;
            try {
                File f;
                if (imageIds.contains(asset.getId())) {

                    f = new File(files.get(tempArrayListImgIds.indexOf(asset.getId())));
                    tempCount++;
                } else {
                    f = new File(
                            path + "/res/images",
                            asset.getFileName());
                }


                b = BitmapFactory.decodeStream(new FileInputStream(f));
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                if (MyUtils.getFileExtension(f.getAbsolutePath()).contains("PNG")) {


                    b.compress(Bitmap.CompressFormat.PNG, 90, out);

                } else {

                    b.compress(Bitmap.CompressFormat.JPEG, 80, out);

                }

                b = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                b = Bitmap.createScaledBitmap(b, asset.getWidth(), asset.getHeight(), false);
                lottieAnimationView.updateBitmap(asset.getId(), b);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return b;
        });

    }

    public void setupMusic(String path, boolean isPlay) {

        if (path != null && new File(path).exists()) {

            mediaPlayer = MediaPlayer.create(context, Uri.parse(path));

            if (isPlay) {

                if (mediaPlayer != null) {


                    btnPlay.setVisibility(View.GONE);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }

            }

        }

    }

    private void createVideoProcess() {

        selectedReplcePos = 0;

        File checkFile = new File(MyUtils.getFolderPath(context, "ae"), templateCode);

        if (checkFile.exists()) {

            lottieAnimationView.postDelayed(() -> {

                Intent intent = new Intent(context, SelectImageActivity.class);
                intent.putExtra("filepath", MyUtils.getFolderPath(context, "ae"));
                intent.putExtra("code", templateModel.getCode());
                intent.putExtra("template", templateModel.getTemplate_Json());
                intent.putExtra("premium", templateModel.isPremium());

                preferenceManager.setBoolean("premium", templateModel.isPremium());
                context.startActivity(intent);
            }, 100);


        } else {

            MyUtils.templateModel = templateModel;

            Intent intent = new Intent(context, VideoEditorActivity.class);
            intent.putExtra("code", "" + templateModel.getCode());
            intent.putExtra("id", "" + templateModel.getId());
            intent.putExtra("filepath", MyUtils.getFolderPath(context, "ae"));
            intent.putExtra("template", "" + templateModel.getTemplate_Json());
            intent.putExtra("zip_link", "" + templateModel.getZip_link());
            intent.putExtra("premium", templateModel.isPremium());

            preferenceManager.setBoolean("premium", templateModel.isPremium());
            context.startActivity(intent);


        }

    }


    public void setDownloadDialog() {
        dialogDownloading = new Dialog(this);

        this.dialogDownloading.requestWindowFeature(1);
        dialogDownloading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogDownloading.setContentView(R.layout.dialog_download);
        dialogDownloading.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogDownloading.setCanceledOnTouchOutside(false);
        dialogDownloading.setCancelable(false);

        progressBar = dialogDownloading.findViewById(R.id.progressBar);

        Window window = dialogDownloading.getWindow();

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        progressBar.setProgress(0);


        new AdsUtils(context).loadNativeAd(context, dialogDownloading.findViewById(R.id.fl_adplaceholder));
    }

    public void downloadFile(String outPutPath) {

        ImageView close = dialogDownloading.findViewById(R.id.iv_close);
        TextView tv_title = dialogDownloading.findViewById(R.id.tv_title);

        if (!isFinishing()) {

            dialogDownloading.show();
        }

        close.setOnClickListener(v -> {

            PRDownloader.cancel(fileDownloadingId);
            dialogDownloading.dismiss();
            finish();

        });


        fileDownloadingId = PRDownloader.download(zipLink, outPutPath, templateCode + ".zip").setPriority(Priority.HIGH).build()
                .setOnProgressListener(progress -> {

                    int bytesWritten = (int) ((progress.currentBytes * 100) / progress.totalBytes);

                    if (bytesWritten != 100) {
                        progressBar.setProgress(bytesWritten);
                    }


                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {


                        try {
                            MyUtils.unzip(new File(outPutPath, templateCode + ".zip"), new File(MyUtils.getFolderPath(context, "ae")), path -> {


                                setUpLottieAnimation(checkFile.getAbsolutePath());

                            });

                            dialogDownloading.dismiss();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        tv_title.setText("Download Success !!");
                    }

                    @Override
                    public void onError(Error error) {

                        Toast.makeText(context, "Download Failed. Please try again later.", Toast.LENGTH_SHORT).show();

                        PRDownloader.cancel(fileDownloadingId);

                        if (dialogDownloading.isShowing()) {

                            dialogDownloading.dismiss();
                        }


                        onBackPressed();


                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

        lottieAnimationView.pauseAnimation();
        btnPlay.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE) {
            Handler handler_intent = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (DOWNLOADED_ZIP) {

                        handler_intent.removeCallbacks(this);
                        Intent intent = new Intent(context, VideoEditorActivity.class);
                        intent.putExtra("filepath", MyUtils.getFolderPath(context, "ae"));
                        intent.putExtra("code", getIntent().getStringExtra("code"));
                        startActivity(intent);

                        finish();

                    } else {
                        handler_intent.postDelayed(this, 100);
                    }
                }
            };
            handler_intent.postDelayed(runnable, 100);

        }
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.lottieAnimationView);
        btnPlay = (ImageView) findViewById(R.id.btn_play);
        createStoryButton = findViewById(R.id.createStoryButton);
        seekTemplateDuration = findViewById(R.id.seek_template_duration);
        title = findViewById(R.id.tool_name);
    }
}