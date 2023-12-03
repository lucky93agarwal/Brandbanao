package com.astarconcepts.brandbanao.ui.activities;

import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.astarconcepts.brandbanao.AdsUtils.AdsUtils;
import com.astarconcepts.brandbanao.AdsUtils.InterstitialsAdsManager;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.binding.GlideDataBinding;
import com.astarconcepts.brandbanao.databinding.ActivityAddBusinessBinding;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.ImageCropperFragment;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.NetworkConnectivity;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.Util;
import com.astarconcepts.brandbanao.viewmodel.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class AddBusinessActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 112;
    public static final String UPDATE = "Update";
    public static String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
    ActivityAddBusinessBinding binding;
    BusinessItem businessItem;
    Uri imageUri;
    String profileImagePath = null;
    NetworkConnectivity networkConnectivity;
    PreferenceManager preferenceManager;
    ProgressDialog prgDialog;
    UniversalDialog universalDialog;

    Activity activity;
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Here, no request code
                    if (result.getData() != null) {
                        getImageFromURI(result);
                    }
                }
            });
    InterstitialsAdsManager interstitialsAdsManager;
    private String stringBusinessCategory = null;

    private String Action;
    private Dialog dialog;

    private void getImageFromURI(ActivityResult result) {
        Uri selectedImage = result.getData().getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        if (selectedImage != null) {
            Cursor cursor = getContentResolver().query(selectedImage,
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                profileImagePath = cursor.getString(columnIndex);
                cursor.close();

                imageUri = selectedImage;

                beginCrop(imageUri);


            }
        }
    }


    private void beginCrop(Uri uri) {
        if (uri != null) {
            try {

                Uri destinationUri = Uri.fromFile(new File(getCacheDir(), new File(uri.getPath()).getName()));
                UCrop.Options options2 = new UCrop.Options();
                options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
                options2.setFreeStyleCropEnabled(true);

                UCrop.of(uri, destinationUri)
                        .withOptions(options2)
                        .start(this);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    UserViewModel userViewModel;

    String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBusinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        preferenceManager = new PreferenceManager(this);

        // load Ads
        new AdsUtils(this).showBannerAds(activity);
        interstitialsAdsManager = new InterstitialsAdsManager(this);
        showBackDialog();

        // Here business Category & business is different things.

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Action = bundle.getString("Action");
        }

        businessItem = Constant.businessItem;

        if (businessItem != null && Action.equalsIgnoreCase("update")) {

            businessId = businessItem.getBusinessid();
            binding.activeCatName.setText(businessItem.getBusinessCategory());
            stringBusinessCategory = businessItem.getCategoryId();

        }


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        networkConnectivity = new NetworkConnectivity(this);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getResources().getString(R.string.login_loading));
        prgDialog.setCancelable(false);

        universalDialog = new UniversalDialog(this, false);

        setUiViews();

        binding.ActivitybusinessLayoutBusiness.setOnClickListener(v -> startActivityForResult(new Intent(activity, MyBusinessCategoryActivity.class), REQUEST_CODE));
    }


    private void setUiViews() {


        if (Action != null) {
            if (Action.equalsIgnoreCase(UPDATE)) {

                if (businessItem != null) {

                    binding.toolbar.toolName.setText(getString(R.string.edit_business_titles));
                    binding.etBusinessName.setText(businessItem.getName());
                    binding.etBusinessEmail.setText(businessItem.getEmail());
                    binding.etBusinessNumber.setText(businessItem.getPhone());
                    binding.etBusinessWebsite.setText(businessItem.getWebsite());
                    binding.etBusinessAddress.setText(businessItem.getAddress());
                    binding.etBusinessInsta.setText(businessItem.getSocial_instagram());
                    binding.etBusinessYoutube.setText(businessItem.getSocial_youtube());
                    binding.etBusinessFacebook.setText(businessItem.getSocial_facebook());
                    binding.etBusinessTwitter.setText(businessItem.getSocial_twitter());
                    binding.etBusinessTaglinee.setText(businessItem.getTagline());
                    binding.activeCatName.setText(businessItem.getBusinessCategory());

                    Glide.with(AddBusinessActivity.this).load(businessItem.getLogo()).into(binding.ivProfile);
                }
            } else {
                binding.toolbar.toolName.setText(getString(R.string.add_business_titles));
            }
        } else {
            binding.toolbar.toolName.setText(getString(R.string.add_business_titles));
        }

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.btnSave.setOnClickListener(v ->  {

            if (validate()) {
            interstitialsAdsManager.showInterstitialAd(() -> save());

        }

        });

        binding.ivBusiness.setOnClickListener(v -> {

            Intent i = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            someActivityResultLauncher.launch(i);

        });

    }

    private void save() {

        if (validate()) {

            String name = binding.etBusinessName.getText().toString().trim();
            String email = binding.etBusinessEmail.getText().toString().trim();
            String phone = binding.etBusinessNumber.getText().toString().trim();
            String website = binding.etBusinessWebsite.getText().toString().trim();
            String address = binding.etBusinessAddress.getText().toString().trim();
            String insta = binding.etBusinessInsta.getText().toString().trim();
            String youtube = binding.etBusinessYoutube.getText().toString().trim();
            String facebook = binding.etBusinessFacebook.getText().toString().trim();
            String twitter = binding.etBusinessTwitter.getText().toString().trim();
            String taglinee = binding.etBusinessTaglinee.getText().toString().trim();


            if (!networkConnectivity.isConnected()) {
                Util.showToast(this, getString(R.string.error_message__no_internet));
                return;
            }


            prgDialog.show();

            String businessId1 = Action.equalsIgnoreCase("Insert") ? null : businessId;


            userViewModel.submitBusiness(preferenceManager.getString(Constant.USER_ID),
                    businessId1,
                    profileImagePath,
                    name,
                    email,
                    phone,
                    website,
                    address,
                    insta,
                    youtube,
                    facebook,
                    twitter,
                    taglinee,
                    Action.toLowerCase(),
                    stringBusinessCategory).observe(this, businessItem -> {

                if (businessItem != null) {
                    prgDialog.dismiss();

                    Log.i("RESPONSE", "RESPONSE-->" + new Gson().toJson(businessItem));

                    Toast.makeText(AddBusinessActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();

                    if (businessItem.isDefault) {
                        Constant.setDefaultBusiness(AddBusinessActivity.this, businessItem);

                    }


                    setResult(RESULT_OK);
                    finish();

                }


            });


        }
    }


    private Boolean validate() {
        if (binding.etBusinessName.getText().toString().trim().isEmpty()) {
            binding.etBusinessName.setError(getResources().getString(R.string.hint_business_name));
            binding.etBusinessName.requestFocus();
            return false;
        } else if (binding.etBusinessNumber.getText().toString().trim().isEmpty()) {
            binding.etBusinessNumber.setError(getResources().getString(R.string.hint_business_number));
            binding.etBusinessNumber.requestFocus();
            return false;
        } else if (binding.etBusinessEmail.getText().toString().trim().isEmpty()) {
            binding.etBusinessEmail.setError(getResources().getString(R.string.hint_business_email));
            binding.etBusinessEmail.requestFocus();
            return false;
        } else if (!isEmailValid(binding.etBusinessEmail.getText().toString())) {
            binding.etBusinessEmail.setError(getString(R.string.invalid_email));
            binding.etBusinessEmail.requestFocus();
            return false;
        } else if (binding.etBusinessWebsite.getText().toString().isEmpty()) {
            binding.etBusinessWebsite.setError(getResources().getString(R.string.hint_business_website));
            binding.etBusinessWebsite.requestFocus();
            return false;
        } else if (binding.etBusinessAddress.getText().toString().trim().isEmpty()) {
            binding.etBusinessAddress.setError(getResources().getString(R.string.hint_business_address));
            binding.etBusinessAddress.requestFocus();
            return false;
        } else if (stringBusinessCategory == null) {
            Toast.makeText(AddBusinessActivity.this, "Please Select Business Categories", Toast.LENGTH_SHORT).show();
            return false;
        } else if (profileImagePath == null && !Action.equalsIgnoreCase(UPDATE)) {

                Toast.makeText(AddBusinessActivity.this, "Please Select Business logo", Toast.LENGTH_SHORT).show();
                binding.ivProfile.requestFocus();

            return false;

        } else {
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE && resultCode == -1) {

            // Update Business category here
            binding.activeCatName.setText(preferenceManager.getString(Constant.BUSINESS_CATEGORY_NAME));
            stringBusinessCategory = preferenceManager.getString(Constant.BUSINESS_CATEGORY_ID);

        } else if (requestCode == UCrop.REQUEST_CROP) {

            if (data != null) {
                new ImageCropperFragment(0, MyUtils.getPathFromURI(this, UCrop.getOutput(data)), (id, out) -> {

                    profileImagePath = out;
                    imageUri = Uri.parse(out);
                    GlideDataBinding.bindImage(binding.ivProfile, out);

                }).show(getSupportFragmentManager(), "");
            }

        }


    }

    public void onBackPressed() {

        if (!dialog.isShowing()) {

            dialog.show();
        }

    }

    private void showBackDialog() {

        dialog = new Dialog(this);
        this.dialog.requestWindowFeature(1);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.discard_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.findViewById(R.id.iv_close).setVisibility(VISIBLE);
        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());

        TextView textView = dialog.findViewById(R.id.tv_ok);
        textView.setText(R.string.save);

        LinearLayout button = dialog.findViewById(R.id.btn_yes);

        LinearLayout button2 = dialog.findViewById(R.id.btn_no);

        button.setOnClickListener(view -> {
            finish();
            dialog.dismiss();
        });

        button2.setOnClickListener(view -> {
            finish();
            dialog.dismiss();
            save();
        });

    }

}