package com.astarconcepts.brandbanao.ui.activities;

import static com.astarconcepts.brandbanao.ui.activities.PreviewActivity.getLayoutParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.AdsUtils.RewardAdsManager;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.LanguageAdapter;
import com.astarconcepts.brandbanao.adapters.DailyPostAdapter;
import com.astarconcepts.brandbanao.adapters.SubCategoryAdapter;
import com.astarconcepts.brandbanao.binding.GlideDataBinding;
import com.astarconcepts.brandbanao.custom.animated_video.activities.SearchActivity;
import com.astarconcepts.brandbanao.databinding.ActivityPersonalPostBinding;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.model.PostItem;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PaginationListener;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DailyPostActivity extends AppCompatActivity {

    ActivityPersonalPostBinding binding;
    Activity context;

    int pageCount = 1;
    LinearLayoutManager layoutManager;
    boolean loading = false;
    DailyPostAdapter adapter;
    List<PostItem> dailyPost = new ArrayList<>();
    View currentView;
    ImageView rewateBtn;
    PreferenceManager preferenceManager;
    private Dialog dialogLanguage;
    private String selectedLanguage = "";
    private String selectedCat = "-1";
    private Dialog dialogWatermarkOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        dialogWatermarkOption = new Dialog(context, R.style.MyAlertDialog);

        preferenceManager = new PreferenceManager(context);
        binding.progreee.setVisibility(View.VISIBLE);

        showLanguageDialog();

        layoutManager = new LinearLayoutManager(this);
        binding.allVideos.setLayoutManager(layoutManager);

        binding.toolbar.back.setOnClickListener(view -> finish());
        binding.toolbar.toolName.setText("Daily Post");
        binding.toolbar.toolbarIvLanguage.setOnClickListener(v -> dialogLanguage.show());
        binding.toolbar.toolbarIvLanguage.setVisibility(View.VISIBLE);

     /*   binding.allVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pastVisiblesItems, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItems = 0;
                    firstVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (firstVisibleItems > 0) {
                        pastVisiblesItems = firstVisibleItems;
                    }

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;
                            pageCount++;

                            binding.progreee.setVisibility(View.VISIBLE);

                            new Handler(Looper.getMainLooper()).postDelayed(DailyPostActivity.this::loadDataMore, 100);

                        }
                    }

                }
            }
        });*/


        binding.allVideos.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return loading;
            }

            @Override
            public void loadMoreItems() {

                loading = true;
                pageCount = pageCount+1;

                binding.progreee.setVisibility(View.VISIBLE);

                new Handler(Looper.getMainLooper()).postDelayed(DailyPostActivity.this::loadDataMore, 100);

            }
        });

        binding.refreshLayout.setOnRefreshListener(() -> {
            binding.progreee.setVisibility(View.VISIBLE);

            pageCount = 1;
            loading = false;

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                getData();

                binding.refreshLayout.setRefreshing(false);

            }, 100);


        });


        loadCategories();

        getData();


    }

    private void loadCategories() {
        List<CategoryItem> categoryItemList = new ArrayList<>();

        categoryItemList.add(new CategoryItem("-1", "All", "", false));


        Constant.getHomeViewModel(this).getCategories(Constant.CATEGORY).observe(this, categoryItems -> {

            if (categoryItems != null) {


                SubCategoryAdapter categoryAdapter = new SubCategoryAdapter(context, data -> {

                    selectedCat = data.getId();
                    pageCount = 1;
                    loading = false;
                    getData();
                }, false);

                categoryItemList.addAll(categoryItems);

                categoryAdapter.setCategories(categoryItemList);

                binding.rvCategory.setAdapter(categoryAdapter);

            }


        });


    }

    public void showLanguageDialog() {

        dialogLanguage = new Dialog(context);
        dialogLanguage.setContentView(R.layout.language_dialog);

        ImageView cancel = dialogLanguage.findViewById(R.id.cancel);
        TextView allLanguage = dialogLanguage.findViewById(R.id.tv_lang_name);

        allLanguage.setOnClickListener(view -> {

            selectedLanguage = "";
            getData();

        });

        RecyclerView rvLanguage = dialogLanguage.findViewById(R.id.rv_language);


        Constant.getHomeViewModel(this).getLanguagess().observe(this, languageItems -> {

            if (languageItems != null) {

                if (languageItems.size() > 0) {
                    rvLanguage.setLayoutManager(new GridLayoutManager(context, 2));
                    LanguageAdapter languageAdapter = new LanguageAdapter(context, (data) -> {

                        selectedLanguage = data.id;
                        getData();

                    });
                    languageAdapter.setLanguageItemList(languageItems);
                    rvLanguage.setAdapter(languageAdapter);
                } else {
                    Toast.makeText(context, "data not found", Toast.LENGTH_SHORT).show();
                }

            }


        });


        if (dialogLanguage.getWindow() != null) {

            dialogLanguage.getWindow().setAttributes(getLayoutParams(dialogLanguage));

            this.dialogLanguage.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            cancel.setOnClickListener(view -> dialogLanguage.dismiss());

        }
    }

    private void getData() {

        binding.progreee.setVisibility(View.VISIBLE);

        dailyPost.clear();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (dialogLanguage.isShowing()) {
            dialogLanguage.dismiss();
        }


        Constant.getHomeViewModel(this).getDailyPosts(pageCount, selectedCat, selectedLanguage).observe(this, postItems -> {

            if (postItems != null) {

                dailyPost.addAll(postItems);

                MyUtils.showResponse(dailyPost);

                adapter = new DailyPostAdapter(context, dailyPost, (view, posterew, postItem) -> {
                    currentView = posterew;
                    switch (view.getId()) {
                        case R.id.watermark:

                            rewateBtn = currentView.findViewById(R.id.watermark);

                            setupDialogWatermarkOption();

                            break;
                        case R.id.downloadBtn:


                            saveImage(GlideDataBinding.viewToBitmap(currentView), "download");

                            break;
                        case R.id.share_Btn:

                            saveImage(GlideDataBinding.viewToBitmap(currentView), "Share");

                            break;
                        case R.id.edit_Btn:
                            startActivity(new Intent(context, ProfileEditActivity.class));
                            break;
                    }
                });

                binding.allVideos.setAdapter(adapter);


            }

            binding.progreee.setVisibility(View.GONE);
            binding.refreshLayout.setRefreshing(false);
        });
    }


    private void loadDataMore() {

        Constant.getHomeViewModel(this).getDailyPosts(pageCount, selectedCat, selectedLanguage).observe(this, postItems -> {

            if (postItems != null) {

                dailyPost.addAll(postItems);
                adapter.setData(dailyPost);
                loading = false;

            }


            binding.progreee.setVisibility(View.GONE);
            binding.refreshLayout.setRefreshing(false);
        });
    }

    private void saveImage(Bitmap bitmap, String type) {

        String fileName = System.currentTimeMillis() + ".png";
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string.app_name)
                + File.separator + fileName;

        boolean success = false;

        if (!new File(filePath).exists()) {
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ), "/" + getResources().getString(R.string.app_name));
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        Toast.makeText(this,
                                getResources().getString(R.string.create_dir_err),
                                Toast.LENGTH_LONG).show();
                        success = false;
                    }
                }
                File file2 = new File(file.getAbsolutePath() + "/" + fileName);
              //  if (file2.exists()) {
              ///      file2.delete();
               // }
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                            bitmap.getHeight(), bitmap.getConfig());
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawColor(-1);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                    createBitmap.compress(Bitmap.CompressFormat.PNG,
                            100, fileOutputStream);
                    createBitmap.recycle();
                    fileOutputStream.flush();
                    fileOutputStream.close();



                    MediaScannerConnection.scanFile(this, new String[]{file2.getAbsolutePath()},
                            (String[]) null, (str, uri) -> {
                                StringBuilder sb = new StringBuilder();
                                sb.append("-> uri=");
                                sb.append(uri);
                                sb.append("-> FILE=");
                                sb.append(file2.getAbsolutePath());
                                Uri muri = Uri.fromFile(file2);
                            });
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    success = false;
                }

            } catch (Exception e) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (success) {
                if (type.equals("download")) {



                    Util.showToast(this, getString(R.string.image_saved));
                } else {
                    shareFileImageUri(getImageContentUri(new File(filePath)), type);
                }
            } else {
                Util.showToast(this, getString(R.string.error));
            }

        }
    }

    public Uri getImageContentUri(File imageFile) {
        return Uri.parse(imageFile.getAbsolutePath());
    }

    public void shareFileImageUri(Uri path,  String shareTo) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        switch (shareTo) {
            case "whtsapp":
                shareIntent.setPackage("com.whatsapp");
                break;
            case "fb":
                shareIntent.setPackage("com.facebook.katana");
                break;
            case "insta":
                shareIntent.setPackage("com.instagram.android");
                break;
            case "twter":
                shareIntent.setPackage("com.twitter.android");
                break;
        }
        shareIntent.setDataAndType(path, "image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, path);

        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt) + getPackageName());

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_txt) + getPackageName()));
    }


    public void setupDialogWatermarkOption() {

        dialogWatermarkOption.show();

        dialogWatermarkOption.setContentView(R.layout.dialog_layout_watermark_option);

        ImageView close = dialogWatermarkOption.findViewById(R.id.iv_close);
        LinearLayout premium = dialogWatermarkOption.findViewById(R.id.cv_no);

        Window window = dialogWatermarkOption.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ProgressBar progressBar = dialogWatermarkOption.findViewById(R.id.pb_loading);

        close.setOnClickListener(view -> dialogWatermarkOption.dismiss());

        premium.setOnClickListener(view -> startActivity(new Intent(context, SubsPlanActivity.class)));

        LinearLayout withouWatermark = dialogWatermarkOption.findViewById(R.id.cv_yes);

        withouWatermark.setOnClickListener(view -> {

            progressBar.setVisibility(View.VISIBLE);

            new RewardAdsManager(context, new RewardAdsManager.OnAdLoaded() {
                @Override
                public void onAdClosed() {

                    Toast.makeText(context, "Ad not loaded, Try Again", Toast.LENGTH_SHORT).show();
                    rewateBtn.setVisibility(View.VISIBLE);
                    dialogWatermarkOption.dismiss();

                }

                @Override
                public void onAdWatched() {

                    rewateBtn.setVisibility(View.GONE);
                  //  adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Congratulations, Remove Watermark", Toast.LENGTH_SHORT).show();
                    dialogWatermarkOption.dismiss();

                }
            });


        });

    }

}