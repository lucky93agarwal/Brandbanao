package com.astarconcepts.brandbanao.Recorder.renderer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.astarconcepts.brandbanao.custom.animated_video.activities.VideoEditorActivity;

import java.io.File;
import java.io.IOException;

public class Renderer extends RenderEngine {
    private static final String TAG = "Renderer";
    public LottieAnimationView animationView;
    public LottieAnimationView constraintLayout;
    public Context context;
    public String str = TAG;
    public int videoFrames = 900;

    public Renderer(Context context2, LottieAnimationView constraintLayout2, LottieAnimationView lottieAnimationView, int videoFrames2) {
        this.context = context2;
        this.constraintLayout = constraintLayout2;
        this.animationView = lottieAnimationView;
        this.videoFrames = videoFrames2;
    }

    @Override
    public void init(File file) {
        Log.i(this.str, "Generating movie...");

        setResolution(this.constraintLayout.getHeight(), this.constraintLayout.getWidth());

        Log.d(TAG, "line56 height = " + this.constraintLayout.getHeight());
        Log.d(TAG, "line56 width = " + this.constraintLayout.getWidth());

        try {
            generateMovie(file);
            Log.i(this.str, "Movie generation complete");
        } catch (Exception e) {
            int heightnew = this.constraintLayout.getHeight();
            int widthnew = this.constraintLayout.getWidth();
            if (((float) (this.constraintLayout.getWidth() % 2)) != 0.0f) {
                heightnew--;
            } else {
                widthnew--;
            }
            try {
                setFrameResolution(heightnew, widthnew);
                generateMovie(file);
            } catch (Exception e2) {
                showAlertDialog("Rendering error", this.constraintLayout.getHeight() + "= height " + this.constraintLayout.getWidth() + "= width");
            }
            Log.e(this.str, "Movie generation FAILED", e);
        }
    }

    public void showAlertDialog(String str2, String str22) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(str2);
        builder.setMessage(str22);
        builder.setPositiveButton("OK", new DialogInterface$OnClickListenerC4726a(this));
        builder.create().show();
    }

    public void generateMovie(File file) {
        setLayout(this.constraintLayout);
        try {
            prepareEncoder(file);
            Handler handler = new Handler();
            handler.post(new RunnableC4727b(handler, file));
        } catch (IOException e) {
            Log.e(TAG, "generateMovie: ", e);
        }
    }

    public void setFrameResolution(int heightnew, int widthnew) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.constraintLayout.getLayoutParams();
        layoutParams.width = widthnew;
        layoutParams.height = heightnew;
        this.constraintLayout.setLayoutParams(layoutParams);
    }

    public class DialogInterface$OnClickListenerC4726a implements DialogInterface.OnClickListener {
        public DialogInterface$OnClickListenerC4726a(Renderer this$0) {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    public class RunnableC4727b implements Runnable {


        public final Handler f7127c;
        public final File f7128d;
        public int f7126b = 0;

        public RunnableC4727b(Handler handler, File file) {
            this.f7127c = handler;
            this.f7128d = file;
        }

        public void run() {
            Renderer.this.drainEncoder(false);
            LottieAnimationView lottieAnimationView = Renderer.this.animationView;
            lottieAnimationView.setFrame((int) (((float) this.f7126b) % lottieAnimationView.getMaxFrame()));

            Renderer renderer2 = Renderer.this;
            renderer2.generateFrame(renderer2.renderer(renderer2.constraintLayout));
            int i = this.f7126b;
            this.f7126b = i + 1;
            Renderer renderer3 = Renderer.this;
            if (((float) i) < ((float) renderer3.videoFrames)) {
                this.f7127c.postDelayed(this, 0);
                Renderer renderer4 = Renderer.this;
                float calcFramePercentage = renderer4.calcFramePercentage(this.f7126b, renderer4.videoFrames);
                RenderEngienInterface aVar = Renderer.this.interfaceRenderEngine;
                if (aVar != null) {
                    ((VideoEditorActivity.RenderInterface) aVar).onProgress(calcFramePercentage);
                    return;
                }
                return;
            }
            renderer3.drainEncoder(true);
            Renderer.this.releaseEncoder();


            RenderEngienInterface aVar2 = Renderer.this.interfaceRenderEngine;


            if (aVar2 != null) {
                ((VideoEditorActivity.RenderInterface) aVar2).onExporteed(this.f7128d);
            }
        }
    }
}
