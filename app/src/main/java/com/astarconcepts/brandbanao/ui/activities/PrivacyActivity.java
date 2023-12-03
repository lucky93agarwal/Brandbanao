package com.astarconcepts.brandbanao.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.databinding.ActivityPrivacyBinding;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

public class PrivacyActivity extends AppCompatActivity {

    ActivityPrivacyBinding binding;
    String type;
    String privacy;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);


        if (getIntent().getExtras() != null) {

            type = getIntent().getExtras().getString("type");

            if (type.equals(Constant.PRIVACY_POLICY)) {
                privacy = preferenceManager.getString(Constant.PRIVACY_POLICY);
                binding.toolbar.toolName.setText(getResources().getString(R.string.menu_privacy_policy));
            } else if (type.equals(Constant.TERM_CONDITION)) {
                privacy = preferenceManager.getString(Constant.TERM_CONDITION);
                binding.toolbar.toolName.setText(getResources().getString(R.string.terms_and_service));
            } else {
                privacy = preferenceManager.getString(Constant.REFUND_POLICY);
                binding.toolbar.toolName.setText(getResources().getString(R.string.refund_policy));
            }

            setData();
        }
    }

    private void setData() {
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.wvPrivacy.getSettings().setJavaScriptEnabled(true);

        binding.wvPrivacy.loadData(privacy, "", null);

        binding.wvPrivacy.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                // Set visibility to VISIBLE when page starts loading
                binding.progreee.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                    // Data has loaded, hide the ProgressBar
                    binding.progreee.setVisibility(View.GONE);
            }
        });
    }

}