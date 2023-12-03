package com.astarconcepts.brandbanao.Recorder.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import android.view.Surface;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;


public class RenderEngine {
    private static final int FRAMES_PER_SECOND = 30;
    private static final int IFRAME_INTERVAL = 30;
    private static final String MIME_TYPE = "video/avc";
    private static final String TAG = "from_created";
    private static final boolean VERBOSE = true;
    public static int BIT_RATE = 8000000;
    public static int FRAME_RATE = 60;
    public static int HEIGHT = 1080;
    public static int WIDTH = 1920;

    public RenderEngienInterface interfaceRenderEngine;
    public LottieAnimationView mainparent;
    private MediaCodec.BufferInfo mBufferInfo;
    private MediaCodec mEncoder;
    private long mFakePts;
    private Surface mInputSurface;
    private MediaMuxer mMuxer;
    private boolean mMuxerStarted;
    private int mTrackIndex;
    private int progress;

    public void init(File file) {
    }

    public void setInterfaceRenderEngine(RenderEngienInterface interfaceRenderEngine2) {
        this.interfaceRenderEngine = interfaceRenderEngine2;
    }

    public void setResolution(int height, int width) {
        HEIGHT = height;
        WIDTH = width;

    }

    public void prepareEncoder(File file) throws IOException {
        this.mBufferInfo = new MediaCodec.BufferInfo();
        int i = WIDTH;

        if (i % 2 != 0) {
            WIDTH = i - 1;
        }

        int i2 = HEIGHT;

        if (i2 % 2 != 0) {
            HEIGHT = i2 - 1;
        }

        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(MIME_TYPE, WIDTH, HEIGHT);
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("bitrate", BIT_RATE);
        createVideoFormat.setInteger("frame-rate", FRAME_RATE);
        createVideoFormat.setInteger("i-frame-interval", 10);
        MediaCodec createEncoderByType = MediaCodec.createEncoderByType(MIME_TYPE);
        this.mEncoder = createEncoderByType;
        createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);

        this.mInputSurface = this.mEncoder.createInputSurface();
        this.mEncoder.start();
        this.mMuxer = new MediaMuxer(file.toString(), 0);
        this.mTrackIndex = -1;
        this.mMuxerStarted = false;

    }

    public void releaseEncoder() {
        Log.d(TAG, "releasing encoder objects");
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mEncoder.release();
            this.mEncoder = null;
        }
        Surface surface = this.mInputSurface;
        if (surface != null) {
            surface.release();
            this.mInputSurface = null;
        }
        MediaMuxer mediaMuxer = this.mMuxer;
        if (mediaMuxer != null) {
            mediaMuxer.stop();
            this.mMuxer.release();
            this.mMuxer = null;
        }
    }

    public void drainEncoder(boolean z) {
        Log.d(TAG, "drainEncoder(" + z + ")");
        if (z) {
            Log.d(TAG, "sending EOS to encoder");
            this.mEncoder.signalEndOfInputStream();
        }
        ByteBuffer[] outputBuffers = this.mEncoder.getOutputBuffers();
        while (true) {
            int dequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, 10000);
            if (dequeueOutputBuffer == -1) {
                if (z) {
                    Log.d(TAG, "no output available, spinning to await EOS");
                } else {
                    return;
                }
            } else if (dequeueOutputBuffer == -3) {
                outputBuffers = this.mEncoder.getOutputBuffers();
            } else if (dequeueOutputBuffer == -2) {
                if (!this.mMuxerStarted) {
                    MediaFormat outputFormat = this.mEncoder.getOutputFormat();
                    Log.d(TAG, "encoder output format changed: " + outputFormat);
                    this.mTrackIndex = this.mMuxer.addTrack(outputFormat);
                    this.mMuxer.start();
                    this.mMuxerStarted = VERBOSE;
                } else {
                    throw new RuntimeException("format changed twice");
                }
            } else if (dequeueOutputBuffer < 0) {
                Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
            } else {
                ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
                if (byteBuffer != null) {
                    if ((this.mBufferInfo.flags & 2) != 0) {
                        Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                        this.mBufferInfo.size = 0;
                    }
                    MediaCodec.BufferInfo bufferInfo = this.mBufferInfo;
                    if (bufferInfo.size != 0) {
                        if (this.mMuxerStarted) {
                            byteBuffer.position(bufferInfo.offset);
                            MediaCodec.BufferInfo bufferInfo2 = this.mBufferInfo;
                            byteBuffer.limit(bufferInfo2.offset + bufferInfo2.size);
                            MediaCodec.BufferInfo bufferInfo3 = this.mBufferInfo;
                            long j = this.mFakePts;
                            bufferInfo3.presentationTimeUs = j;
                            this.mFakePts = 33333 + j;
                            this.mMuxer.writeSampleData(this.mTrackIndex, byteBuffer, bufferInfo3);
                            Log.d(TAG, "sent " + this.mBufferInfo.size + " bytes to muxer");
                        } else {
                            throw new RuntimeException("muxer hasn't started");
                        }
                    }
                    this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        if (!z) {
                            Log.w(TAG, "reached end of stream unexpectedly");
                            return;
                        } else {
                            Log.d(TAG, "end of stream reached");
                            return;
                        }
                    }
                } else {
                    throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                }
            }
        }
    }

    public void setLayout(LottieAnimationView constraintLayout) {

        this.mainparent = constraintLayout;

    }

    public void generateFrame(Bitmap bitmap) {
        Canvas lockCanvas = this.mInputSurface.lockCanvas(null);
        try {
            lockCanvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        } finally {
            this.mInputSurface.unlockCanvasAndPost(lockCanvas);
        }
    }

    public Bitmap renderer(View view) {

        Bitmap createBitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));


        return createBitmap;
    }

    public float calcFramePercentage(int i, int i2) {
        if (i2 == 0) {
            return 0.0f;
        }
        return (float) ((i * 100) / i2);
    }

    public void deleteAllFilesFromDir(File file) {
        if (file.isDirectory()) {
            for (File delete : file.listFiles()) {
                delete.delete();
            }
        }
    }

    public interface RenderEngienInterface {
    }
}
