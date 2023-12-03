package com.astarconcepts.brandbanao.utils;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static com.astarconcepts.brandbanao.custom.animated_video.activities.SelectImageActivity.getDataColumn;
import static com.astarconcepts.brandbanao.custom.animated_video.activities.SelectImageActivity.isDownloadsDocument;
import static com.astarconcepts.brandbanao.custom.animated_video.activities.SelectImageActivity.isMediaDocument;
import static com.astarconcepts.brandbanao.utils.Constant.DARK_MODE_ON;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;
import com.astarconcepts.brandbanao.model.AdsModel;
import com.astarconcepts.brandbanao.ui.activities.SubsPlanActivity;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.gson.Gson;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HttpsURLConnection;

public class MyUtils {

    public static AdsModel model;
    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    public static PreferenceManager preferenceManager;
    private static String TAG="AdmobAppId";
    public Activity context;
    public static TemplateModel templateModel;

    public static final void saveWatermark(Activity context) {

        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), R.drawable.watermark_remove);
        File file = new File(context.getExternalCacheDir(), "water_mark.png");
        if (!file.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                decodeResource.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        }
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghjiklmnopqrstuvwxyz".length());
            builder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghjiklmnopqrstuvwxyz".charAt(character));
        }
        return builder.toString();
    }

    public static Bitmap getBitmapFromURLImage(String src) {
        try {
            URL url = new URL(src);
            InputStream input;
            if (src.contains("https://")) {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } else {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            }
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }


    public static void showResponse(Object onject) {


        Log.i("MyResponse " + onject.getClass().getSimpleName(), "" + new Gson().toJson(onject));

    }

    public static void showResponse(String tag, Object onject) {


        Log.i(tag, tag + "->>" + new Gson().toJson(onject));

    }

    public static void unzip(File zipFile, File targetDirectory, TemplateUtils.OnUnZipListener onUnZipListener) throws Exception {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {

            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }

            }
        } finally {
            zis.close();

            zipFile.delete();

            onUnZipListener.onZip(targetDirectory.getAbsolutePath());

        }
    }

    public static boolean isConnectingToInternet(Context context) {

        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static String getFileExtension(String file) {
        return FilenameUtils.getExtension(file).toUpperCase();
    }

    public static String readFromFile(String path) {
        String ret = "";
        try {
            InputStream inputStream = new FileInputStream(new File(path));

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("FileToJson", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("FileToJson", "Can not read file: " + e.toString());
        }
        return ret;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getStoreVideoExternalStorage(Activity context) {

        File file = new File(Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_DOWNLOADS + File.separator + context.getResources().getString(R.string.app_name)
                + File.separator);
        if (!file.exists())
            file.mkdirs();

        return file.getAbsolutePath();

    }

    public static String getFolderPath(Activity activity, String folder) {

        File file = new File(activity.getFilesDir().getAbsolutePath(), folder);

        if (!file.exists())
            file.mkdirs();

        return file.getAbsolutePath();


    }

    public static String format(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        if (number != null) {


            long numValue = number.longValue();
            int value = (int) Math.floor(Math.log10(numValue));
            int base = value / 3;
            if (value >= 3 && base < suffix.length) {
                return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
            } else {
                return new DecimalFormat("#,##0").format(numValue);
            }

        } else {
            return String.valueOf(number);
        }

    }

    public static void hideNavigation(Activity context, boolean statusBar) {
        View decorView = context.getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        if (statusBar) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.bg_screen));
            }

            PreferenceManager preferenceManager = new PreferenceManager(context);

            if (!preferenceManager.getString(DARK_MODE_ON).equals("yes")) {

                uiOptions =
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;


            }
        }


        decorView.setSystemUiVisibility(uiOptions);
    }

    public static String extractFilename(String pathOrUrl) {
        String filename = "";

        if (pathOrUrl != null) {
            // Extract filename from URL
            if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
                try {
                    URL url = new URL(pathOrUrl);
                    String urlPath = url.getPath();
                    filename = urlPath.substring(urlPath.lastIndexOf('/') + 1);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else { // Extract filename from local file path
                File file = new File(pathOrUrl);
                filename = file.getName();
            }
        }

        return filename;
    }

    public static String getAppFolder(Context activity) {
        return activity.getExternalFilesDir(null).getPath() + "/";
    }

    public static void showToast(Activity activity, String string) {

        Toast.makeText(activity, "" + string, Toast.LENGTH_SHORT).show();
    }

    public static void saveLogoPath(Activity context, String url, final FrameUtils.OnLogoDownloadListener listener) {

        String filepath = getFolderPath(context, "cache");

        if (url != null && !url.isEmpty() && !url.contains("http")) {
            if (listener != null) {
                listener.onLogoDownloadError();
            }
            return;
        }

        String name = MyUtils.extractFilename(url);

        String resizedLogoPath = getFolderPath(context, "resized_logos") + File.separator + name;

        File file = new File(resizedLogoPath);

        if (file.exists()) {
            if (listener != null) {
                listener.onLogoDownloaded(file.getAbsolutePath());
            }
            return;
        }


        PRDownloader.download(url, filepath, name)
                .build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        String logoPath = new File(filepath, name).getAbsolutePath();

                        // Bitmap resizedLogo = resizeLogo(logoPath, Constant.BUSINESS_LOGO_WIDTH, Constant.BUSINESS_LOGO_HEIGHT);

                        if (listener != null) {
                            //  String resizedLogoPath = saveResizedLogo(context, logoPath, name);
                            listener.onLogoDownloaded(logoPath);
                        }


                    }

                    @Override
                    public void onError(Error error) {
                        if (listener != null) {
                            listener.onLogoDownloadError();
                        }
                    }
                });


    }

    private static Bitmap resizeLogo(String logoPath, int desiredWidth, int desiredHeight) {
        Bitmap logoBitmap = BitmapFactory.decodeFile(logoPath);
        return Bitmap.createScaledBitmap(logoBitmap, desiredWidth, desiredHeight, false);
    }

    private static String saveResizedLogo(Activity context, Bitmap resizedLogo, String name) {

        String resizedLogoPath = getFolderPath(context, "resized_logos") + File.separator + name;

        try (FileOutputStream outStream = new FileOutputStream(resizedLogoPath)) {
            resizedLogo.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resizedLogoPath;
    }


    public static void updateAdmobAppid(Activity context, String appId){



        try {
            ApplicationInfo ai =  context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
            Log.d(TAG, "Name Found: " + myApiKey);
            ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", appId);//you can replace your key APPLICATION_ID here
            String ApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
            Log.d(TAG, "ReNamed Found: " + ApiKey);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }

    public static void openPlanActivity(Activity context) {

        context.startActivity(new Intent(context, SubsPlanActivity.class));

    }

    public static String getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return "blocked";
            }
            return "denied";
        }
        return "granted";
    }

    public static class GetImageFileAsync extends AsyncTask<String, Void, File> {

        Activity context;
        Bitmap bitmap;

        OnBitmapSaved mBitmapSavedListener;
        ProgressDialog dialog;

        public GetImageFileAsync(Activity context, Bitmap bitmap) {
            this.context = context;
            this.bitmap = bitmap;


            dialog = new ProgressDialog(context);
            dialog.setMessage("Please Wait...");

            dialog.setCancelable(false);
            dialog.show();


            this.execute();
        }

        public void onBitmapSaved(OnBitmapSaved mlistener) {
            this.mBitmapSavedListener = mlistener;
        }

        @Override
        protected File doInBackground(String... strings) {


            String path = context.getCacheDir().getAbsolutePath();

            String name = System.currentTimeMillis() + ".png";

            File file = null;

            try {

                OutputStream fOut = null;
                Integer counter = 0;


                file = new File(path, name);

                if (!file.exists())
                    file.createNewFile();
                // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                fOut = new FileOutputStream(file);

                Bitmap pictureBitmap = bitmap; // obtaining the Bitmap
                pictureBitmap.compress(Bitmap.CompressFormat.PNG, 70, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream

            } catch (Exception e) {
                e.printStackTrace();
            }

            return file;
        }

        @Override
        protected void onPostExecute(File s) {
            super.onPostExecute(s);

            dialog.dismiss();

            mBitmapSavedListener.onSaved(s);
        }

        public interface OnBitmapSaved {
            void onSaved(File file);
        }
    }


}
