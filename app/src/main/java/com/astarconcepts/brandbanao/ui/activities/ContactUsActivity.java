package com.astarconcepts.brandbanao.ui.activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.api.ApiStatus;
import com.astarconcepts.brandbanao.databinding.ActivityContactUsBinding;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.NetworkConnectivity;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.Util;
import com.astarconcepts.brandbanao.viewmodel.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {

    ActivityContactUsBinding binding;
    PreferenceManager preferenceManager;
    UniversalDialog universalDialog;
    ProgressDialog prgDialog;
    NetworkConnectivity networkConnectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        networkConnectivity = new NetworkConnectivity(this);
        universalDialog = new UniversalDialog(this, false);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.login_loading));
        prgDialog.setCancelable(false);

        setUiViews();
    }

    private void setUiViews() {
        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_contact));

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.btnSave.setOnClickListener(v -> {
            if (validate()) {

                if (!networkConnectivity.isConnected()) {
                    Util.showToast(ContactUsActivity.this, getString(R.string.error_message__no_internet));
                    return;
                }

                if (binding.etNumber.getText().toString().length() < 10) {
                    Util.showToast(ContactUsActivity.this, getString(R.string.please_enter_valid_mobile));
                    return;

                }

                String name = binding.etName.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String number = binding.etNumber.getText().toString().trim();
                String message = binding.etDetails.getText().toString().trim();
                prgDialog.show();


                UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

                userViewModel.contactUsMessage(preferenceManager.getString(Constant.USER_ID), name,email, number, message).observe(this, apiStatus -> {


                    if (apiStatus!= null && apiStatus.status!=null) {

                        if (apiStatus.status.equals("200")){
                            prgDialog.cancel();
                            universalDialog.showSuccessDialog(getString(R.string.message_contact), getString(R.string.ok));
                            universalDialog.show();
                            universalDialog.okBtn.setOnClickListener(v1 -> {
                                universalDialog.cancel();
                                onBackPressed();
                            });

                        }else{
                            prgDialog.cancel();
                            universalDialog.showErrorDialog(getString(R.string.fail_message_contact), getString(R.string.ok));
                            universalDialog.show();
                        }


                    }
                });

            }
        });

    }

    private Boolean validate() {
        if (binding.etName.getText().toString().trim().isEmpty()) {
            binding.etName.setError(getResources().getString(R.string.hint_name));
            binding.etName.requestFocus();
            return false;
        } else if (binding.etEmail.getText().toString().trim().isEmpty()) {
            binding.etEmail.setError(getResources().getString(R.string.email));
            binding.etEmail.requestFocus();
            return false;
        } else if (!isEmailValid(binding.etEmail.getText().toString())) {
            binding.etEmail.setError(getString(R.string.invalid_email));
            binding.etEmail.requestFocus();
            return false;
        } else if (binding.etDetails.getText().toString().isEmpty()) {
            binding.etDetails.setError(getResources().getString(R.string.enter_details));
            binding.etDetails.requestFocus();
            return false;
        } else if (binding.etNumber.getText().toString().isEmpty()) {
            binding.etNumber.setError(getResources().getString(R.string.hint_phone_number));
            binding.etNumber.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }

}