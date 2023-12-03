package com.astarconcepts.brandbanao.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.arthenica.mobileffmpeg.FFmpeg.cancel;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.astarconcepts.brandbanao.AppConfig;
import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.api.ApiResponse;
import com.astarconcepts.brandbanao.databinding.ActivityPlanBinding;
import com.astarconcepts.brandbanao.model.CouponItem;
import com.astarconcepts.brandbanao.model.PaytmResponse;
import com.astarconcepts.brandbanao.model.StripeResponse;
import com.astarconcepts.brandbanao.model.SubsPlanItem;
import com.astarconcepts.brandbanao.model.UserItem;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.astarconcepts.brandbanao.utils.Util;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class PlanDetailActivity extends AppCompatActivity implements PaymentResultListener {
    public int finalPrice;
    public SubsPlanItem subsPlanItem;
    ActivityPlanBinding binding;
    PreferenceManager preferenceManager;
    String planId = "";
    String planPrice = "";
    UniversalDialog universalDialog;
    private String paymentId;
    String stripeOrderID = "";
    String paytmOrderID = "";
    String customerID = "";
    String publisherKey = "";
    String client_secret = "";
    Stripe stripe;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;
    PaymentSheet paymentSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);

        universalDialog = new UniversalDialog(this, false);
        subsPlanItem = Constant.subsPlanItem;

        if (subsPlanItem == null) {

            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            finish();

        } else {

            finalPrice = Integer.parseInt(subsPlanItem.planPrice);
            planPrice = subsPlanItem.planPrice;
            planId = subsPlanItem.id;

        }

        binding.toolbar.toolName.setText(R.string.payments);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        if (preferenceManager.getString(Constant.RazorPay).equals(AppConfig.ONE)) {
            Checkout.preload(this);
            binding.cbRazorPay.setVisibility(VISIBLE);
        }

        if (preferenceManager.getString(Constant.Stripe).equals(AppConfig.ONE)) {
            paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
            binding.cbStripe.setVisibility(VISIBLE);
        }

        if (preferenceManager.getString(Constant.Paytm).equals("1")) {
            binding.cbPaytm.setVisibility(VISIBLE);
        }

        if (preferenceManager.getString(Constant.RazorPay).equals("1")) {
            binding.cbRazorPay.setVisibility(VISIBLE);
        }



        binding.tvPlanName.setText(subsPlanItem.planName);
        binding.tvOriPrice.setText(subsPlanItem.planPrice);
        binding.tvDiscount.setText(subsPlanItem.planDiscount);
        binding.tvPrice.setText(subsPlanItem.planPrice);
        binding.tvOriPrice2.setText(subsPlanItem.planPrice);

        binding.rlOpen.setVisibility(View.VISIBLE);


        binding.btnApply.setOnClickListener(v -> {

            if (binding.etPromo.getText().toString().equals("")) {
                Util.showToast(PlanDetailActivity.this, "Enter Code");
                return;
            }

            binding.btnApply.setEnabled(false);
            binding.btnApply.setText("Checking...");
            binding.cbRazorPay.setEnabled(false);

            checkCoupon(preferenceManager.getString(Constant.USER_ID), binding.etPromo.getText().toString());

        });

        if (preferenceManager.getString(Constant.CURRENCY).equals("USD")) {
            binding.tvCurrency.setText("$");
            binding.tvCurrency1.setText("$");
            binding.tvCurrency2.setText("$");
            binding.tvCurrency3.setText("$");

        }

        if (preferenceManager.getString(Constant.RazorPay).equals("1")) {

           binding.cbRazorPay.setBackground(getDrawable(R.drawable.round_button_paymant));

           binding.checkRazor.setButtonTintList(ColorStateList.valueOf(getColor(R.color.white)));
           binding.cardRz.setTextColor(getColor(R.color.white));
           binding.checkRazor.setChecked(true);

        } else if (preferenceManager.getString(Constant.Paytm).equals("1")) {

            binding.cbPaytm.setBackground(getDrawable(R.drawable.round_button_paymant));
            binding.checkPaytm.setButtonTintList(ColorStateList.valueOf(getColor(R.color.white)));
            binding.cardPt.setTextColor(getColor(R.color.white));
            binding.checkPaytm.setChecked(true);
        } else if (preferenceManager.getString(Constant.Stripe).equals("1")) {

           binding.cbStripe.setBackground(getDrawable(R.drawable.round_button_paymant));
           binding.checkStripe.setButtonTintList(ColorStateList.valueOf(getColor(R.color.white)));
           binding.cardSt.setTextColor(getColor(R.color.white));
           binding.checkStripe.setChecked(true);

        }



       binding.checkRazor.setOnClickListener(v -> binding.cbRazorPay.performClick());
       binding.checkStripe.setOnClickListener(v -> binding.cbStripe.performClick());
       binding.checkPaytm.setOnClickListener(v -> binding.cbPaytm.performClick());


       binding.cbRazorPay.setOnClickListener(v -> setButtonStyle("RAZORPAY"));
       binding.cbStripe.setOnClickListener(v -> setButtonStyle("STRIPE"));
       binding.cbPaytm.setOnClickListener(v -> setButtonStyle("PAYTM"));

        binding.btnContinue.setOnClickListener(v -> {

            binding.pbPayment.setVisibility(VISIBLE);
            binding.btnContinue.setVisibility(GONE);

            if (finalPrice == 0) {
                createTransactions("100%_coupon_use", "Free");
            } else {
                if (binding.checkRazor.isChecked()) {
                    startPayment(finalPrice, preferenceManager.getString(Constant.RAZORPAY_KEY_ID));
                }   else if (binding.checkPaytm.isChecked()) {
                    createPaytm();
                } else if (binding.checkStripe.isChecked()) {
                    createStripePayment();
                }
            }
        });


    }

    private void checkCoupon(String userId, String couponCode) {


        Constant.getHomeViewModel(this).validateCoupon(userId,couponCode).observe(this, couponItem -> {

            if (couponItem !=null){

                double discountPrice = Double.parseDouble(couponItem.discount) * finalPrice / 100;

                double price = finalPrice - discountPrice;

                binding.tvPrice.setText("" + price);

                finalPrice = (int) price;

                planPrice = String.valueOf(finalPrice);

                binding.csApplied.setVisibility(View.VISIBLE);

                binding.tvCode.setText(binding.etPromo.getText().toString());
                binding.tvCodeDec.setText(couponItem.discount + "% " + getString(R.string.discount_on)
                        + " " + binding.tvPlanName.getText());
                binding.btnApply.setEnabled(true);
                binding.cbRazorPay.setEnabled(true);


            }


        });



        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<CouponItem> response = ApiClient.getApiDataService().validateCoupon(
                        userId,
                        couponCode).execute();


                // Wrap with APIResponse Class
                ApiResponse<CouponItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("yes " + apiResponse.body);

                    runOnUiThread(() -> {

                        double discountPrice = Double.parseDouble(apiResponse.body.discount) * finalPrice / 100;

                        double price = finalPrice - discountPrice;

                        binding.tvPrice.setText("" + price);

                        finalPrice = (int) price;

                        planPrice = String.valueOf(finalPrice);

                        binding.csApplied.setVisibility(View.VISIBLE);

                        binding.tvCode.setText(binding.etPromo.getText().toString());
                        binding.tvCodeDec.setText(apiResponse.body.discount + "% " + getString(R.string.discount_on)
                                + " " + binding.tvPlanName.getText());
                        binding.btnApply.setEnabled(true);
                        binding.cbRazorPay.setEnabled(true);
                    });

                } else {
                    runOnUiThread(() -> {

                        binding.tvError.setText(apiResponse.errorMessage);
                        binding.tvError.setVisibility(View.VISIBLE);

                        binding.btnApply.setEnabled(true);
                        binding.cbRazorPay.setEnabled(true);
                        binding.btnApply.setText(getString(R.string.apply));
                    });
                }

            } catch (Exception e) {
                runOnUiThread(() -> {

                    binding.tvError.setText("Coupon Code Not Valid");
                    binding.tvError.setVisibility(View.VISIBLE);

                    binding.btnApply.setEnabled(true);
                    binding.cbRazorPay.setEnabled(true);
                    binding.btnApply.setText(getString(R.string.apply));
                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });

    }

    private void startPayment(int planPrice, String key) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(key);

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", preferenceManager.getString(Constant.USER_NAME));
            options.put("description", "Charge Of Plan");
            options.put("theme.color", "#f59614");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("currency", "INR");
            options.put("amount", planPrice * 100);//pass amount in currency subunits
            options.put("prefill.email", preferenceManager.getString(Constant.USER_EMAIL));
            if (preferenceManager.getString(Constant.USER_PHONE) != null && !preferenceManager.getString(Constant.USER_PHONE).equals("")) {
                options.put("prefill.contact", preferenceManager.getString(Constant.USER_PHONE));
            }
            checkout.open(activity, options);

        } catch (Exception e) {
            e.printStackTrace();
            Util.showErrorLog("Error in starting Razorpay Checkout", e);
        }
    }
    public void setButtonStyle(String type) {
       // int primaryColor = getColor(R.color.active_color);
        int grayColor = getColor(R.color.white);
        int accGrayColor = getColor(R.color.acc_gray);
        int whiteColor = getColor(R.color.white);
        int tgCheckColor = getColor(R.color.tg_check);

        button(R.drawable.rounded_border2x, R.drawable.rounded_border2x, R.drawable.rounded_border2x);


        binding.checkRazor.setButtonTintList(ColorStateList.valueOf(tgCheckColor));
        binding.checkPaytm.setButtonTintList(ColorStateList.valueOf(tgCheckColor));
        binding.checkStripe.setButtonTintList(ColorStateList.valueOf(tgCheckColor));



        if (type.equals("RAZORPAY")) {
            button(R.drawable.round_button_paymant, R.drawable.rounded_border2x, R.drawable.rounded_border2x);
            binding.checkRazor.setButtonTintList(ColorStateList.valueOf(whiteColor));


            binding.cardRz.setTextColor(whiteColor);
            binding.cardSt.setTextColor(accGrayColor);
            binding.cardPt.setTextColor(accGrayColor);

            binding.checkRazor.setChecked(true);
            binding.checkPaytm.setChecked(false);
            binding.checkStripe.setChecked(false);

        } else if (type.equals("PAYTM")) {
            button(R.drawable.rounded_border2x, R.drawable.round_button_paymant, R.drawable.rounded_border2x);

            binding.checkPaytm.setButtonTintList(ColorStateList.valueOf(whiteColor));

            binding.cardRz.setTextColor(accGrayColor);
            binding.cardSt.setTextColor(accGrayColor);
            binding.cardPt.setTextColor(whiteColor);

            binding.checkRazor.setChecked(false);
            binding.checkStripe.setChecked(false);
            binding.checkPaytm.setChecked(true);

        } else if (type.equals("STRIPE")) {
            button(R.drawable.rounded_border2x, R.drawable.rounded_border2x, R.drawable.round_button_paymant);
            binding.checkStripe.setButtonTintList(ColorStateList.valueOf(whiteColor));

            binding.cardRz.setTextColor(accGrayColor);
            binding.cardSt.setTextColor(whiteColor);
            binding.cardPt.setTextColor(accGrayColor);

            binding.checkStripe.setChecked(true);
            binding.checkRazor.setChecked(false);
            binding.checkPaytm.setChecked(false);
        }
    }

    private void button(int round_button_paymant, int rounded_border2x, int rounded_border2x1) {
        binding.cbRazorPay.setBackground(getDrawable(round_button_paymant));
        binding.cbPaytm.setBackground(getDrawable(rounded_border2x));
        binding.cbStripe.setBackground(getDrawable(rounded_border2x1));
    }


    @Override
    public void onPaymentSuccess(String paymentId) {
        binding.pbPayment.setVisibility(GONE);
        binding.btnContinue.setVisibility(VISIBLE);
        createTransactions(paymentId, "RazorPay");
    }

    @Override
    public void onPaymentError(int i, String s) {
        String message = "";
        if (i == Checkout.PAYMENT_CANCELED) {
            message = "The user canceled the payment.";
        } else if (i == Checkout.NETWORK_ERROR) {
            message = "There was a network error, for example, loss of internet connectivity.";
        } else if (i == Checkout.INVALID_OPTIONS) {
            message = "An issue with options passed in checkout.open .";
        } else if (i == Checkout.TLS_ERROR) {
            message = "The device does not support TLS v1.1 or TLS v1.2.";
        } else {
            message = "Unknown Error";
        }
        binding.pbPayment.setVisibility(GONE);
        binding.btnContinue.setVisibility(VISIBLE);
        universalDialog.cancel();
        Util.showLog(i + " " + s);
        universalDialog.showErrorDialog(message, getString(R.string.ok));
        universalDialog.show();
    }

    private void createTransactions(String paymentId, String type) {

        Toast.makeText(PlanDetailActivity.this, "Payment DONE Successfully!", Toast.LENGTH_SHORT).show();

        Constant.getUserViewModel(this).storeTransaction(preferenceManager.getString(Constant.USER_ID), planId, paymentId, String.valueOf(finalPrice),
                binding.tvCode.getText().toString(),type).observe(this, apiStatus -> {

                    if (apiStatus != null) {
                        binding.pbPayment.setVisibility(GONE);
                        getPlanDetails();
                        universalDialog.showSuccessDialog(apiStatus.message, getString(R.string.ok));
                        universalDialog.show();
                    }

                });
    }


    private void getPlanDetails() {

        Constant.getUserViewModel(this).getUserById(preferenceManager.getString(Constant.USER_ID)).observe(this, userItem -> {

            if (userItem != null) {

                preferenceManager.setString(Constant.PLAN_NAME, userItem.planName);
                preferenceManager.setString(Constant.PLAN_ID, userItem.planId);
                preferenceManager.setString(Constant.PLAN_DURATION, userItem.planDuration);
                preferenceManager.setString(Constant.PLAN_START_DATE,userItem.planStartDate);
                preferenceManager.setString(Constant.PLAN_END_DATE, userItem.planEndDate);
                preferenceManager.setBoolean(Constant.IS_SUBSCRIBE, userItem.isSubscribed);
                preferenceManager.setInt(Constant.USER_BUSINESS_LIMIT, userItem.businessLimit);

                Intent intent = new Intent(PlanDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }


        });


    }


    //**** Stripe


    private void createStripePayment() {
        stripeOrderID = "STRIPE_" + System.currentTimeMillis();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<StripeResponse> response = ApiClient.getApiDataService().createStripePayment(
                        String.valueOf(finalPrice)).execute();

                // Wrap with APIResponse Class
                ApiResponse<StripeResponse> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("" + apiResponse.body + " " + apiResponse.body.toString());

                    runOnUiThread(() -> {
                        publisherKey = response.body().publishableKey;

                        Util.showLog("KEY: " + publisherKey + " " + response.body().customer + " "
                                + response.body().ephemeralKey);

                        customerConfig = new PaymentSheet.CustomerConfiguration(
                                response.body().customer,
                                response.body().ephemeralKey
                        );
                        paymentIntentClientSecret = response.body().paymentIntent;
                        PaymentConfiguration.init(getApplicationContext(), publisherKey);
                        presentPaymentSheet();
                    });

                } else {
                    runOnUiThread(() -> {
                        binding.pbPayment.setVisibility(GONE);
                        binding.btnContinue.setVisibility(VISIBLE);
                        universalDialog.cancel();
                        universalDialog.showErrorDialog(apiResponse.errorMessage, "Ok");
                        universalDialog.show();
                    });
                }

            } catch (IOException e) {
                runOnUiThread(() -> {

                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });
    }

    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder(getString(R.string.app_name))
                .customer(customerConfig)
                // Set `allowsDelayedPaymentMethods` to true if your business can handle payment methods
                // that complete payment after a delay, like SEPA Debit and Sofort.
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        // implemented in the next steps
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            binding.pbPayment.setVisibility(GONE);
            binding.btnContinue.setVisibility(VISIBLE);
            Util.showLog("Canceled");
            universalDialog.cancel();
            universalDialog.showErrorDialog("Payment Canceled By User", "Ok");
            universalDialog.show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            binding.pbPayment.setVisibility(GONE);
            binding.btnContinue.setVisibility(VISIBLE);
            Util.showLog("Got error: " + ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            universalDialog.cancel();
            universalDialog.showErrorDialog("" + ((PaymentSheetResult.Failed) paymentSheetResult).getError(), "Ok");
            universalDialog.show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Util.showLog("Completed");
            createTransactions(stripeOrderID, "Stripe");
        }
    }

    //Paytm
    private void createPaytm() {
        paytmOrderID = MyUtils.randomAlphaNumeric(20);
        customerID = MyUtils.randomAlphaNumeric(10);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<PaytmResponse> response = ApiClient.getApiDataService().createPaytmPayment(
                        String.valueOf(finalPrice),
                        paytmOrderID,
                        customerID).execute();

                // Wrap with APIResponse Class
                ApiResponse<PaytmResponse> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("" + apiResponse.body + " " + apiResponse.body.toString());

                    runOnUiThread(() -> startPaytmPayment(response.body().txnToken, response.body().callback_url));

                } else {
                   runOnUiThread(() -> {
                       binding.pbPayment.setVisibility(GONE);
                       binding.btnContinue.setVisibility(VISIBLE);
                        universalDialog.cancel();
                        universalDialog.showErrorDialog(apiResponse.errorMessage, "Ok");
                        universalDialog.show();
                   });
                }

            } catch (IOException e) {
                runOnUiThread(() -> {

                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });
    }

    private void startPaytmPayment(String txnToken, String callback_url) {
        PaytmOrder paytmOrder = new PaytmOrder(paytmOrderID, preferenceManager.getString(Constant.PAYTM_ID),
                txnToken, String.valueOf(finalPrice), callback_url);

        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(@Nullable Bundle bundle) {
                Util.showLog("SS: " + bundle.toString());
                if (bundle.getString("STATUS").equals("TXN_FAILURE")) {
                    showError(bundle.getString("RESPMSG"));
                } else if (bundle.getString("STATUS").equals("TXN_SUCCESS")) {
                    createTransactions(paytmOrderID, "Paytm");
                }
            }

            @Override
            public void networkNotAvailable() {
                showError("Internet is not available");
            }

            @Override
            public void onErrorProceed(String s) {
                showError(s);
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                showError("Transaction Failed");
            }

            @Override
            public void someUIErrorOccurred(String s) {
                showError("Transaction Error");
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                showError("Transaction Error");
            }

            @Override
            public void onBackPressedCancelTransaction() {
                showError("Transaction cancel");
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                showError("Transaction cancel");
            }
        });
        transactionManager.setAppInvokeEnabled(false);
        transactionManager.startTransaction(this, 50000);
    }

    private void showError(String detail) {
       binding.pbPayment.setVisibility(GONE);
       binding.btnContinue.setVisibility(VISIBLE);
        universalDialog.cancel();
        universalDialog.showErrorDialog(detail, "Ok");
        universalDialog.show();
    }

}