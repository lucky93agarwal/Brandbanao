package com.astarconcepts.brandbanao.custom.poster.movie;

import android.widget.Toast;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.poster.moviewidget.FilterItem;
import com.astarconcepts.brandbanao.custom.poster.moviewidget.SaveVideoResponse;
import com.astarconcepts.brandbanao.custom.poster.moviewidget.TransferItem;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.hw.photomovie.PhotoMovie;
import com.hw.photomovie.PhotoMovieFactory;
import com.hw.photomovie.PhotoMoviePlayer;
import com.hw.photomovie.model.PhotoData;
import com.hw.photomovie.model.PhotoSource;
import com.hw.photomovie.model.SimplePhotoData;
import com.hw.photomovie.record.GLMovieRecorder;
import com.hw.photomovie.render.GLSurfaceMovieRenderer;
import com.hw.photomovie.render.GLTextureMovieRender;
import com.hw.photomovie.render.GLTextureView;
import com.hw.photomovie.timer.IMovieTimer;
import com.hw.photomovie.util.MLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangwei on 2018/9/9.
 */
public class DemoPresenter implements IMovieTimer.MovieListener{

    private IDemoView mDemoView;
    private PhotoMovie mPhotoMovie;
    private PhotoMoviePlayer mPhotoMoviePlayer;
    private GLSurfaceMovieRenderer mMovieRenderer;
    private String mMusicUri = "";
    private PhotoMovieFactory.PhotoMovieType mMovieType = PhotoMovieFactory.PhotoMovieType.HORIZONTAL_TRANS;

    public void attachView(IDemoView demoView) {
        mDemoView = demoView;
        initMoviePlayer();
    }

    private void initMoviePlayer() {
        final GLTextureView glTextureView = mDemoView.getGLView();

        mMovieRenderer = new GLTextureMovieRender(glTextureView);
        addWaterMark();
        mPhotoMoviePlayer = new PhotoMoviePlayer(mDemoView.getActivity().getApplicationContext());
        mPhotoMoviePlayer.setMovieRenderer(mMovieRenderer);
        mPhotoMoviePlayer.setMovieListener(this);
        mPhotoMoviePlayer.setLoop(true);

        mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
            @Override
            public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
            }

            @Override
            public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
//                mDemoView.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
                mPhotoMoviePlayer.start();
            }

            @Override
            public void onError(PhotoMoviePlayer moviePlayer) {
                MLog.i("onPrepare", "onPrepare error");
            }
        });
    }

    private void addWaterMark(){
//        Bitmap waterMark = BitmapFactory.decodeResource(mDemoView.getActivity().getResources(),R.drawable.watermark);
//        DisplayMetrics displayMetrics = mDemoView.getActivity().getResources().getDisplayMetrics();
//        mMovieRenderer.setWaterMark(waterMark,new RectF(displayMetrics.widthPixels-waterMark.getWidth(),0,displayMetrics.widthPixels,waterMark.getHeight()),0.5f);
//
//        mMovieRenderer.setWaterMark("Watermark",40, Color.argb(100,255,0,0),100,100);
    }

    private void startPlay(PhotoSource photoSource) {
        mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource, mMovieType);
        mPhotoMoviePlayer.setDataSource(mPhotoMovie);
        mPhotoMoviePlayer.prepare();
    }

    public void onFilterSelect(FilterItem item) {
        mMovieRenderer.setMovieFilter(item.initFilter());
    }


    public void onTransferSelect(TransferItem item) {
        mMovieType = item.type;
        mPhotoMoviePlayer.stop();
        mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(mPhotoMovie.getPhotoSource(), mMovieType);
        mPhotoMoviePlayer.setDataSource(mPhotoMovie);
        mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
            @Override
            public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
            }

            @Override
            public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
                mPhotoMoviePlayer.start();
            }

            @Override
            public void onError(PhotoMoviePlayer moviePlayer) {
                MLog.i("onPrepare", "onPrepare error");
            }
        });
        mPhotoMoviePlayer.prepare();
    }

    public void setMusic(String uri) {
        mMusicUri = uri;
    }


    public void saveVideo(SaveVideoResponse saveVideoResponse) {
        mPhotoMoviePlayer.pause();

        final long startRecodTime = System.currentTimeMillis();
        final GLMovieRecorder recorder = new GLMovieRecorder(mDemoView.getActivity());
        final File file = initVideoFile();
        GLTextureView glTextureView = mDemoView.getGLView();
        int bitrate = glTextureView.getWidth() * glTextureView.getHeight() > 1000 * 1500 ? 8000000 : 4000000;
        recorder.configOutput(glTextureView.getWidth(), glTextureView.getHeight(), bitrate, 30, 1, file.getAbsolutePath());
        //生成一个全新的MovieRender，不然与现有的GL环境不一致，相互干扰容易出问题
        PhotoMovie newPhotoMovie = PhotoMovieFactory.generatePhotoMovie(mPhotoMovie.getPhotoSource(), mMovieType);
        GLSurfaceMovieRenderer newMovieRenderer = new GLSurfaceMovieRenderer(mMovieRenderer);
        newMovieRenderer.setPhotoMovie(newPhotoMovie);
        if(!mMusicUri.isEmpty()) {
            recorder.setMusic(mMusicUri);
        }
        recorder.setDataSource(newMovieRenderer);
        recorder.startRecord(new GLMovieRecorder.OnRecordListener() {
            @Override
            public void onRecordFinish(boolean success) {
                File outputFile = file;
                long recordEndTime = System.currentTimeMillis();
                MLog.i("Record", "record:" + (recordEndTime - startRecodTime));
                if (success) {
                    saveVideoResponse.onVideoSAve(outputFile.getAbsolutePath());
                } else {
                    Toast.makeText(mDemoView.getActivity().getApplicationContext(), "com.hw.photomovie.record error!", Toast.LENGTH_LONG).show();
                }
                if(recorder.getAudioRecordException()!=null){
                    Toast.makeText(mDemoView.getActivity().getApplicationContext(), "record audio failed:"+recorder.getAudioRecordException().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRecordProgress(int recordedDuration, int totalDuration) {
//                dialog.setProgress((int) (recordedDuration / (float) totalDuration * 100));
            }
        });
    }

    private File initVideoFile() {
        File dir = new File(MyUtils.getAppFolder(mDemoView.getActivity()));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.exists()) {
            dir = mDemoView.getActivity().getCacheDir();
        }
        return new File(dir, String.format(mDemoView.getActivity().getString(R.string.app_name)+"%s.mp4",""+System.currentTimeMillis()));
    }

    public void onPause() {
        mPhotoMoviePlayer.pause();
    }

    public void onResume() {
        mPhotoMoviePlayer.start();
    }

    public void onPhotoPick(ArrayList<String> photos) {
        List<PhotoData> photoDataList = new ArrayList<PhotoData>(photos.size());
        for (String path : photos) {
            PhotoData photoData = new SimplePhotoData(mDemoView.getActivity(), path, PhotoData.STATE_LOCAL);
            photoDataList.add(photoData);
        }
        PhotoSource photoSource = new PhotoSource(photoDataList);
        if (mPhotoMoviePlayer == null) {
            startPlay(photoSource);
        } else {
            mPhotoMoviePlayer.stop();
            mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource, PhotoMovieFactory.PhotoMovieType.HORIZONTAL_TRANS);
            mPhotoMoviePlayer.setDataSource(mPhotoMovie);
            mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
                @Override
                public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
                }

                @Override
                public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {

                    mPhotoMoviePlayer.start();
                }

                @Override
                public void onError(PhotoMoviePlayer moviePlayer) {
                    MLog.i("onPrepare", "onPrepare error");
                }
            });
            mPhotoMoviePlayer.prepare();
        }
    }

    @Override
    public void onMovieUpdate(int elapsedTime) {

    }

    @Override
    public void onMovieStarted() {

    }

    @Override
    public void onMoviedPaused() {

    }

    @Override
    public void onMovieResumed() {

    }

    @Override
    public void onMovieEnd() {

    }
}
