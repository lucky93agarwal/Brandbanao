package com.astarconcepts.brandbanao.utils;

import static com.astarconcepts.brandbanao.utils.MyUtils.unzip;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.AdsUtils.RewardAdsManager;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.poster.activity.ThumbnailActivity;
import com.astarconcepts.brandbanao.custom.poster.model.Sticker_info;
import com.astarconcepts.brandbanao.custom.poster.model.Text_infoposter;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailCo;
import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailInfo;
import com.astarconcepts.brandbanao.model.AdsModel;
import com.astarconcepts.brandbanao.ui.activities.SubsPlanActivity;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.gms.common.Scopes;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class TemplateUtils {

    public static ArrayList<Sticker_info> stickerInfoArrayList = new ArrayList<>();
    public static ArrayList<Text_infoposter> textInfoArrayList = new ArrayList<>();
    public static ArrayList<ThumbnailCo> thumbnailCos = new ArrayList<>();

    public static AdsModel model;

    public static String real_x = "";
    public static String real_y = "";
    public static String calc_width = "";
    public static String calc_height = "";
    public static Activity context;
    public static ProgressBar progressBar;
    public static InterstitialsAdsManager interstitialAds;
    private static ArrayList<String> urlss = new ArrayList<>();
    private static ArrayList<String> fontNames = new ArrayList<>();
    private static String bgUrl = null;
    private static ThumbnailCo thumbnailCo;
    private static Dialog dialog;
    private static int postId = 0;
    private static int downloadId = 0;
    private static Dialog dialogPremium;
    private static ProgressBar progressBarPremium;
    private static PreferenceManager preferenceManager;
    private static File filePath;

    public TemplateUtils(Activity context) {

        this.context = context;
        setDialogPremium();
        interstitialAds = new InterstitialsAdsManager(context);
        preferenceManager = new PreferenceManager(context);
    }

    public static void openPosterActivity(int id) {

        postId = id;
        setupProgress(context);
        loadPoster();

    }


    public static void setupProgress(Activity context) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_download);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.show();

        new AdsUtils(context).loadNativeAd(context, dialog.findViewById(R.id.fl_adplaceholder));

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> {

            PRDownloader.cancel(downloadId);
            dismissDialog();

        });

        progressBar = dialog.findViewById(R.id.progressBar);

        progressBar.setProgress(0);
        progressBar.setMax(100);

    }

    public static void dismissDialog() {

        if (dialog != null) {

            dialog.dismiss();

        }

    }


    public static void loadPoster() {

        if (thumbnailCos != null) {
            thumbnailCos.clear();
        }

        if (stickerInfoArrayList != null) {

            stickerInfoArrayList.clear();
        }

        if (textInfoArrayList != null) {

            textInfoArrayList.clear();

        }


        //Get all Poster Info

        ApiClient.getApiDataService().getPosterData(postId).enqueue(new Callback<ThumbnailInfo>() {
            @Override
            public void onResponse(Call<ThumbnailInfo> call, retrofit2.Response<ThumbnailInfo> response) {

                if (response.isSuccessful() && response.body() != null) {

                    posterData(response.body());

                }

            }

            @Override
            public void onFailure(Call<ThumbnailInfo> call, Throwable t) {

                t.printStackTrace();
            }
        });

    }

    public static void posterData(ThumbnailInfo thumbnailInfo) {

        try {

            thumbnailCos.clear();
            urlss.clear();

            fontNames.clear();
            textInfoArrayList.clear();
            stickerInfoArrayList.clear();

            // Add Poster Creation Count

            Constant.addTemplateCreationCount(postId, Constant.TEMPLATE_TYPE_POSTER);

            thumbnailCos = thumbnailInfo.getData();

            thumbnailCo = thumbnailCos.get(0);

            filePath = new File(MyUtils.getFolderPath(context, context.getString(R.string.poster_folder)), thumbnailCo.getTitle());

            if (thumbnailCo.isPremium()) {

                dialogPremium.show();

            } else {

                if (dialog != null)
                    dialog.show();


                // Check if exist

                if (filePath.exists()) {


                    processTemplateData();

                } else {

                    // Check if exist -> Download the template if not Exits
                    downloadTemplate();

                }

            }


        } catch (Exception e) {
            e.printStackTrace();

            dismissDialog();

        }

    }

    public static void setDialogPremium() {


        dialogPremium = new Dialog(context, R.style.MyAlertDialog);
        dialogPremium.setContentView(R.layout.dialog_layout_watermark_option);
        dialogPremium.setCancelable(false);

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

            context.startActivity(new Intent(context, SubsPlanActivity.class));
            dialogPremium.dismiss();

        });

        LinearLayout yes = dialogPremium.findViewById(R.id.cv_yes);

        yes.setOnClickListener(view -> {

            progressBarPremium.setVisibility(View.VISIBLE);

            Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show();

            new RewardAdsManager(context, new RewardAdsManager.OnAdLoaded() {
                @Override
                public void onAdClosed() {

                    progressBarPremium.setVisibility(View.GONE);
                    dialogPremium.dismiss();
                    Toast.makeText(context, "Failed, Ad Not Loaded.", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onAdWatched() {

                    progressBarPremium.setVisibility(View.GONE);
                    dialogPremium.dismiss();

                    if (dialog != null)
                        dialog.show();

                    if (filePath.exists()) {

                        dismissDialog();

                        processTemplateData();

                    } else {

                        downloadTemplate();

                    }

                    Toast.makeText(context, "Premium Unlocked!", Toast.LENGTH_SHORT).show();

                }
            });

        });

    }

    private static void downloadTemplate() {


        downloadId = PRDownloader.download(thumbnailCo.getPost_zip(), MyUtils.getFolderPath(context, context.getString(R.string.poster_folder)), thumbnailCo.getTitle() + ".zip").build()
                .setOnProgressListener(progress -> progressBar.setProgress((int) (progress.currentBytes * 100 / progress.totalBytes))).start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {


                        try {

                            unzip(new File(MyUtils.getFolderPath(context, context.getString(R.string.poster_folder)), thumbnailCo.getTitle() + ".zip"), new File(MyUtils.getFolderPath(context, context.getString(R.string.poster_folder))), path -> processTemplateData());

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(Error error) {


                        PRDownloader.cancel(downloadId);
                        dismissDialog();

                    }
                });


    }

    private static void processTemplateData() {


        new Thread(() -> {
            try {

                progressBar.setProgress(100);

                String filePath1 = new File(MyUtils.getFolderPath(context, context.getString(R.string.poster_folder)), thumbnailCo.getTitle()).getAbsolutePath();

                JSONObject jsonObject = new JSONObject(MyUtils.readFromFile(new File(filePath1, "json/" + thumbnailCo.getTitle() + ".json").getAbsolutePath()));

                JSONArray jsonArrayLayers = jsonObject.getJSONArray("layers");

                for (int i = 0; i < jsonArrayLayers.length(); i++) {

                    JSONObject jsonObject1 = jsonArrayLayers.getJSONObject(i);
                    parseJson(i, jsonObject1, filePath1);

                }

                urlss.add(bgUrl);

                for (int i = 0; i < stickerInfoArrayList.size(); i++) {

                    urlss.add(stickerInfoArrayList.get(i).getSt_image());


                    if (i == 0) {
                        (thumbnailCos.get(i)).setBack_image(urlss.get(i));
                    } else {
                        (stickerInfoArrayList.get(i - 1)).setSt_image(urlss.get(i));
                    }

                }

                MyUtils.showResponse(stickerInfoArrayList);

                File file = new File(Configure.GetFileDir(context).getPath() + File.separator + "font");

                // find fonts and copy to font directory.

                for (int i1 = 0; i1 < fontNames.size(); i1++) {

                    try {


                        File sourceFile = new File(filePath1, "fonts/" + fontNames.get(i1) + ".ttf");

                        File dest = new File(file.getPath(), fontNames.get(i1) + ".ttf");

                        if (sourceFile.exists()) {

                            FileUtils.copyFile(sourceFile, dest);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                new Handler(Looper.getMainLooper()).post(() -> interstitialAds.showInterstitialAd(() -> {

                    preferenceManager.setBoolean(Constant.ENABLE, true);

                    gotoEditorActivity(context, postId);
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


    }

    private static void gotoEditorActivity(Activity context, int postId) {

        dismissDialog();

        Intent intent = new Intent(context, ThumbnailActivity.class);
        intent.putParcelableArrayListExtra("template", thumbnailCos);
        intent.putParcelableArrayListExtra("text", textInfoArrayList);
        intent.putParcelableArrayListExtra("sticker", stickerInfoArrayList);
        intent.putExtra(Scopes.PROFILE, "Background");
        intent.putExtra("backgroundImage", bgUrl);
        intent.putExtra("cat_id", 1);
        intent.putExtra("pos_id", postId);

        intent.putExtra("isTamplate", 1);
        intent.putExtra("loadUserFrame", false);
        intent.putExtra("sizeposition", thumbnailCos.get(0).getTemplate_w_h_ratio());
        intent.putExtra("Temp_Type", "MY_TEMP");
        context.startActivity(intent);
    }

    public static void parseJson(int i, JSONObject jsonObject1, String filePath1) throws Exception {

        String type = jsonObject1.getString("type");


        String width = jsonObject1.getString("width");
        String height = jsonObject1.getString("height");

        // editable 0 = not editable & 1 = editable;

        String x = jsonObject1.getString("x");
        String y = jsonObject1.getString("y");

        real_x = x;
        real_y = y;


        calc_width = "";
        calc_height = "";

        int template_real_width = Integer.parseInt(thumbnailCo.getTemplate_w_h_ratio().split(":")[0]);
        int template_real_height = Integer.parseInt(thumbnailCo.getTemplate_w_h_ratio().split(":")[1]);


        if (i == 0) {

            calc_width = width;
            calc_height = height;

        } else {

            real_x = String.valueOf(((int) Math.round(Float.parseFloat(x)) * 100) / template_real_width);
            real_y = String.valueOf((int) Math.round(Float.parseFloat(y) * 100) / template_real_height);


            calc_width = String.valueOf(Integer.parseInt(width) * 100 / template_real_width + Integer.parseInt(real_x));
            calc_height = String.valueOf(Integer.parseInt(height) * 100 / template_real_height + Integer.parseInt(real_y));


        }

        String rotation = "0";

        if (type.contains("image")) {

            int editable = jsonObject1.has("editable")?0:1;

            if (jsonObject1.has("rotation")) {
                rotation = jsonObject1.getString("rotation");
            }

            String stickerUrl = filePath1 + File.separator + jsonObject1.getString("src").replace("../", "");


            // Bg
            if (i == 0) {

                bgUrl = stickerUrl;

            } else {


                Sticker_info info = new Sticker_info();

                info.setSticker_id(String.valueOf(i));

                info.setSt_image(stickerUrl);
                info.setSt_order(String.valueOf(i));
                info.setSt_height(calc_height);
                info.setSt_width(calc_width);
                info.setSt_x_pos(real_x);
                info.setSt_y_pos(real_y);
                info.setEditable(editable);
                info.setSt_rotation(rotation);
                stickerInfoArrayList.add(info);

            }
        } else if (type.contains("text")) {

            String color = jsonObject1.getString("color");
            String font = jsonObject1.getString("font");

            String text = jsonObject1.getString("text");
            String size = jsonObject1.getString("size");

            if (!jsonObject1.has("rotation")) {

                jsonObject1.put("size", (int) Math.round(Integer.parseInt(size) +Integer.parseInt(size)*0.5));
                jsonObject1.put("y",  (int) Math.round(Integer.parseInt(y) +Integer.parseInt(y)* 0.002));


                y = jsonObject1.getString("y");

                size = jsonObject1.getString("size");

                String cal_size_height = String.valueOf(Integer.parseInt(size) - Integer.parseInt(height)).replace("-", "");

                String cal_real_y = String.valueOf(Integer.parseInt(y) - Integer.parseInt(cal_size_height));

                real_y = String.valueOf((int) Math.round(Float.parseFloat(cal_real_y) * 100) / template_real_height);

                calc_height = String.valueOf(Integer.parseInt(size) * 100 / template_real_height + Integer.parseInt(real_y));

            }


            fontNames.add(font);


            Text_infoposter text_infoposter = new Text_infoposter();
            text_infoposter.setText_id(String.valueOf(i));

            text_infoposter.setText(text);
            text_infoposter.setTxt_height(calc_height);
            text_infoposter.setTxt_width(calc_width);
            text_infoposter.setTxt_x_pos(real_x);
            text_infoposter.setTxt_y_pos(real_y);
            text_infoposter.setTxt_rotation(rotation);
            text_infoposter.setTxt_color(color.replace("0x", "#"));
            text_infoposter.setTxt_order("" + i);
            text_infoposter.setFont_family(font);

            textInfoArrayList.add(text_infoposter);

        }


    }

    public interface OnUnZipListener {
        void onZip(String path);
    }


}
