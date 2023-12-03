package com.astarconcepts.brandbanao.custom.animated_video.adapters;


import static com.astarconcepts.brandbanao.utils.Constant.TEMPLATE_TYPE_VIDEO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.RenderMode;
import com.airbnb.lottie.TextDelegate;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.layer.Layer;
import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.MyApplication;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.PreviewVideoTemplate;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.model.UserItem;
import com.astarconcepts.brandbanao.room.AppDatabase;
import com.astarconcepts.brandbanao.room.entity.FavoriteList;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.LottieCompositionFactory;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.zip.ZipInputStream;

public class TemplateListAdapter extends RecyclerView.Adapter<TemplateListAdapter.MyViewHolder> {
    private final AppDatabase appDatabase;
    ArrayList<TemplateModel> templateList;
    Activity context;

    private boolean favorite = false;
    private InterstitialsAdsManager interstitialsAdsManager;
    private PreferenceManager prefManager;

    public static Bitmap bitmap;

    int layout = 0;


    public void clearData() {
        templateList.clear();
        notifyDataSetChanged();
    }

    public TemplateListAdapter(Activity context, ArrayList<TemplateModel> list) {
        this.templateList = list;
        this.context = context;
        appDatabase = AppDatabase.getDatabase(context);
        prefManager = new PreferenceManager(context);
        interstitialsAdsManager = new InterstitialsAdsManager(context);

    }

    public TemplateListAdapter(Activity context, ArrayList<TemplateModel> list, int layout) {
        this.templateList = list;
        this.layout = layout;
        this.context = context;
        appDatabase = AppDatabase.getDatabase(context);
        prefManager = new PreferenceManager(context);
        interstitialsAdsManager = new InterstitialsAdsManager(context);

    }

    public static void showLottieAnimation(Activity context, LottieAnimationView lottieAnimationView, TemplateModel templateModel, LottieAnimationView loader) {


        Log.d("templateModel", "showLottieAnimation: " + templateModel.getTemplate_type());


        String ziplink = templateModel.getZip_link_preview();

        String title = ziplink.substring(ziplink.lastIndexOf('/') + 1);

        File checkFile = new File(MyUtils.getFolderPath(context, "cacheTemplate/" + templateModel.getCode()), title);

        loader.setVisibility(View.VISIBLE);

        if (!checkFile.exists()) {

            String finalZiplink = ziplink;

            new Thread(() -> PRDownloader.download(finalZiplink, MyUtils.getFolderPath(context, "cacheTemplate/" + templateModel.getCode()), title).build()
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            loadAnimation(context, lottieAnimationView, templateModel, loader, checkFile);

                        }

                        @Override
                        public void onError(Error error) {

                            Log.d("error download ", "" + error.getServerErrorMessage());

                        }
                    })).start();

        } else {
            loadAnimation(context, lottieAnimationView, templateModel, loader, checkFile);
        }

    }

    public static void loadAnimation(Activity context, LottieAnimationView lottieAnimationView, TemplateModel templateModel, LottieAnimationView loader, File file) {

        lottieAnimationView.setRenderMode(RenderMode.HARDWARE);
        lottieAnimationView.setRepeatCount(0);

        if (templateModel != null && templateModel.getTemplate_type() != null && templateModel.getTemplate_type().contains("frame")) {

            lottieAnimationView.setFontAssetDelegate(new FontAssetDelegate() {

                @Override
                public Typeface fetchFont(String fontFamily) {

                        return Typeface.createFromAsset(context.getAssets(), "font/Roboto.ttf");
                }
            });


            lottieAnimationView.setTextDelegate(new TextDelegate(lottieAnimationView));
        }
        loadanimation2(context, lottieAnimationView, templateModel, file, loader);

    }

    @SuppressLint("RestrictedApi")
    private static void loadanimation2(Activity context, LottieAnimationView lottieAnimationView, TemplateModel templateModel, File file, LottieAnimationView loader) {

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                if (file.exists()) {
                    InputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    LottieCompositionFactory.fromZipStream(context, new ZipInputStream(bufferedInputStream), templateModel.getId()).addListener(result -> new Handler(Looper.getMainLooper()).post(() -> {

                        if (result != null) {

                            lottieAnimationView.setComposition(result);

                            if (templateModel != null && templateModel.getTemplate_type() != null && templateModel.getTemplate_type().contains("frame")) {
                                // 'templateModel' is not null and 'template_type' is not null and contains "frame"
                                lottieModification(context, templateModel, result, lottieAnimationView);
                            } else {
                                // Either 'templateModel' is null or 'template_type' is null or doesn't contain "frame"
                                lottieAnimationView.playAnimation();
                                lottieAnimationView.loop(true);
                            }



                        }

                        loader.setVisibility(View.GONE);

                    }));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public static void lottieModification(Activity context, TemplateModel model, LottieComposition result, LottieAnimationView lottieAnimationView) {
        BusinessItem businessItem = Constant.getBusinessItem(context);

        PreferenceManager preferenceManager = new PreferenceManager(context);

        UserItem userItem = Constant.getUserItem(context);

        for (Layer layer : result.getLayers()) {

            Log.d("lottieLayerNames", "lottieLayerNames: " + layer.getName());

            if (!layer.getName().equals("logo")) {


                lottieAnimationView.addValueCallback(new KeyPath(layer.getName()), LottieProperty.TEXT, frameInfo -> {

                    switch (layer.getName()) {
                        case "name":

                            if (model.getCategory().contains(Constant.BUSINESS)) {


                                return businessItem.getName();
                            } else {
                                return userItem.getUserName();
                            }

                        case "address":

                            return businessItem.getAddress();

                        case "mobile":

                            if (model.getCategory().contains(Constant.BUSINESS)) {

                                return businessItem.getPhone();
                            } else {
                                return userItem.getPhone();
                            }

                        case "website":
                            return businessItem.getWebsite();
                        case "email":

                            if (model.getCategory().contains(Constant.BUSINESS)) {

                                return businessItem.getEmail();
                            } else {
                                return userItem.getEmail();
                            }

                    }
                    return "";
                });
            }
        }
        String logo = model.getCategory().contains(Constant.BUSINESS) ? preferenceManager.getString(Constant.BUSINESS_LOGO_PATH) : preferenceManager.getString(Constant.USER_IMAGE_PATH);

        Glide.with(MyApplication.getAppContext())
                .asBitmap()
                .load(logo)
                .override(Constant.BUSINESS_LOGO_WIDTH, Constant.BUSINESS_LOGO_HEIGHT)

                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        lottieAnimationView.addValueCallback(new KeyPath("logo"), LottieProperty.IMAGE, frameInfo -> resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle when the image load is cleared
                    }
                });

        lottieAnimationView.setComposition(result);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setProgress(0);
        lottieAnimationView.loop(true);

    }


    private void openNextActivity(int position, @NonNull MyViewHolder holder) {


        MyUtils.templateModel = templateList.get(position);

        Intent intent = new Intent(context, PreviewVideoTemplate.class);

        context.startActivity(intent);

    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        View inflate;
        if (i == 0) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_view, viewGroup, false);
        } else {

            if (layout == -1) {


                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_template_adapter_home, viewGroup, false);

            } else {

                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_template_adapter_list, viewGroup, false);
            }

        }
        return new MyViewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int itemViewType = holder.getItemViewType();

        if (itemViewType == 0) {


            new AdsUtils(context).loadNativeAd(context, holder.fbPlaceHolder);

        } else if (itemViewType == 1) {

            try {

                TemplateModel templateModel = templateList.get(position);

                templateModel.setTemplate_type("templates");
                holder.downloadCount.setText("" + MyUtils.format(templateModel.getCreated()));

                showLottieAnimation(context, holder.lottieAnimationView, templateModel, holder.loading_anim);


                if (appDatabase.myDao().isFavoriteVideo(Integer.parseInt(templateModel.getId())) == 1) {

                    holder.favourite.setImageResource(R.drawable.ic_like_fill);

                } else {

                    holder.favourite.setImageResource(R.drawable.ic_favorite);
                }


                holder.ivPremium.setVisibility(templateModel.isPremium() ? View.VISIBLE : View.GONE);

                holder.lottieAnimationView.setOnTouchListener(new View.OnTouchListener() {

                    private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {


                        @Override

                        public boolean onSingleTapUp(MotionEvent e) {
                            super.onSingleTapUp(e);


                            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_test);
                            animation.setDuration(300);
                            holder.itemView.startAnimation(animation);

                            return true;
                        }


                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {


                            interstitialsAdsManager.showInterstitialAd(() -> openNextActivity(position, holder));


                            return true;
                        }
                    });

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        gestureDetector.onTouchEvent(event);


                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_test);
                        animation.setDuration(300);
                        holder.itemView.startAnimation(animation);

                        return true;
                    }

                });

                holder.favourite.setOnClickListener(view -> {

                    FavoriteList favorite = new FavoriteList();

                    TemplateModel model = templateList.get(position);
                    favorite.setTemplate_type(TEMPLATE_TYPE_VIDEO);
                    favorite.setZip_link(model.getZip_link());
                    favorite.setZip_link_preview(model.getZip_link_preview());
                    favorite.setVideo_id(Integer.parseInt(model.getId()));
                    favorite.setCategory(model.getCategory());

                    favorite.setData_html(model.getData_html());
                    favorite.setTitle(model.getTitle());
                    favorite.setViews(model.getViews());
                    favorite.setCreated(model.getCreated());
                    favorite.setPremium(model.isPremium());

                    favorite.setCode(model.getCode());
                    favorite.setResImageNum(model.getResImageNum());
                    favorite.setTemplate_json(model.getTemplate_json());

                    holder.favourite.setImageResource(R.drawable.ic_like_fill);

                    if (appDatabase.myDao().isFavoriteVideo(Integer.parseInt(model.getId())) == 1) {

                        appDatabase.myDao().deleteVideo(Integer.parseInt(model.getId()));

                        holder.favourite.setImageResource(R.drawable.ic_favorite);
                    } else {
                        holder.favourite.setImageResource(R.drawable.ic_like_fill);

                        appDatabase.myDao().addData(favorite);
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int getItemCount() {
        return templateList.size();
    }

    @Override
    public int getItemViewType(int i) {
        return this.templateList.get(i) != null ? 1 : 0;
    }

    public void setdata(ArrayList<TemplateModel> templateModels) {
        this.templateList = templateModels;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final FrameLayout fbPlaceHolder;
        public LottieAnimationView lottieAnimationView;
        TextView downloadCount;
        ImageView favourite;
        ImageView ivPremium;
        LottieAnimationView loading_anim;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            loading_anim = itemView.findViewById(R.id.loading_anim);
            favourite = itemView.findViewById(R.id.favourite);
            lottieAnimationView = itemView.findViewById(R.id.lottie);
            ivPremium = itemView.findViewById(R.id.ivPremium);
            fbPlaceHolder = itemView.findViewById(R.id.fl_adplaceholder);
            downloadCount = itemView.findViewById(R.id.downloadTextCount);
        }
    }

}
