package com.astarconcepts.brandbanao.custom.animated_video.fragment;

import static android.os.Looper.getMainLooper;
import static android.provider.MediaStore.MediaColumns.ARTIST;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DURATION;
import static android.provider.MediaStore.MediaColumns.TITLE;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static com.astarconcepts.brandbanao.utils.Constant.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.SubCategoryAdapter;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.animated_video.activities.VideoEditorActivity;
import com.astarconcepts.brandbanao.custom.animated_video.adapters.AudioAdapter;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelAudio;
import com.astarconcepts.brandbanao.custom.poster.activity.ThumbnailActivity;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PaginationListener;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.material.slider.RangeSlider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicFragment extends Fragment {

    ArrayList<ModelAudio> audioArrayList;
    AudioAdapter adapter;
    LottieAnimationView progressBar;
    RangeSlider rangeSlider;
    TextView musicTitle;
    TextView musicDuration;
    ImageView ivImageView;
    AppCompatImageView dlCancel;
    LinearLayout dlSelect;
    Dialog cutterDialog;
    MediaPlayer mediaPlayer;
    int startTrim = 0;
    int endTrim = 0;
    Handler mHandler;
    Runnable monitor;
    ImageView back;
    private Activity context;
    private RecyclerView rvAudiList, rvCategory;
    private EditText etSearch;

    PreferenceManager preferenceManager;

    private LinearLayoutManager linearLayoutManager;

    boolean isFromOnline = false;
    private Integer page = 1;
    private Integer cardId = -1;
    private String selectedLanguage = "-1";
    boolean loading = false;

    public static MusicFragment newInstance(boolean isFromonline) {

        Bundle args = new Bundle();

        args.putBoolean("is_from_online", isFromonline);

        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void loadCategories() {

        rvCategory.setVisibility(View.VISIBLE);
        List<CategoryItem> categoryItemList = new ArrayList<>();

        categoryItemList.add(new CategoryItem("-1", "All", "", false));


        Constant.getHomeViewModel(this).getCategories("music_category").observe(getViewLifecycleOwner(), categoryItems -> {

            if (categoryItems != null) {


                SubCategoryAdapter categoryAdapter = new SubCategoryAdapter(context, data -> {
                    cardId = Integer.valueOf(data.getId());
                    page = 1;
                    loading = false;
                    loadData();
                }, false);

                categoryItemList.addAll(categoryItems);

                categoryAdapter.setCategories(categoryItemList);

                rvCategory.setAdapter(categoryAdapter);

            }


        });


    }

    public void loadData() {
        progressBar.setVisibility(View.VISIBLE);

        audioArrayList = new ArrayList<>();

        ApiClient.getApiDataService().getAllMusic(page, cardId, selectedLanguage).enqueue(new Callback<List<ModelAudio>>() {

            @Override
            public void onResponse(Call<List<ModelAudio>> call, Response<List<ModelAudio>> response) {

                if (response.isSuccessful() && response.body() != null) {


                    MyUtils.showResponse(response.body());

                    audioArrayList.addAll(response.body());

           /*
                    for (int i = 0; i < response.body().size(); i++) {

                        ModelAudio model = response.body().get(i);

                        if (i % preferenceManager.getInt(Constant.NATIVE_AD_COUNT) == 0 && i != 0 && !preferenceManager.getBoolean(Constant.IS_SUBSCRIBE)) {
                            audioArrayList.add(null);
                        }


                        audioArrayList.add(model);

                        adapter = new AudioAdapter(context, audioArrayList);
                       rvAudiList.setAdapter(adapter);
                       // Toast.makeText(context, ""+response.body().size(), Toast.LENGTH_SHORT).show();
                    }*/

                    setUpAdapter();

                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<ModelAudio>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void setUpAdapter() {


        adapter = new AudioAdapter(context, audioArrayList);
        rvAudiList.setAdapter(adapter);

        adapter.setOnItemClickListener((pos, v) -> {

            ModelAudio audio = audioArrayList.get(pos);

            if (mediaPlayer != null) {
                mediaPlayer.pause();
                ivImageView.setImageResource(R.drawable.ic__play_circle);
            }


            if (isFromOnline) {


                ProgressDialog dialog = new ProgressDialog(context);

                dialog.setMessage("Downloading...");
                dialog.setCancelable(false);


                String file = MyUtils.getFolderPath(context, "audio") + "/" + audio.getAudioTitle() + ".mp3";

                if (!new File(file).exists()) {

                    dialog.show();


                    PRDownloader.download(audio.getAudioUrl(), MyUtils.getFolderPath(context, "audio"), audio.getAudioTitle() + ".mp3").build()
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {

                                    String file = MyUtils.getFolderPath(context, "audio") + "/" + audio.getAudioTitle() + ".mp3";

                                    audio.setaudioDuration(getDuration(new File(file)));
                                    audio.setaudioUri(Uri.parse(file));
                                    adapter.updateItems(audio, pos);

                                    dialog.dismiss();

                                    setupMusicDialog(pos);

                                }

                                @Override
                                public void onError(Error error) {

                                    dialog.dismiss();

                                    Toast.makeText(context, "Download Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {

                    audio.setaudioDuration(getDuration(new File(file)));
                    audio.setaudioUri(Uri.parse(file));
                    adapter.updateItems(audio, pos);

                    dialog.dismiss();

                    setupMusicDialog(pos);

                }


            } else {


                setupMusicDialog(pos);

            }


        });
    }

    public void loadMore() {
        ApiClient.getApiDataService().getAllMusic(page, cardId, selectedLanguage).enqueue(new Callback<List<ModelAudio>>() {

            @Override
            public void onResponse(Call<List<ModelAudio>> call, Response<List<ModelAudio>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (!response.body().isEmpty()) {

                        for (int i = 0; i < response.body().size(); i++) {

                            ModelAudio model = response.body().get(i);

                            if (i % preferenceManager.getInt(Constant.NATIVE_AD_COUNT) == 0 && i != 0 && !preferenceManager.getBoolean(Constant.IS_SUBSCRIBE)) {
                                audioArrayList.add(null);
                            }
                            audioArrayList.add(model);
                        }


                        adapter.updateList(audioArrayList);
                    }

                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<ModelAudio>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private static String getDuration(File file) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return (durationStr);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            isFromOnline = getArguments().getBoolean("is_from_online");
        }

        return inflater.inflate(R.layout.fragment_offline_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        context = getActivity();

        preferenceManager = new PreferenceManager(context);

        linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        rvAudiList.setLayoutManager(linearLayoutManager);

        setupMusicCutterDialog();

        if (isFromOnline) {

            loadData();

            loadCategories();

        } else {

            audioArrayList = getAudioFiles();

            setUpAdapter();

        }

        mHandler = new Handler(getMainLooper());


        if (isFromOnline) {

            rvAudiList.addOnScrollListener(new PaginationListener(linearLayoutManager) {
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

                    page++;
                    loading = true;

                    progressBar.setVisibility(View.VISIBLE);

                    new Handler(getMainLooper()).postDelayed(MusicFragment.this::loadMore, 100);

                }
            });

        }


        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {


            if (i == EditorInfo.IME_ACTION_SEARCH) {

                if (etSearch.getText() != null && !etSearch.getText().toString().isEmpty()) {

                    filter(etSearch.getText().toString());

                } else {
                    Toast.makeText(context, "Please enter text", Toast.LENGTH_SHORT).show();
                }


            }

            return false;
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //code
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.toString() != null) {
                    filter(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //code
            }
        });


    }

    private void setupMusicDialog(int pos) {

        stopMusic();

        musicTitle.setText(audioArrayList.get(pos).getaudioTitle());
        musicDuration.setText(timerConversion(Long.parseLong(audioArrayList.get(pos).getaudioDuration())));

        startTrim = 0;

        endTrim = Integer.parseInt(audioArrayList.get(pos).getaudioDuration());

        rangeSlider.setValueFrom(0);
        rangeSlider.setValueTo(endTrim);
        rangeSlider.setValues(1F, Float.valueOf(endTrim));


        rangeSlider.addOnChangeListener((slider, value, fromUser) -> {

            List<Float> values = slider.getValues();
            Float start = values.get(0);
            Float end = values.get(1);

            if (fromUser) {
                startTrim = Math.round(start);
                mediaPlayer.seekTo(startTrim);
                endTrim = Math.round(end);
            }
        });

        mediaPlayer = new MediaPlayer();

        try {

            mediaPlayer.setDataSource(context, audioArrayList.get(pos).getaudioUri());
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

        cutterDialog.show();


        mediaPlayer.start();

        if (mediaPlayer.isPlaying()) {

            musicDuration.setText(timerConversion(mediaPlayer.getCurrentPosition()));
        }

        ivImageView.setImageResource(R.drawable.pause);

        dlCancel.setOnClickListener(view1 -> {

            cutterDialog.dismiss();

            pauseMediaPlayer();


        });
        dlSelect.setOnClickListener(view1 -> {
            cutterDialog.dismiss();
            String trimOutDir = context.getCacheDir() + "/.tempSound";
            File f = new File(trimOutDir);
            if (!f.exists()) {
                f.mkdirs();
            }
            int dur = endTrim - startTrim;
            mediaPlayer.pause();

            trimAudio(audioArrayList.get(pos).getaudioUri().getPath(), timerConversionFfmpeg(startTrim), timerConversionFfmpeg(dur), trimOutDir + "/trimmedAudio" + System.currentTimeMillis() + ".mp3");

        });
    }

    void filter(String text) {
        ArrayList<ModelAudio> temp = new ArrayList();
        for (ModelAudio d : audioArrayList) {

            if (d != null) {

                if (d.getaudioTitle().contains(text)) {
                    temp.add(d);
                }
            }

        }
        adapter.updateList(temp);
    }

    private void initView(View view) {

        rvAudiList = view.findViewById(R.id.rv_songList);
        rvCategory = view.findViewById(R.id.rv_category);
        progressBar = view.findViewById(R.id.loading_view);

        back = view.findViewById(R.id.back);
        etSearch = view.findViewById(R.id.et_search);
    }

    private void setupMusicCutterDialog() {

        cutterDialog = new Dialog(context);
        cutterDialog.setContentView(R.layout.dailog_audio_cut);

        cutterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Window window = cutterDialog.getWindow();

        Handler handler = new Handler(getMainLooper());


        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        rangeSlider = cutterDialog.findViewById(R.id.rangeSeekBar1);

        musicTitle = cutterDialog.findViewById(R.id.music_title);
        musicDuration = cutterDialog.findViewById(R.id.music_duration);
        musicDuration.setText("00:00");
        ivImageView = cutterDialog.findViewById(R.id.dl_playpause);
        dlCancel = cutterDialog.findViewById(R.id.cancel);
        dlSelect = cutterDialog.findViewById(R.id.dl_select);

        ivImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        ivImageView.setImageResource(R.drawable.pause);
                        monitor = new Runnable() {
                            @Override
                            public void run() {

                                try{
                                    musicDuration.setText(timerConversion(mediaPlayer.getCurrentPosition()));
                                    if (mediaPlayer.getCurrentPosition() > endTrim) {
                                        mediaPlayer.pause();
                                        ivImageView.setImageResource(R.drawable.ic__play_circle);
                                        mHandler.removeCallbacks(monitor);
                                        mediaPlayer.seekTo(startTrim);
                                    } else {
                                        mHandler.postDelayed(this, 500);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }




                            }
                        };
                        mHandler.postDelayed(monitor, 500);
                    } else {
                        mHandler.removeCallbacks(monitor);
                        mediaPlayer.pause();
                        ivImageView.setImageResource(R.drawable.ic__play_circle);
                    }
                }
            }
        });

    }

    private void pauseMediaPlayer() {

        stopMusic();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopMusic();
    }

    @Override
    public void onResume() {
        super.onResume();
        stopMusic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopMusic();
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        stopMusic();
    }

    public void trimAudio(String path, String start, String end, String outputPath) {
        String[] cmd = new String[]{"-ss", start + "", "-i", path, "-t", end + "", "-c", "copy", outputPath};
        FFmpeg.executeAsync(cmd, (executionId, returnCode) -> {

            if (returnCode == RETURN_CODE_SUCCESS) {
                VideoEditorActivity.musicPath = outputPath;
                context.setResult(RESULT_OK);

                stopMusic();

                try {

                    if (getActivity().getClass() == ThumbnailActivity.class) {


                        ((ThumbnailActivity) getActivity()).closeMusicFragment(outputPath);


                    } else {

                        context.finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        Config.enableStatisticsCallback(statistics -> Log.d("FFMPEG", "apply: " + statistics));


    }

    public String timerConversion(long value) {


        String audioTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {

            audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);

        } else {

            audioTime = String.format("%02d:%02d", mns, scs);

        }
        return audioTime;
    }

    public String timerConversionFfmpeg(long value) {

        String audioTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);

        return audioTime;
    }

    public ArrayList<ModelAudio> getAudioFiles() {

        ArrayList<ModelAudio> audioArrayList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";

        Cursor cursor = contentResolver.query(uri, null, null, null, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {

            do {

                String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));

                String artist = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST));

                String duration = cursor.getString(cursor.getColumnIndexOrThrow(DURATION));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(DATA));

                ModelAudio modelAudio = new ModelAudio();
                modelAudio.setaudioTitle(title);
                modelAudio.setaudioArtist(artist);
                modelAudio.setaudioUri(Uri.parse(url));
                modelAudio.setaudioDuration(duration);
                audioArrayList.add(modelAudio);


            } while (cursor.moveToNext());

        }

        progressBar.setVisibility(View.GONE);

        return audioArrayList;
    }

}

