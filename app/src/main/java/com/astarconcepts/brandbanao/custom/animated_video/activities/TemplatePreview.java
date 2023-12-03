package com.astarconcepts.brandbanao.custom.animated_video.activities;


import static com.astarconcepts.brandbanao.utils.MyUtils.readFromFile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.TextDelegate;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.utils.MyUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TemplatePreview extends AppCompatActivity {

    public static ArrayList<String> files = new ArrayList<>();
    Activity context;
    String animationJson;
    String overlayPath;
    String imageAssets;
    String musicPath;
    int fileCount = 0;
    int tempCount = 0;
    ArrayList<String> tempimageIds = new ArrayList<>();
    ArrayList<String> imageIds = new ArrayList<>();
    private LottieAnimationView lottieAnimationView;
    private ProgressBar progressBar;
    private MediaPlayer mediaPlayer;
    private String rootFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_template_preview);
        initView();
        context = this;

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            animationJson = intent.getStringExtra("animationJson");
            overlayPath = intent.getStringExtra("overlay");
            musicPath = intent.getStringExtra("music");
            imageAssets = intent.getStringExtra("imageAssets");
            rootFolder = intent.getStringExtra("rootFolder");
            files = (ArrayList<String>) intent.getSerializableExtra("files");
            imageIds = (ArrayList<String>) intent.getSerializableExtra("imageIds");
            tempimageIds = (ArrayList<String>) intent.getSerializableExtra("tempimageIds");

        }

        try {

            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(musicPath)));


        } catch (Exception e) {
            e.printStackTrace();

        }

        lottieAnimationView.setImageAssetsFolder(imageAssets);

        String strJsonAnim = readFromFile(animationJson);


        progressBar.setProgress(0);
        progressBar.setMax(1000);

        LottieComposition.Factory.fromJsonString(strJsonAnim, composition -> {
            lottieAnimationView.setComposition(composition);
            lottieAnimationView.playAnimation();


            lottieAnimationView.addAnimatorUpdateListener(valueAnimator -> {


                if (progressBar != null && valueAnimator != null) {
                    progressBar.setProgress((int) (valueAnimator.getAnimatedFraction() * 1000.0f));
                }


            });


        });

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
                    f = new File(rootFolder + "/res/images",
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
        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                finish();

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mediaPlayer != null)
                    mediaPlayer.start();

            }
        });

        TextDelegate textDelegate = new TextDelegate(lottieAnimationView);

        lottieAnimationView.setFontAssetDelegate(new FontAssetDelegate() {

            @Override
            public Typeface fetchFont(String fontFamily) {

                if (new File(rootFolder + "/res/fonts/" + fontFamily + ".ttf").exists()) {

                    return Typeface.createFromFile(rootFolder + "/res/fonts/" + fontFamily + ".ttf");
                } else {

                    return Typeface.createFromAsset(getAssets(), "fonts/Times New Roman.ttf");
                }

            }


        });

        lottieAnimationView.setTextDelegate(textDelegate);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();

    }

    @Override
    protected void onPause() {
        super.onPause();

        releasePlayer();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        releasePlayer();

    }

    private void releasePlayer() {

        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        progressBar = findViewById(R.id.progressBar);
    }
}
