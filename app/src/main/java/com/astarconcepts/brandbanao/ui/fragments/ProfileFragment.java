package com.astarconcepts.brandbanao.ui.fragments;

import static com.arthenica.mobileffmpeg.Config.getPackageName;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.binding.GlideDataBinding;
import com.astarconcepts.brandbanao.databinding.FragmentProfileBinding;
import com.astarconcepts.brandbanao.model.UserItem;
import com.astarconcepts.brandbanao.ui.activities.CustomSplashActivity;
import com.astarconcepts.brandbanao.ui.activities.PrivacyActivity;
import com.astarconcepts.brandbanao.ui.activities.ProfileEditActivity;
import com.astarconcepts.brandbanao.ui.activities.ServicesActivity;
import com.astarconcepts.brandbanao.ui.activities.SettingActivity;
import com.astarconcepts.brandbanao.ui.activities.SubsPlanActivity;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ProfileFragment extends Fragment {
    Activity context;
    UserItem userItem;
    FragmentProfileBinding binding;
    PreferenceManager preferenceManager;
    UniversalDialog universalDialog;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        preferenceManager = new PreferenceManager(context);
        universalDialog = new UniversalDialog(context, false);

        setUpUi();
        // set user details
        setProfile();
    }

    @Override
    public void onResume() {
        super.onResume();

        setProfile();
    }

    private void setProfile() {
        userItem = Constant.getUserItem(context);
        GlideDataBinding.bindImage(binding.circularImageView, userItem.getUserImage());
        binding.txtName.setText(userItem.getUserName());
        binding.txtNumber.setText(userItem.getPhone());

        if (preferenceManager.getBoolean(Constant.IS_SUBSCRIBE)){

            binding.tvPremium.setVisibility(View.VISIBLE);

        }else {
           binding.tvPremium.setVisibility(View.GONE);
        }

    }

    private void setUpUi() {


        if (!preferenceManager.getBoolean(Constant.IS_SUBSCRIBE)) {
            binding.btnSubsUpgrade.setVisibility(View.VISIBLE);
        } else {
            binding.btnSubsUpgrade.setVisibility(View.GONE);
        }

        binding.ivEdit.setOnClickListener(v -> startActivity(new Intent(context, ProfileEditActivity.class)));

        binding.rlSetting.setOnClickListener(v -> startActivity(new Intent(context, SettingActivity.class)));

        binding.rlRefundPolicy.setOnClickListener(v -> {

            Intent intent = new Intent(context, PrivacyActivity.class);
            intent.putExtra("type", Constant.REFUND_POLICY);
            startActivity(intent);

        });

        binding.btnSubsUpgrade.setOnClickListener(v -> startActivity(new Intent(context, SubsPlanActivity.class)));
        binding.services.setOnClickListener(v -> startActivity(new Intent(context, ServicesActivity.class)));
        binding.logout.setOnClickListener(v -> {

            UniversalDialog universalDialog = new UniversalDialog(context, false);
            universalDialog.showConfirmDialog(getString(R.string.menu_logout), getString(R.string.message__want_to_logout),
                    getString(R.string.message__logout),
                    getString(R.string.message__cancel_close));

            universalDialog.okBtn.setOnClickListener(view -> {

                universalDialog.cancel();

                preferenceManager.clearAllDataOnLogout();

                preferenceManager.setBoolean(Constant.IS_LOGIN, false);
                userItem = null;

                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);

                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(context, task -> Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show());
                startActivity(new Intent(context, CustomSplashActivity.class));
                context.finish();

            });

            universalDialog.cancelBtn.setOnClickListener(view -> universalDialog.cancel());

            universalDialog.show();

        });

        binding.rlRateAap.setOnClickListener(v -> {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });

        binding.rlShare.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt) + context.getPackageName());
            startActivity(Intent.createChooser(intent, "Share App..."));

        });

    }

}