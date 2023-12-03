package com.astarconcepts.brandbanao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageDownloadManager {
    private static final String DOT = ".";
    private static final String FORWARD_SLASH = "/";
    private static final Object LOCK = new Object();
    private static final String LOG_TAG = "ImageDownloadManager";
    public static ArrayList<String> downloadImages;
    private static ImageDownloadManager sInstance;
    private final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private final HashMap<ImageDownloadTask, Callback> callbacks = new HashMap<>();
    private final Context context;
    private final ThreadPoolExecutor threadPoolExecutor;

    private ImageDownloadManager(Context context2) {
        if (sInstance == null) {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            this.context = context2.getApplicationContext();
            int i = availableProcessors * 2;
            this.threadPoolExecutor = new ThreadPoolExecutor(i, i, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
            return;
        }
        throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
    }

    public static ImageDownloadManager getInstance(Context context2) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new ImageDownloadManager(context2);
                }
            }
        }
        return sInstance;
    }

    public static String getFileNameFromUrl(String str) {
        return str.substring(str.lastIndexOf(FORWARD_SLASH));
    }

    public static String getHashCodeBasedFileName(String str) {
        return str.hashCode() + DOT + getExtension(getFileNameFromUrl(str));
    }

    public static String getExtension(String str) {
        int lastIndexOf;
        if (str == null || str.isEmpty() || (lastIndexOf = str.lastIndexOf(DOT)) == -1 || lastIndexOf >= str.length()) {
            return "";
        }
        return str.substring(lastIndexOf + 1);
    }

    public static boolean saveBitmapImage(Bitmap bitmap, String str, String str2) {
        File file = new File(str);

        if (!file.exists() && !file.mkdir()) {
            return false;
        }
        try {
            String hashCodeBasedFileName = getHashCodeBasedFileName(str2);
            String str3 = str + FORWARD_SLASH + hashCodeBasedFileName;
            FileOutputStream fileOutputStream = new FileOutputStream(str3);
            bitmap.compress(getCompressFormatFromFileName(hashCodeBasedFileName), 100, fileOutputStream);
            fileOutputStream.close();
            downloadImages.add(str3);
            return true;
        } catch (Exception e) {


            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap.CompressFormat getCompressFormatFromFileName(String fileName) {
        switch (getExtension(fileName)) {
            case Extensions.WEBP:
                return Bitmap.CompressFormat.WEBP;
            case Extensions.JPEG:
                return Bitmap.CompressFormat.JPEG;
            default:
                return Bitmap.CompressFormat.PNG;
        }
    }

    public void addTask(ImageDownloadTask imageDownloadTask) {
        if (this.callbacks.containsKey(imageDownloadTask)) {
            Log.e(LOG_TAG, "Have another task to process with same Tag. Rejecting");
            return;
        }
        this.threadPoolExecutor.execute(new ImageDownloadRunnable(imageDownloadTask));
        this.callbacks.put(imageDownloadTask, imageDownloadTask.callback.get());
    }

    public void deleteFolder(File file) {
        try {
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.list().length == 0) {
                file.delete();
            } else {
                for (String file2 : file.list()) {
                    deleteFolder(new File(file, file2));
                }
                if (file.list().length == 0) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap startDownload(String str) {
        try {
            return Glide.with(this.context).asBitmap().load(Uri.parse(str)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true)).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void postSuccess(final ImageDownloadTask imageDownloadTask) {
        final Callback callback = imageDownloadTask.callback.get();
        if (callback != null) {
            this.MAIN_HANDLER.post(() -> callback.onSuccess(imageDownloadTask, ImageDownloadManager.downloadImages));
        }
        this.callbacks.remove(imageDownloadTask);
    }

    public void postFailure(ImageDownloadTask imageDownloadTask, final ImageSaveFailureReason imageSaveFailureReason) {
        final Callback callback = imageDownloadTask.callback.get();
        if (callback != null) {
            this.MAIN_HANDLER.post(() -> callback.onFailure(imageSaveFailureReason));
        }
        this.callbacks.remove(imageDownloadTask);
    }

    public enum ImageSaveFailureReason {
        NETWORK,
        FILE
    }


    public enum Task {
        DOWNLOAD,
        DELETE
    }


    public interface Callback {
        void onFailure(ImageSaveFailureReason imageSaveFailureReason);

        void onSuccess(ImageDownloadTask imageDownloadTask, ArrayList<String> arrayList);
    }

    public interface Extensions {
        String JPEG = "jpeg";
        String PNG = "png";
        String WEBP = "webp";
    }

    public static class ImageDownloadTask {
        private final Object tag;
        public WeakReference<Callback> callback;
        public String folderPath;
        public Task task;
        public List<String> urls;

        public ImageDownloadTask(Object obj, Task task2, List<String> list, String str, Callback callback2) {
            this.tag = obj;
            this.task = task2;
            this.urls = list;
            this.folderPath = str;
            this.callback = new WeakReference<>(callback2);
            ImageDownloadManager.downloadImages = new ArrayList<>();
        }

        public boolean equals(Object obj) {
            if (obj instanceof ImageDownloadTask) {
                return this.tag.equals(((ImageDownloadTask) obj).tag);
            }
            return super.equals(obj);
        }

        public int hashCode() {
            return this.tag.hashCode();
        }
    }

    private class ImageDownloadRunnable implements Runnable {
        ImageDownloadTask imageDownloadTask;

        ImageDownloadRunnable(ImageDownloadTask imageDownloadTask2) {
            this.imageDownloadTask = imageDownloadTask2;
            if (imageDownloadTask2 == null) {
                throw new InvalidParameterException("Task is null");
            }
        }

        public void run() {
            switch (this.imageDownloadTask.task) {
                case DELETE:
                    ImageDownloadManager.this.deleteFolder(new File(this.imageDownloadTask.folderPath));
                    ImageDownloadManager.this.postSuccess(this.imageDownloadTask);
                    break;
                case DOWNLOAD:
                    downloadImages(this.imageDownloadTask);
                    break;
            }


        }

        private void downloadImages(ImageDownloadTask imageDownloadTask2) {
            for (String next : imageDownloadTask2.urls) {
                Bitmap bitmap = ImageDownloadManager.this.startDownload(next);
                if (bitmap == null) {
                    ImageDownloadManager.this.postFailure(this.imageDownloadTask, ImageSaveFailureReason.NETWORK);
                    return;
                } else if (!ImageDownloadManager.saveBitmapImage(bitmap, this.imageDownloadTask.folderPath, next)) {
                    ImageDownloadManager.this.postFailure(imageDownloadTask2, ImageSaveFailureReason.FILE);
                    return;
                }
            }
            ImageDownloadManager.this.postSuccess(imageDownloadTask2);
        }

        public boolean equals(Object obj) {
            if (obj instanceof ImageDownloadRunnable) {
                return this.imageDownloadTask.equals(((ImageDownloadRunnable) obj).imageDownloadTask);
            }
            return super.equals(obj);
        }
    }
}
