package com.astarconcepts.brandbanao.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.databinding.ActivityPreviewBusinessCardBinding;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

public class PreviewDigitalCardActivity extends AppCompatActivity {
    Activity context;
    PreferenceManager preferenceManager;
    ActivityPreviewBusinessCardBinding binding;
    InterstitialsAdsManager interstitialsAdsManager;
    String url;


    BusinessItem businessItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBusinessCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        preferenceManager = new PreferenceManager(this);

        interstitialsAdsManager = new InterstitialsAdsManager(this);
        binding.toolbar.back.setOnClickListener(view -> onBackPressed());

        businessItem= Constant.getBusinessItem(this);

        if (getIntent().getExtras() != null) {

            url = getIntent().getExtras().getString("url") ;
            binding.wvPrivacy.getSettings().setJavaScriptEnabled(true);
            binding.wvPrivacy.loadUrl(url + "/fromApp");

            Log.d("url", "onCreate: "+url + "/fromApp");
            binding.wvPrivacy.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    binding.progressBar.setVisibility(View.VISIBLE);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    binding.progressBar.setVisibility(View.GONE);
                }
            });

        }

        binding.llEditCard.setOnClickListener(v -> {

            Constant.businessItem = businessItem;

            Intent intent = new Intent(context, AddBusinessActivity.class);
            intent.putExtra("Action", "Update");
            startActivity(intent);
        });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.ivShare.setOnClickListener(v -> {

            interstitialsAdsManager.showInterstitialAd(this::shareMore);
        });

        binding.ivWhatsapp.setOnClickListener(v -> interstitialsAdsManager.showInterstitialAd(this::sendToWhatsApp));

        binding.ivMessage.setOnClickListener(v -> interstitialsAdsManager.showInterstitialAd(() -> shareMessageViaSMS(getString(R.string.share_card) + url)));

    }

    public void shareMessageViaSMS(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:")); // Set the URI scheme for sending an SMS

        intent.putExtra("sms_body", message); // Set the message body

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Handle the case when no SMS app is available on the device
            Toast.makeText(this, "No SMS app found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareMore() {
        try {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_card) + url);
            startActivity(Intent.createChooser(intent, "Share Your Card!"));


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void sendToWhatsApp() {
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.whatsapp") != null) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_card) + url);
                intent.setPackage("com.whatsapp");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

}