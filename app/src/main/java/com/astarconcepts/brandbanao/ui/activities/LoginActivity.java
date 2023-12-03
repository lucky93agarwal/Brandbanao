package com.astarconcepts.brandbanao.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.databinding.ActivityLoginBinding;
import com.astarconcepts.brandbanao.model.LoginModel;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.NetworkConnectivity;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.Util;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserInfo;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    public GoogleSignInClient mGoogleSignInClient;
    ActivityLoginBinding binding;
    FirebaseUser user;
    UniversalDialog universalDialog;
    ProgressDialog prgDialog;
    PreferenceManager preferenceManager;
    NetworkConnectivity networkConnectivity;
    LoginModel loginmodel = new LoginModel();
    String otpType;
    public static PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public static PhoneAuthProvider.ForceResendingToken token;

    CountryCodePicker ccp;
    Activity context;
    private FirebaseAuth mAuth;
    String verificationId;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(

            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Here, no request code
                    if (result.getData() != null) {

                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {

                                firebaseAuthWithGoogle(account);
                                Util.showLog("Google sign in success ");

                            }
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Util.showLog("Google sign in failed: " + e);
                            prgDialog.dismiss();

                        }
                    }
                } else {

                    prgDialog.dismiss();

                }
            });

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        ccp = new CountryCodePicker(context);
        binding.countryRegister.setOnClickListener(v -> opencountry());

        preferenceManager = new PreferenceManager(this);
        universalDialog = new UniversalDialog(this, false);
        prgDialog = new ProgressDialog(this);
        networkConnectivity = new NetworkConnectivity(this);
        prgDialog.setMessage(getResources().getString(R.string.login_loading));
        prgDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            //
        };

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        String logoutRedirect = getIntent().getStringExtra(Constant.LOGIN_TYPE);
        if (logoutRedirect != null) {
            if (logoutRedirect.equalsIgnoreCase(Constant.GOOGLE)) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, task -> {
                            Toast.makeText(LoginActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                            preferenceManager.setBoolean(Constant.IS_LOGIN, false);
                            preferenceManager.remove(Constant.USER_ID);

                        });
            }
        }


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);

                if (context != null && !preferenceManager.getBoolean(Constant.IS_LOGIN)) {
                    Toast.makeText(context, "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken;

                binding.numberLay.setVisibility(View.GONE);
                binding.otpLay.setVisibility(View.VISIBLE);
                binding.tvOtp.setText("Code is sent to " + ccp.getDefaultCountryCodeWithPlus() + " " + binding.numberEt.getText().toString());
                binding.txtTabLogin.setText(R.string.otp_verify);

                prgDialog.dismiss();

                Toast.makeText(context, context.getString(R.string.otp_send_success), Toast.LENGTH_SHORT).show();

                startCountdown();
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                verifyAuth(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
                prgDialog.dismiss();

                Log.d("number", "onVerificationFailed: " + e.getMessage());
            }
        };

        initUi();
    }

    @Override
    public void onBackPressed() {

        if (binding.layoutLogin.getVisibility() == View.GONE) {
            binding.layoutLogin.setVisibility(View.VISIBLE);
            binding.layoutLogin.startAnimation(Constant.getAnimUp(context));
            binding.editTvLayout.setVisibility(View.GONE);
            binding.numberEt.setText("");
            binding.otpEdit.setText("");
            binding.otpLay.setVisibility(View.GONE);
            binding.numberLay.setVisibility(View.VISIBLE);

        } else {

            super.onBackPressed();
        }

    }

    private void initUi() {

        binding.phoneBtn.setOnClickListener(view1 -> {
            otpType = "phone";
            binding.editTvLayout.setVisibility(View.VISIBLE);
            binding.layoutLogin.setVisibility(View.GONE);
        });

        binding.whatsappBtn.setOnClickListener(view1 -> {
            binding.titleTv.setText(R.string.enter_wp_number);
            otpType = "whatsapp";
            binding.editTvLayout.setVisibility(View.VISIBLE);
            binding.layoutLogin.setVisibility(View.GONE);
        });

        binding.ivGoogle.setOnClickListener(v -> {
            if (!networkConnectivity.isConnected()) {
                Util.showToast(this, getString(R.string.error_message__no_internet));
                return;
            }

            signIn();

        });

        binding.recentBtn.setOnClickListener(view1 -> {
            if (binding.numberEt.getText().toString().length() > 9) {
                if (otpType.equals("phone")) {

                    prgDialog.show();

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(ccp.getDefaultCountryCodeWithPlus() + binding.numberEt.getText().toString())       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(this)                 // (optional) Activity for callback binding
                                    // If no activity is passed, reCAPTCHA verification can not be used.
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                } else {
                    getWhatsappOtp(ccp.getDefaultCountryCodeWithPlus() + binding.numberEt.getText().toString());
                }
            } else {
                Util.showToast(context, getString(R.string.please_enter_currect_number));
            }
        });

        binding.ivEditNum.setOnClickListener(v -> {

            binding.numberLay.setVisibility(View.VISIBLE);
            binding.otpLay.setVisibility(View.GONE);
            binding.txtTabLogin.setText(R.string.login_signup);


        });

        binding.otpBtn.setOnClickListener(v -> {

            if (binding.numberEt.getText().toString().length() > 9) {
                prgDialog.show();
                if (otpType.equals("phone")) {

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(ccp.getDefaultCountryCodeWithPlus() + binding.numberEt.getText().toString())       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(this)                 // (optional) Activity for callback binding
                                    // If no activity is passed, reCAPTCHA verification can not be used.
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                } else {
                    prgDialog.show();
                    getWhatsappOtp(ccp.getDefaultCountryCodeWithPlus() + binding.numberEt.getText().toString());


                }
            } else {
                Util.showToast(context, getString(R.string.please_enter_currect_number));
            }
        });

        binding.submitBtn.setOnClickListener(v -> {

            String otp = binding.otpEdit.getText().toString();

            if (otp.length() > 5) {
                if (otpType.equals("phone")) {

                    verifyAuth(PhoneAuthProvider.getCredential(verificationId, otp));

                } else {
                    if (whatsappOTP.equals(otp)) {
                        loginmodel.phoneNo = ccp.getDefaultCountryCodeWithPlus() + binding.numberEt.getText().toString();
                        addLoginInfo(null, null, null, loginmodel.phoneNo, Constant.PHONE);


                    } else {
                        Toast.makeText(context, getString(R.string.wrong_otp), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(context, getString(R.string.enter_currect_otp), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void signIn() {


        prgDialog.show();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);

    }


    @SuppressLint("WrongConstant")
    public void opencountry() {
        ccp.showCountryCodePickerDialog();
        ccp.setOnCountryChangeListener(selectedCountry -> {
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAuth != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        if (user != null) {
                            List<? extends UserInfo> userInfoList = user.getProviderData();

                            String email = "";
                            String uid = "";
                            String displayName = "";
                            String photoUrl = "";
                            String mobile = "";
                            for (int i = 0; i < userInfoList.size(); i++) {

                                email = userInfoList.get(i).getEmail();

                                if (email != null && !email.equals("")) {
                                    uid = userInfoList.get(i).getUid();
                                    displayName = userInfoList.get(i).getDisplayName();
                                    photoUrl = String.valueOf(userInfoList.get(i).getPhotoUrl());
                                    mobile = userInfoList.get(i).getPhoneNumber();
                                    break;
                                }

                            }

                            addLoginInfo(email, displayName, photoUrl, mobile, Constant.GOOGLE);

                        } else {
                            // Error Message
                            Toast.makeText(this, getString(R.string.login__fail_account), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // display message to user when it fails
                        Toast.makeText(this, getString(R.string.login__fail), Toast.LENGTH_LONG).show();
                        String email = user.getEmail();
                        handleFirebaseAuthError(email);

                    }
                });
    }

    private void addLoginInfo(String email, String displayName, String photoUrl, String mobile, String loginType) {


        Constant.getUserViewModel(this).userLoginGoogle(email, displayName, photoUrl, mobile, loginType).observe(this, userItem -> {


            if (userItem != null) {

                MyUtils.showResponse(userItem);

                preferenceManager.setBoolean(Constant.IS_LOGIN, true);

                preferenceManager.setString(Constant.PLAN_NAME, userItem.planName);
                preferenceManager.setString(Constant.PLAN_ID, userItem.planId);
                preferenceManager.setString(Constant.PLAN_DURATION, userItem.planDuration);
                preferenceManager.setString(Constant.PLAN_START_DATE, userItem.planStartDate);
                preferenceManager.setString(Constant.PLAN_END_DATE, userItem.planEndDate);
                preferenceManager.setBoolean(Constant.IS_SUBSCRIBE, userItem.isSubscribed);
                preferenceManager.setInt(Constant.USER_BUSINESS_LIMIT, userItem.businessLimit);

                preferenceManager.setString(Constant.USER_EMAIL, userItem.email);
                preferenceManager.setString(Constant.USER_NAME, userItem.userName);
                preferenceManager.setString(Constant.USER_PHONE, userItem.phone);
                preferenceManager.setString(Constant.USER_IMAGE, userItem.userImage);
                preferenceManager.setString(Constant.USER_DESIGNATION, userItem.designation);
                preferenceManager.setString(Constant.USER_ID, userItem.userId);
                preferenceManager.setString(Constant.STATUS, userItem.status);
                preferenceManager.setString(Constant.LOGIN_TYPE, loginType);
                preferenceManager.setInt(Constant.USER_BUSINESS_LIMIT, userItem.businessLimit);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                finish();

            } else {

                universalDialog.showErrorDialog(getString(R.string.fail_message_contact), getString(R.string.ok));
                universalDialog.show();
            }
            isTimerRunning = false;
            prgDialog.dismiss();

        });

    }

    private void handleFirebaseAuthError(String email) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SignInMethodQueryResult result = task.getResult();
                List<String> signInMethod = result.getSignInMethods();

                Util.showLog("SignInMethod  =" + signInMethod);
                if (signInMethod.contains(Constant.EMAILAUTH)) {
                    universalDialog.showErrorDialog("[" + email + "]" + getString(R.string.login__auth_email), getString(R.string.ok));
                    universalDialog.show();
                } else if (signInMethod.contains(Constant.GOOGLEAUTH)) {
                    universalDialog.showErrorDialog("[" + email + "]" + getString(R.string.login__auth_google), getString(R.string.ok));
                    universalDialog.show();
                }
            }
        });
    }

    String whatsappOTP;

    private void getWhatsappOtp(String number) {


        Constant.getUserViewModel(this).createWhatsappOtp(number).observe(this, whatsappOtpResponse -> {

            if (whatsappOtpResponse != null) {

                verificationId = whatsappOtpResponse.getOtp();
                whatsappOTP = whatsappOtpResponse.getOtp();
                binding.tvOtp.setText("Code is sent to " + ccp.getDefaultCountryCodeWithPlus() + binding.numberEt.getText().toString());
                binding.txtTabLogin.setText(getString(R.string.otp_verify));
                binding.numberLay.setVisibility(View.GONE);
                binding.otpLay.setVisibility(View.VISIBLE);
                startCountdown();

                prgDialog.dismiss();

            } else {

                Util.showToast(context, whatsappOtpResponse.getMessage());
                prgDialog.dismiss();

            }
        });


    }

    int sec = 60;
    boolean isTimerRunning = false;

    private void startCountdown() {
        sec = 60;
        isTimerRunning = true;
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isTimerRunning) {
                    sec--;
                    binding.recentBtn.setEnabled(false);
                    binding.recentBtn.setText(getString(R.string.resend_otp) + " (" + sec + ")");
                }
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                if (context != null) {
                    binding.recentBtn.setText(getString(R.string.resend_otp));
                    binding.recentBtn.setEnabled(true);
                }
            }
        }.start();
    }


    private void verifyAuth(PhoneAuthCredential credential) {
        prgDialog.show();

        mAuth.signInWithCredential(credential).addOnCompleteListener(context, task -> {

            if (task.isSuccessful()) {
                loginmodel.phoneNo = ccp.getDefaultCountryCodeWithPlus() + binding.numberEt.getText().toString();
                user = mAuth.getCurrentUser();

                addLoginInfo(null, null, null, loginmodel.phoneNo, Constant.PHONE);


            } else {
                Toast.makeText(context, "The verification code you have been entered incorrect !", Toast.LENGTH_SHORT).show();

            }
        });
    }

}