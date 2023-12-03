package com.astarconcepts.brandbanao.ui.activities;

import static com.astarconcepts.brandbanao.utils.Constant.DARK_MODE_ON;
import static com.astarconcepts.brandbanao.utils.Constant.NOTIFICATION_ENABLED;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.astarconcepts.brandbanao.BuildConfig;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.databinding.ActivitySettingBinding;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.io.File;
import java.text.DecimalFormat;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    PreferenceManager preferenceManager;
    String[] themes;

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        setUiViews();
    }

    private void setUiViews() {
        themes = getResources().getStringArray(R.array.theme_array);
        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_setting));

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.txtVersion.setText(getString(R.string.version) + ": " + BuildConfig.VERSION_NAME);
        binding.txtTheme.setText(getString(R.string.theme) + ": " + (preferenceManager.getString(DARK_MODE_ON).equals("yes") ? themes[1] : themes[2]));
        if (preferenceManager.getString(DARK_MODE_ON).equals("yes")) {
            binding.swTheme.setChecked(true);
        } else if (preferenceManager.getString(DARK_MODE_ON).equals("no")) {
            binding.swTheme.setChecked(false);
        }
        binding.swTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.swTheme.setChecked(isChecked);
            binding.txtTheme.setText(getString(R.string.theme) + ": " + (isChecked ? themes[1] : themes[2]));
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

            if (isChecked) {
                preferenceManager.setString(DARK_MODE_ON, "yes");
            } else {
                preferenceManager.setString(DARK_MODE_ON, "no");
            }
        });

        binding.swNotification.setChecked(preferenceManager.getBoolean(NOTIFICATION_ENABLED));
        binding.swNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.swNotification.setChecked(isChecked);
            preferenceManager.setBoolean(NOTIFICATION_ENABLED, isChecked);
        });

        initializeCache();
        binding.cvClearCache.setOnClickListener(v -> {
            deleteCache(this);
            initializeCache();
        });

        binding.cvPrivacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.PRIVACY_POLICY);
            startActivity(intent);
        });

        binding.cvTermService.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.TERM_CONDITION);
            startActivity(intent);
        });


        binding.cvContact.setOnClickListener(v -> startActivity(new Intent(this, ContactUsActivity.class)));

    }

    private void initializeCache() {
        long size = 0;
        try {
            size += getDirSize(this.getCacheDir());
            size += getDirSize(this.getExternalCacheDir());
        } catch (Exception e) {

        }

        binding.txtCache.setText(getString(R.string.clear_cache) + ": " + readableFileSize(size));

    }

    public long getDirSize(File dir) {
        long size = 0;

        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }
}