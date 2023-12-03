package com.astarconcepts.brandbanao.custom.poster.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.toolbox.ImageRequest;
import com.astarconcepts.brandbanao.MyApplication;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.poster.adapter.VeticalViewAdapter;
import com.astarconcepts.brandbanao.custom.poster.fragment.BackgroundFragment1;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSelectSize;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSnapListener;
import com.astarconcepts.brandbanao.custom.poster.listener.RecyclerViewLoadMoreScroll;
import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;
import com.astarconcepts.brandbanao.custom.poster.model.MainBG;
import com.astarconcepts.brandbanao.custom.poster.model.Snap2;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbBG;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.StorageUtils;
import com.astarconcepts.brandbanao.utils.YourDataProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.Scopes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import yuku.ambilwarna.AmbilWarnaDialog;

public class BackgrounImageActivity extends BaseActivity implements View.OnClickListener, GetSnapListener, GetSelectSize {
    private static final int SELECT_PICTURE_FROM_CAMERA = 9062;
    private static final int SELECT_PICTURE_FROM_GALLERY = 9072;
    private static final String TAG = "BackgrounImageActivity";
    private final int bColor = Color.parseColor("#4149b6");
    private final List<WeakReference<Fragment>> mFragments = new ArrayList();
    public Animation animSlideUp;
    public PreferenceManager PreferenceManager;
    public ProgressDialog dialogIs;
    public File file;
    public LottieAnimationView loading_view;
    public String ratio = "1:1";
    public Uri resultUri;
    public RecyclerViewLoadMoreScroll scrollListener;
    public VeticalViewAdapter veticalViewAdapter;
    RelativeLayout fragmen_container;
    LinearLayoutManager mLinearLayoutManager;
    UCrop.Options options;
    float screenHeight;
    float screenWidth;
    ArrayList<Object> snapData = new ArrayList<>();
    ArrayList<MainBG> thumbnailBg = new ArrayList<>();
    YourDataProvider yourDataProvider;
    private Animation animSlideDown;
    private boolean isSizeSelected = false;
    private RecyclerView mRecyclerView;
    private TextView textviewRatio;
    private Activity context;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_select_backgroundimage);

        context = this;

        this.options = new UCrop.Options();
        this.options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setToolbarColor(getResources().getColor(R.color.white));
        options.setToolbarWidgetColor(getResources().getColor(R.color.color_add_btn));
        this.loading_view = findViewById(R.id.loading_view);

        findViews();
        this.mRecyclerView = findViewById(R.id.background_recyclerview);
        this.mLinearLayoutManager = new LinearLayoutManager(this);
        this.mRecyclerView.setLayoutManager(this.mLinearLayoutManager);
        this.mRecyclerView.setHasFixedSize(true);
        this.PreferenceManager = new PreferenceManager(this);

        this.textviewRatio = findViewById(R.id.textview_rat);
        this.textviewRatio.setOnClickListener(this);
        TextView textView = this.textviewRatio;
        textView.setText(getResources().getString(R.string.ratio) + ": (1:1)");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = (float) displayMetrics.widthPixels;
        this.screenHeight = (float) displayMetrics.heightPixels;
        this.animSlideUp = Constant.getAnimUp(this);
        this.animSlideDown = Constant.getAnimDown(this);
        this.fragmen_container = findViewById(R.id.fragmen_container);
        this.fragmen_container.setOnTouchListener((view, motionEvent) -> true);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();


        try {
            beginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.fragmen_container.post(() -> {
            BackgrounImageActivity.this.fragmen_container.startAnimation(BackgrounImageActivity.this.animSlideUp);
            BackgrounImageActivity.this.fragmen_container.setVisibility(View.VISIBLE);
        });

        //Load Background Images
        getBgImages();
    }

    private void getBgImages() {

        Constant.getTemplateBasedViewModel(this).getPosterBackground().observe(this, thumbBG -> {

            if (thumbBG != null) {

                MyUtils.showResponse(thumbBG);

                BackgrounImageActivity.this.loading_view.setVisibility(View.GONE);

                BackgrounImageActivity.this.thumbnailBg = thumbBG.getThumbnail_bg();

                BackgrounImageActivity.this.setupAdapter();

            }

        });


    }


    public void setupAdapter() {
        for (int i = 0; i < this.thumbnailBg.size(); i++) {
            if (this.thumbnailBg.get(i).getCategory_list().size() != 0) {
                this.snapData.add(new Snap2(1, this.thumbnailBg.get(i).getCategory_name(), this.thumbnailBg.get(i).getCategory_list(), this.thumbnailBg.get(i).getCategory_id(), this.ratio));
            }
        }
        this.loading_view.setVisibility(View.GONE);
        this.yourDataProvider = new YourDataProvider();
        this.yourDataProvider.setPosterList(this.snapData);
        if (this.snapData != null) {
            this.veticalViewAdapter = new VeticalViewAdapter(this, this.yourDataProvider.getLoadMorePosterItems(), 1);
            this.mRecyclerView.setAdapter(this.veticalViewAdapter);
            this.scrollListener = new RecyclerViewLoadMoreScroll(this.mLinearLayoutManager);
            this.scrollListener.setOnLoadMoreListener(BackgrounImageActivity.this::LoadMoreData);
            this.mRecyclerView.addOnScrollListener(this.scrollListener);
        }
    }


    public void LoadMoreData() {
        this.veticalViewAdapter.addLoadingView();
        new Handler().postDelayed(() -> {
            BackgrounImageActivity.this.veticalViewAdapter.removeLoadingView();
            BackgrounImageActivity.this.veticalViewAdapter.addData();
            BackgrounImageActivity.this.veticalViewAdapter.notifyDataSetChanged();
            BackgrounImageActivity.this.scrollListener.setLoaded();
        }, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        freeMemory();
    }

    public void freeMemory() {
        try {
            new Thread(() -> {
                try {
                    Glide.get(BackgrounImageActivity.this).clearDiskCache();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            Glide.get(this).clearMemory();
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == SELECT_PICTURE_FROM_GALLERY) {
            try {
                Uri fromFile = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                String[] split = Constant.selectedRatio.split(":");
                UCrop.of(intent.getData(), fromFile).withOptions(this.options).withAspectRatio((float) Integer.parseInt(split[0]), (float) Integer.parseInt(split[1])).start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (i2 == -1 && i == SELECT_PICTURE_FROM_CAMERA) {

            try {

                Uri fromFile2 = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                String[] split2 = Constant.selectedRatio.split(":");
                int parseInt = Integer.parseInt(split2[0]);
                int parseInt2 = Integer.parseInt(split2[1]);
                UCrop.of(FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", this.file), fromFile2).withOptions(this.options).withAspectRatio((float) parseInt, (float) parseInt2).start(this);

            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }
        if (i2 == -1 && i == 69) {

            handleCropResult(intent);

        } else if (i2 == 96) {

            try {

                UCrop.getError(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCropResult(@NonNull Intent intent) {
        try {

            this.resultUri = UCrop.getOutput(intent);

            handleCrop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int gcd(int i, int i2) {
        return i2 == 0 ? i : gcd(i2, i % i2);
    }

    public void handleCrop() {

        try {

            Intent intent = new Intent(this, ThumbnailActivity.class);
            intent.putExtra("sizeposition", this.ratio);
            intent.putExtra("loadUserFrame", true);
            intent.putExtra(Scopes.PROFILE, this.resultUri.toString());
            intent.putExtra("position", "0");
            intent.putExtra("hex", "");
            startActivity(intent);
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findViews() {
        ImageView btnBack = findViewById(R.id.btn_back);
        TextView txtTitle = findViewById(R.id.txtTitle);
        ImageView btnColorPicker = findViewById(R.id.btnColorPicker);
        ImageView btnGalleryPicker = findViewById(R.id.btnGalleryPicker);
        ImageView btnTakePicture = findViewById(R.id.btnTakePicture);
        txtTitle.setText("Background");
        btnColorPicker.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        btnColorPicker.setOnClickListener(this);
        btnGalleryPicker.setOnClickListener(this);
        btnTakePicture.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

        if (this.fragmen_container.getVisibility() != View.VISIBLE) {
            super.onBackPressed();
        } else if (this.isSizeSelected) {
            this.fragmen_container.startAnimation(this.animSlideDown);
            this.fragmen_container.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnColorPicker:
                colorPickerDialog();
                return;
            case R.id.btnGalleryPicker:
                btnGallery();
                return;
            case R.id.btnTakePicture:
                btnTakePicture();
                return;
            case R.id.btn_back:
                onBackPressed();
                return;
            case R.id.textview_rat:
                this.fragmen_container.startAnimation(this.animSlideUp);
                this.fragmen_container.setVisibility(View.VISIBLE);
                return;
            default:
        }
    }


    private void btnTakePicture() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File tmp = new StorageUtils(BackgrounImageActivity.this).getPackageStorageDir(".bgTemp");
        BackgrounImageActivity.this.file = new File(tmp.getAbsolutePath(), ".temp.jpg");
        BackgrounImageActivity backgrounImageActivity = BackgrounImageActivity.this;
        intent.putExtra("output", FileProvider.getUriForFile(backgrounImageActivity, BackgrounImageActivity.this.getApplicationContext().getPackageName() + ".provider", BackgrounImageActivity.this.file));
        BackgrounImageActivity.this.startActivityForResult(intent, BackgrounImageActivity.SELECT_PICTURE_FROM_CAMERA);
    }


    private void btnGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        BackgrounImageActivity backgrounImageActivity = BackgrounImageActivity.this;
        backgrounImageActivity.startActivityForResult(Intent.createChooser(intent, backgrounImageActivity.getString(R.string.select_picture)), BackgrounImageActivity.SELECT_PICTURE_FROM_GALLERY);
    }


    private void colorPickerDialog() {
        new AmbilWarnaDialog(this, this.bColor, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }

            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                BackgrounImageActivity.this.updateColor(i);
            }
        }).show();
    }


    public void updateColor(int i) {
        FileOutputStream fileOutputStream;
        Bitmap createBitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
        createBitmap.eraseColor(i);
        Log.e(TAG, "updateColor: ");
        try {
            File file = new File(new File(this.PreferenceManager.getString(Constant.sdcardPath) + "/bg/"), "tempcolor.png");
            Uri uri;
            fileOutputStream = new FileOutputStream(file);
            createBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            uri = Uri.fromFile(file);
            if (uri != null) {
                Uri fromFile = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                String[] split = Constant.selectedRatio.split(":");
                UCrop.of(uri, fromFile).withOptions(this.options).withAspectRatio((float) Integer.parseInt(split[0]), (float) Integer.parseInt(split[1])).start(this);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    public void onSnapFilter(int i, int i2, String str) {
        FileOutputStream fileOutputStream;
        Uri fromFile = null;

        try {
            Uri uri = null;
            if (str.equals("null")) {
                Bitmap decodeResource = BitmapFactory.decodeResource(BackgrounImageActivity.this.getResources(), 1);
                File file = new File(BackgrounImageActivity.this.PreferenceManager.getString(Constant.sdcardPath) + "/bgs" + i2 + "/");
                File file2 = new File(file, "dummy");
                if (file2.exists()) {
                    fromFile = Uri.fromFile(file2);
                } else {
                    try {
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        fileOutputStream = new FileOutputStream(file2);
                        decodeResource.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.close();
                        fromFile = Uri.fromFile(file2);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                uri = fromFile;
            } else {
                BackgrounImageActivity.this.ongetPosition(str);
            }
            if (uri != null) {
                Uri fromFile2 = Uri.fromFile(new File(BackgrounImageActivity.this.getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                String[] split = Constant.selectedRatio.split(":");
                UCrop.of(uri, fromFile2).withOptions(BackgrounImageActivity.this.options).withAspectRatio((float) Integer.parseInt(split[0]), (float) Integer.parseInt(split[1])).start(BackgrounImageActivity.this);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }


    public void ratioOptions(String str) {
        this.ratio = str;
        Constant.selectedRatio = str;
        TextView textView = this.textviewRatio;
        textView.setText(getResources().getString(R.string.ratio) + ": (" + str + ")");
        this.fragmen_container.startAnimation(this.animSlideDown);
        this.fragmen_container.setVisibility(View.GONE);
        this.isSizeSelected = true;
    }

    public void sizeOptions(String str) {
        String[] split = str.split(":");
        int parseInt = Integer.parseInt(split[0]);
        int parseInt2 = Integer.parseInt(split[1]);
        this.ratio = str;
        int gcd = gcd(parseInt, parseInt2);
        Constant.selectedRatio = "" + (parseInt / gcd) + ":" + (parseInt2 / gcd) + ":" + parseInt + ":" + parseInt2;
        TextView textView = this.textviewRatio;
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.size));
        sb.append(":");
        sb.append(parseInt);
        sb.append(" x ");
        sb.append(parseInt2);
        textView.setText(sb.toString());
        this.fragmen_container.startAnimation(this.animSlideDown);
        this.fragmen_container.setVisibility(View.GONE);
        this.isSizeSelected = true;
    }

    public void itemClickSeeMoreAdapter(ArrayList<BackgroundImage> arrayList) {
        seeMore(arrayList);
    }

    private void seeMore(ArrayList<BackgroundImage> arrayList) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        BackgroundFragment1 backgroundFragment1 = (BackgroundFragment1) supportFragmentManager.findFragmentByTag("back_category_frgm");
        if (backgroundFragment1 != null) {
            beginTransaction.remove(backgroundFragment1);
        }
        BackgroundFragment1 newInstance = BackgroundFragment1.newInstance(arrayList);
        this.mFragments.add(new WeakReference(newInstance));
        beginTransaction.setCustomAnimations(R.anim.push_left_in_no_alpha, R.anim.push_left_out_no_alpha);
        beginTransaction.add(R.id.frameContainerBackground, newInstance, "back_category_frgm");
        beginTransaction.addToBackStack("back_category_frgm");
        try {
            beginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ongetPosition(String str) {
        this.dialogIs = new ProgressDialog(this);
        this.dialogIs.setMessage(getResources().getString(R.string.plzwait));
        this.dialogIs.setCancelable(false);
        this.dialogIs.show();
        final File cacheFolder = getCacheFolder(this);
        MyApplication.getInstance().addToRequestQueue(new ImageRequest(str, bitmap -> {
            try {
                BackgrounImageActivity.this.dialogIs.dismiss();
                try {
                    File file = new File(cacheFolder, "localFileName.png");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    try {
                        Uri fromFile = Uri.fromFile(file);
                        Uri fromFile2 = Uri.fromFile(new File(BackgrounImageActivity.this.getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                        String[] split = Constant.selectedRatio.split(":");
                        UCrop.of(fromFile, fromFile2).withOptions(BackgrounImageActivity.this.options).withAspectRatio((float) Integer.parseInt(split[0]), (float) Integer.parseInt(split[1])).start(BackgrounImageActivity.this);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }, 0, 0, null, volleyError -> BackgrounImageActivity.this.dialogIs.dismiss()));
    }

    public void ongetPositions(String str) {
        BackgrounImageActivity backgrounImageActivity = BackgrounImageActivity.this;
        backgrounImageActivity.dialogIs = new ProgressDialog(backgrounImageActivity);
        BackgrounImageActivity.this.dialogIs.setMessage(BackgrounImageActivity.this.getResources().getString(R.string.plzwait));
        BackgrounImageActivity.this.dialogIs.setCancelable(false);
        BackgrounImageActivity.this.dialogIs.show();


        final File cacheFolder = backgrounImageActivity.getCacheFolder(context);

        MyApplication.getInstance().addToRequestQueue(new ImageRequest(str, bitmap -> {
            try {
                BackgrounImageActivity.this.dialogIs.dismiss();
                try {

                    File file = new File(cacheFolder, "localFileName.png");

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                    fileOutputStream.flush();
                    fileOutputStream.close();


                    try {


                        Uri fromFile = Uri.fromFile(file);
                        Uri fromFile2 = Uri.fromFile(new File(BackgrounImageActivity.this.getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                        String[] split = Constant.selectedRatio.split(":");


                        UCrop.of(fromFile, fromFile2)
                                .withOptions(BackgrounImageActivity.this.options)
                                .withAspectRatio((float) Integer.parseInt(split[0]), (float) Integer.parseInt(split[1]))
                                .start(BackgrounImageActivity.this);


                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }, 0, 0, null, volleyError -> BackgrounImageActivity.this.dialogIs.dismiss()));

    }

    public File getCacheFolder(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals("mounted")) {

            file = new StorageUtils(context).getPackageStorageDir("cachefolder");
            if (!file.isDirectory()) {
                file.mkdirs();
            }
        } else {
            file = null;
        }
        return !file.isDirectory() ? context.getCacheDir() : file;
    }
}
