package com.astarconcepts.brandbanao.custom.poster.activity;

import static com.astarconcepts.brandbanao.utils.MyUtils.buttonClick;
import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.BuildConfig;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.ui.activities.MainActivity;
import com.astarconcepts.brandbanao.ui.activities.SubsPlanActivity;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.stepstone.apprating.AppRatingDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;


public class ShareImageActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView imageView;
    public Uri phototUri = null;
    public File pictureFile;
    PreferenceManager preferenceManager;
    private Activity context;
    private String path = null;
    private InterstitialsAdsManager interstitialsAdsManager;
    ImageView poster_iv;
    String file_name;
    PlayerView playerview;
    ExoPlayer exoplayer;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_saved_post);

        context = this;
        interstitialsAdsManager = new InterstitialsAdsManager(context);
        preferenceManager = new PreferenceManager(context);

        initView();

        path = getIntent().getStringExtra("uri");

        Log.d("dataexported","dataexported 0 :"+path);


        if (path != null && path.endsWith(".mp4")) {
            file_name = System.currentTimeMillis() + ".mp4";
            playerview.setVisibility(View.VISIBLE);
            poster_iv.setVisibility(View.GONE);
            initializePlayer();

            pictureFile = new File(path);

        } else {
            file_name = System.currentTimeMillis() + ".jpg";
            playerview.setVisibility(View.GONE);
            poster_iv.setVisibility(View.VISIBLE);
            try {

                pictureFile = new File(path);

                Glide.with(this).load(path).placeholder(R.drawable.spaceholder).into(poster_iv);

                Log.d("dataexported","dataexported 1 :"+path);

                File root = new File(Environment.getExternalStorageDirectory() + File.separator
                        + Environment.DIRECTORY_PICTURES + File.separator + getString(R.string.app_name) + File.separator +file_name);

           /*     try {

                    boolean isMoved = new File(path).renameTo(root);
                    Toast.makeText(context, "" + isMoved, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                    e.printStackTrace();

                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        initUI();

    }

    private void initView() {
        ImageView btnBack = findViewById(R.id.back);
        ImageView btnMoreApp = findViewById(R.id.more);
        ImageView btnShareFacebook = findViewById(R.id.facebook);
        ImageView btnShareInstagram = findViewById(R.id.instagram);
        ImageView btnShareWhatsapp = findViewById(R.id.whatsapp);
        ImageView ivHome = findViewById(R.id.iv_home);
        ImageView ivPremium = findViewById(R.id.ivPremium);
        btnBack.setOnClickListener(this);
        btnMoreApp.setOnClickListener(this);
        btnShareFacebook.setOnClickListener(this);
        btnShareInstagram.setOnClickListener(this);
        btnShareWhatsapp.setOnClickListener(this);
        poster_iv = findViewById(R.id.iv_save_image);
        playerview = findViewById(R.id.videoPlayer);

        ivHome.setOnClickListener(v -> {

            startActivity(new Intent(context, MainActivity.class));
            finish();
        });

        ivPremium.setOnClickListener(v -> startActivity(new Intent(context, SubsPlanActivity.class)));

    }

    public void initUI() {
        this.imageView = findViewById(R.id.iv_save_image);
        this.imageView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);

    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        } else {
            switch (id) {
                case R.id.facebook:
                    interstitialsAdsManager.showInterstitialAd(() -> shareToFacebook(pictureFile.getPath()));
                    view.startAnimation(buttonClick);
                    return;

                case R.id.instagram:


                    interstitialsAdsManager.showInterstitialAd(() -> shareToInstagram(pictureFile.getPath()));
                    view.startAnimation(buttonClick);


                    return;

                case R.id.whatsapp:

                    view.startAnimation(buttonClick);

                    interstitialsAdsManager.showInterstitialAd(() -> sendToWhatsaApp(pictureFile.getPath()));
                    return;

                case R.id.more:

                    view.startAnimation(buttonClick);
                    interstitialsAdsManager.showInterstitialAd(() -> shareMoreButton());
                    return;


                default:
            }
        }
    }

    private void shareMoreButton() {
        try {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(path));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt) + getPackageName());
            startActivity(Intent.createChooser(intent, "Share Your Flyer!"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void newLibRateDialog() {
        new AppRatingDialog.Builder().setPositiveButtonText("Submit").setNegativeButtonText("Cancel").setNeutralButtonText("Later").setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!")).setDefaultRating(2).setTitle("Rate this application").setDescription("Please select some stars and give your feedback").setCommentInputEnabled(false).setStarColor(R.color.yellow).setNoteDescriptionTextColor(R.color.text_color).setTitleTextColor(R.color.text_color).setDescriptionTextColor(R.color.text_color).setHint("Please write your comment here ...").setHintTextColor(R.color.hintTextColor).setWindowAnimation(R.style.MyDialogFadeAnimation).setCancelable(false).setCanceledOnTouchOutside(false).create(this).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void sendToWhatsaApp(String str) {
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.whatsapp") != null) {
            try {
                Uri uriForFile = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(str));
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt) + getPackageName());
                intent.setPackage("com.whatsapp");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("WrongConstant")
    public void shareToFacebook(String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setPackage("com.facebook.katana");
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.facebook.katana") != null) {
            try {
                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(str)));
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt) + getPackageName());
                intent.addFlags(1);
                startActivity(Intent.createChooser(intent, "Share Gif."));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Facebook not installed", Toast.LENGTH_SHORT).show();
        }
    }


    public void shareToInstagram(String str) {
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.instagram.android") != null) {
            try {
                Uri uriForFile = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(str));
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                new File(str);
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setPackage("com.instagram.android");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Instagram not installed", Toast.LENGTH_SHORT).show();
        }
    }


    public void saveBitmap() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.plzwait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(() -> {
            try {
                ShareImageActivity.this.pictureFile = new File(ShareImageActivity.this.phototUri.getPath());
                try {
                    if (!ShareImageActivity.this.pictureFile.exists()) {
                        ShareImageActivity.this.pictureFile.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(ShareImageActivity.this.pictureFile);
                    ThumbnailActivity.withoutWatermark.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    MediaScannerConnection.scanFile(ShareImageActivity.this, new String[]{ShareImageActivity.this.pictureFile.getAbsolutePath()}, null, (str, uri) -> {
                        Log.i("ExternalStorage", "Scanned " + str + ":");
                        String sb = "-> uri=" +
                                uri;
                        Log.i("ExternalStorage", sb);
                    });
                    ShareImageActivity shareImageActivity = ShareImageActivity.this;
                    ShareImageActivity shareImageActivity2 = ShareImageActivity.this;
                    shareImageActivity.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", FileProvider.getUriForFile(shareImageActivity2, ShareImageActivity.this.getApplicationContext().getPackageName() + ".provider", ShareImageActivity.this.pictureFile)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }).start();
        progressDialog.setOnDismissListener(dialogInterface -> {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            ShareImageActivity.this.imageView.setImageBitmap(BitmapFactory.decodeFile(ShareImageActivity.this.pictureFile.getAbsolutePath(), options));
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (exoplayer != null) {
            exoplayer.setPlayWhenReady(false);
        }
    }

    private void initializePlayer() {
        playerview.setUseController(true);
        playerview.setControllerHideOnTouch(true);
        playerview.setShowBuffering(true);
        TrackSelector trackSelectorDef = new DefaultTrackSelector();
        exoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef);


        int appNameStringRes = R.string.app_name;
        String userAgent = Util.getUserAgent(this, this.getString(appNameStringRes));
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
        Uri uriOfContentUrl = Uri.parse(path);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source

        exoplayer.prepare(mediaSource);
        exoplayer.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline
        playerview.setPlayer(exoplayer); // attach surface to the view
        playerview.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
        exoplayer.addListener(new Player.EventListener() {

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Player.EventListener.super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);
                switch (playbackState) {
                    case ExoPlayer.STATE_ENDED:

                        break;
                }
            }
        });

        playerview.setOnTouchListener(new View.OnTouchListener() {

            private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);

                    if (!exoplayer.getPlayWhenReady()) {
                        exoplayer.setPlayWhenReady(true);
                        playerview.hideController();


                    } else {

                        new Handler(getMainLooper()).postDelayed(() -> exoplayer.setPlayWhenReady(false), 100);
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


}
