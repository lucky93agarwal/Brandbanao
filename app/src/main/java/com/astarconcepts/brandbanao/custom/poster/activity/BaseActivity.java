package com.astarconcepts.brandbanao.custom.poster.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.astarconcepts.brandbanao.utils.Configure;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class BaseActivity extends AppCompatActivity {
    public static final String TAG = "BaseActivity";
    public PreferenceManager PreferenceManager;

    public static void copyFile(Activity context, String str, String str2) {
        try {
            InputStream open = context.getAssets().open("font/" + str);
            String str3 = str2 + "/" + str;
            if (new File(str3).exists()) {
                Log.e(TAG, "copyAssets: font exist   " + str3);
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(str3);
            Log.e(TAG, "copyFile: " + str);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    open.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        this.PreferenceManager = new PreferenceManager(this);

    }

    public void makeStickerDir(Activity context) {
        this.PreferenceManager = new PreferenceManager(context);
        new StorageUtils(context).getPackageStorageDir(".sticker").getAbsolutePath();
        File file = new File(new StorageUtils(context).getPackageStorageDir(".sticker").getAbsolutePath() + "/.Poster Design Stickers/sticker");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(new StorageUtils(context).getPackageStorageDir(".sticker").getAbsolutePath() + "/.Poster Design Stickers/sticker/bg");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(new StorageUtils(context).getPackageStorageDir(".sticker").getAbsolutePath() + "/.Poster Design Stickers/sticker/font");
        if (!file3.exists()) {
            file3.mkdirs();
        }
        for (int i = 0; i < 29; i++) {
            File file4 = new File(new StorageUtils(context).getPackageStorageDir(".sticker").getAbsolutePath() + "/.Poster Design Stickers/sticker/cat" + i);
            if (!file4.exists()) {
                file4.mkdirs();
            }
        }
        for (int i2 = 0; i2 < 11; i2++) {
            File file5 = new File(new StorageUtils(context).getPackageStorageDir(".sticker").getAbsolutePath() + "/.Poster Design Stickers/sticker/art" + i2);
            if (!file5.exists()) {
                file5.mkdirs();
            }
        }
        this.PreferenceManager.setString(Constant.sdcardPath, file.getPath());
        Log.e(TAG, "onCreate: " + Constant.sdcardPath);
    }

    public class CopyFontBG extends AsyncTask<String, Void, String> {
        public CopyFontBG() {
        }

        @Override
        public void onPostExecute(String str) {


        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onProgressUpdate(Void... voidArr) {
        }

        public String doInBackground(String... strArr) {
            try {


                File file = new File(Configure.GetFileDir(BaseActivity.this).getPath() + File.separator + "font");


                try {
                    String[] list = getAssets().list("font");
                    if (!file.exists() && !file.mkdir()) {
                        Log.e(BaseActivity.TAG, "No create external directory: " + file);
                    }
                    for (String str : list) {

                        copyFile(BaseActivity.this, str, file.getPath());

                    }


                    return "Executed";
                } catch (IOException e) {
                    Log.e(BaseActivity.TAG, "I/O Exception", e);
                    return "Executed";
                }
            } catch (NullPointerException e2) {
                e2.printStackTrace();
                return "Executed";
            }
        }
    }


}
