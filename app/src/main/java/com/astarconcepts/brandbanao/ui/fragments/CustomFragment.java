package com.astarconcepts.brandbanao.ui.fragments;

import static android.app.Activity.RESULT_OK;
import static android.os.Looper.getMainLooper;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.activities.AnimatedVideoActivity;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.TemplateListAdapter;
import com.astarconcepts.brandbanao.custom.poster.activity.ThumbnailActivity;
import com.astarconcepts.brandbanao.custom.poster.adapter.PosterCategoryAdapter;
import com.astarconcepts.brandbanao.custom.poster.adapter.ThumbnailSnapAdapter;
import com.astarconcepts.brandbanao.custom.poster.fragment.SizeSelection;
import com.astarconcepts.brandbanao.custom.poster.interfaces.GetSelectSize;
import com.astarconcepts.brandbanao.databinding.FragmentCustomBinding;
import com.astarconcepts.brandbanao.ui.activities.DigitalCardActivity;
import com.astarconcepts.brandbanao.ui.activities.GreetingActivity;
import com.astarconcepts.brandbanao.ui.activities.MainActivity;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PaginationListener;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.UriUtil;
import com.astarconcepts.brandbanao.utils.Util;
import com.astarconcepts.brandbanao.viewmodel.TemplateBasedViewModel;
import com.google.android.gms.common.Scopes;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;


public class CustomFragment extends Fragment {

    FragmentCustomBinding binding;
    String ratio = "";
    Activity context;
    private int page = 1;
    private boolean isLoading = false;
    LinearLayoutManager mLinearLayoutManager;
    PosterCategoryAdapter posterCategoryAdapter;
    ThumbnailSnapAdapter snapAdapter;
    private PreferenceManager preferenceManager;
    private SizeSelection sizeSelection;
    private TemplateListAdapter listAdapter;
    TemplateBasedViewModel templateBasedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCustomBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        showShimmerManage(VISIBLE, GONE);

        preferenceManager = new PreferenceManager(context);

        templateBasedViewModel = new ViewModelProvider(this).get(TemplateBasedViewModel.class);

        mLinearLayoutManager = new LinearLayoutManager(context);

        binding.rvPoster.setLayoutManager(this.mLinearLayoutManager);

        binding.refreshLayout.setOnRefreshListener(() -> {

            try {


                binding.refreshLayout.setRefreshing(false);

                snapAdapter.clearData();
                listAdapter.clearData();
                posterCategoryAdapter.clearData();

                snapAdapter.notifyDataSetChanged();
                listAdapter.notifyDataSetChanged();
                posterCategoryAdapter.notifyDataSetChanged();

                page = 1;
                isLoading = false;


                new Handler(getMainLooper()).postDelayed(() -> {
                    getPosterList();
                    getAnimatedTemplates();

                }, 100);

                showShimmerManage(VISIBLE, GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        binding.rvPoster.addOnScrollListener(new PaginationListener(mLinearLayoutManager) {
            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void loadMoreItems() {

                page = page + 1;

                isLoading = true;

                new Handler(getMainLooper()).postDelayed(CustomFragment.this::getPosterListMore, 100);


            }
        });

        binding.photoToVideo.setOnClickListener(this::selectSize);

        binding.customPhoto.setOnClickListener(this::selectSize);

        binding.llPhotoFrame.setOnClickListener(v -> startActivity(new Intent(getContext(), GreetingActivity.class)
                .putExtra("name", "Greeting")));

        binding.digitalCard.setOnClickListener(v -> startActivity(new Intent(getContext(), DigitalCardActivity.class)));

        binding.seeMoreVideo.setOnClickListener(v -> startActivity(new Intent(getContext(), AnimatedVideoActivity.class)));

        getPosterList();
        getAnimatedTemplates();
    }

    private void getAnimatedTemplates() {


        templateBasedViewModel.getSearchedTemplates(1, "All", Constant.ANIMATED_CUSTOM_SHOW_LIMIT).observe(getViewLifecycleOwner(), templateResponse -> {

            if (templateResponse != null) {

                listAdapter = new TemplateListAdapter(context, templateResponse.msg, -1);
                binding.rvAnimated.setAdapter(listAdapter);

                showShimmerManage(GONE, VISIBLE);

            }

        });

    }


    public void getPosterList() {

        templateBasedViewModel.getPosterHome(page).observe(getViewLifecycleOwner(), thumbnailWithList -> {

            if (thumbnailWithList != null) {

                snapAdapter = new ThumbnailSnapAdapter(context, thumbnailWithList.getData());
                binding.rvPoster.setAdapter(snapAdapter);

                posterCategoryAdapter = new PosterCategoryAdapter(context, thumbnailWithList.getData());
                binding.rvPosterCat.setAdapter(posterCategoryAdapter);

                showShimmerManage(GONE, VISIBLE);
                binding.shimmerViewContainer.stopShimmer();

            }


        });


    }

    private void showShimmerManage(int gone, int visible) {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(gone);
        binding.mainContainer.setVisibility(visible);
    }

    public void getPosterListMore() {


        templateBasedViewModel.getPosterHome(page).observe(getViewLifecycleOwner(), thumbnailWithList -> {


            if (thumbnailWithList != null) {


                snapAdapter.addData(thumbnailWithList.getData());

            }
            isLoading = false;


        });


    }


    private void selectSize(View v) {
        sizeSelection = new SizeSelection(new GetSelectSize() {
            @Override
            public void ratioOptions(String str) {
                ratio = str;
                startAction(v);
            }

            @Override
            public void sizeOptions(String str) {
                String[] split = str.split(":");
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                int gcd = gcd(parseInt, parseInt2);
                ratio = "" + (parseInt / gcd) + ":" + (parseInt2 / gcd) + ":" + parseInt + ":" + parseInt2;
                startAction(v);

            }
        });
        sizeSelection.show(getChildFragmentManager(), "");
    }

    private void startAction(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.photoToVideo:
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                resultCallbackForImagetoVideo.launch(intent);
                break;
            case R.id.custom_photo:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                resultCallbackForCustom.launch(intent);
                break;

        }
    }


    private int gcd(int i, int i2) {
        return i2 == 0 ? i : gcd(i2, i % i2);
    }

    ActivityResultLauncher<Intent> resultCallbackForCustom = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (preferenceManager.getBoolean(Constant.IS_LOGIN)) {

                            String path = UriUtil.getPath(getActivity(), result.getData().getData());

                            UCrop.Options options = new UCrop.Options();
                            options.setCompressionFormat(Bitmap.CompressFormat.PNG);
                            options.setToolbarColor(getResources().getColor(R.color.white));
                            options.setToolbarWidgetColor(getResources().getColor(R.color.color_add_btn));
                            Uri fromFile = Uri.fromFile(new File(getContext().getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));

                            String[] split = ratio.split(":");

                            UCrop.of(result.getData().getData(), fromFile).withOptions(options).withAspectRatio((float) Integer.parseInt(split[0]), (float) Integer.parseInt(split[1])).start(getActivity(), CustomFragment.this);

                            if (path.endsWith(".mp4")) {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(path);
                                int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                try {
                                    retriever.release();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ratio = width + ":" + height;

                            }


                        } else {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> resultCallbackForImagetoVideo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();

                            Constant.movieImageList.clear();

                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                Constant.movieImageList.add(UriUtil.getPath(getActivity(), uri));
                            }

                            sizeSelection.dismiss();
                            String backgroundUrl = null;


                            if (Constant.movieImageList.size() >= 3) {
                                if (preferenceManager.getBoolean(Constant.IS_LOGIN)) {

                                    String imageSquare = "https://brand-up.app/uploads/1-1-instagram-1024x1024 (1).webp";
                                    String imageStory = "https://brand-up.app/uploads/1280×720.jpg";
                                    String imageHorizontal = "https://brand-up.app/uploads/169horizontal.jpg";
                                    String image43 = "https://brand-up.app/uploads/43image.png";
                                    String image34 = "https://brand-up.app/uploads/34image.png";


                                    if (ratio.contains("1:1")) {
                                        backgroundUrl = imageSquare;

                                    } else if (ratio.contains("9:16")) {
                                        backgroundUrl = imageStory;
                                    } else if (ratio.contains("16:9")) {

                                        backgroundUrl = imageHorizontal;
                                    } else if (ratio.contains("4:3")) {

                                        backgroundUrl = image43;

                                    } else if (ratio.contains("3:4")) {

                                        backgroundUrl = image34;
                                    } else {
                                        backgroundUrl = imageSquare;
                                    }

                                    Log.d("imagsss", "" + backgroundUrl);

                                    Intent intent = new Intent(getActivity(), ThumbnailActivity.class);
                                    intent.putExtra("sizeposition", ratio);
                                    intent.putExtra("backgroundImage", backgroundUrl);
                                    intent.putExtra("type", "Movie");
                                    startActivity(intent);
                                } else {
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                }
                            } else {
                                Util.showToast(getActivity(), getString(R.string.select_minimum_3_images));
                            }
                        } else {
                            Util.showToast(getActivity(), getString(R.string.select_minimum_3_images));
                        }
                    }
                }
            });


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            handleCropResult(data);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(getContext(), "Crop failed.", Toast.LENGTH_SHORT).show();
            cropError.printStackTrace();
        }
    }


    private void handleCropResult(Intent data) {

        sizeSelection.dismiss();

        Intent intent = new Intent(getActivity(), ThumbnailActivity.class);
        intent.putExtra("sizeposition", ratio);
        intent.putExtra("backgroundImage", UCrop.getOutput(data).toString());
        intent.putExtra("type", "post");
        intent.putExtra("isTamplate", 0);
        intent.putExtra("loadUserFrame", true);
        intent.putExtra(Scopes.PROFILE, UCrop.getOutput(data).toString());
        intent.putExtra("position", "0");

        startActivity(intent);

    }
}
