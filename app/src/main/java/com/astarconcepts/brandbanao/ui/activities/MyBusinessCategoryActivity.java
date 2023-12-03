package com.astarconcepts.brandbanao.ui.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.adapters.SelectCategoryAdapter;
import com.astarconcepts.brandbanao.databinding.ActivityMyBusinessCategoryBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.CategoryItem;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MyBusinessCategoryActivity extends AppCompatActivity implements ClickListener<CategoryItem> {

    ActivityMyBusinessCategoryBinding binding;
    SelectCategoryAdapter adapter;

    PreferenceManager preferenceManager;
    List<CategoryItem> categoryItems;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBusinessCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        editTextSearchBar();
        setData();

        categoryItems = new ArrayList<>();

        shimmer(View.VISIBLE);
        binding.toolbar.toolName.setText(getResources().getString(R.string.business_category));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.lstbusinessList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        adapter = new SelectCategoryAdapter(this, this, false);
        binding.lstbusinessList.setAdapter(adapter);



       /* binding.etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {


            if (i == EditorInfo.IME_ACTION_SEARCH) {

                if (binding.etSearch.getText() != null && !binding.etSearch.getText().toString().isEmpty()) {

                    filter(binding.etSearch.getText().toString());

                } else {
                    Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
                }


            }

            return false;
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //code
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.toString() != null) {
                    filter(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //code
            }
        });

*/
    }


    private void editTextSearchBar() {
        binding.etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {

            if (i == EditorInfo.IME_ACTION_SEARCH) {

                Toast.makeText(this, "category " + categoryItems, Toast.LENGTH_SHORT).show();

                if (binding.etSearch.getText() != null && !binding.etSearch.getText().toString().isEmpty()) {

                    filter(binding.etSearch.getText().toString());


                } else {
                    Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
                }


            }

            return false;
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //code
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                filter(charSequence.toString());


            }

            @Override
            public void afterTextChanged(Editable editable) {
                //code
            }
        });
    }


    void filter(String text) {

        ArrayList<CategoryItem> temp = new ArrayList();

        for (CategoryItem d : categoryItems) {

            if (d != null) {

                if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }

        }
        adapter.setCategories(temp);
    }


    private void setData() {

        Constant.getHomeViewModel(this).getCategories("business").observe(this, categoryItems1 -> {

            if (categoryItems1 != null && !categoryItems1.isEmpty()) {

                categoryItems = categoryItems1;

                adapter.setCategories(categoryItems);

                shimmer(View.GONE);
                binding.shimmerViewContainer.stopShimmer();
            } else {

                binding.llNotFound.setVisibility(View.VISIBLE);
            }


        });


    }

    @Override
    public void onClick(CategoryItem data) {

        if (preferenceManager.getString(Constant.BUSINESS_CATEGORY_NAME).isEmpty()) {

            changeCategory(data);

        } else {

            showDialog(data);
        }

    }

    private void changeCategory(CategoryItem data) {
        preferenceManager.setString(Constant.BUSINESS_CATEGORY_NAME, data.getName());
        preferenceManager.setString(Constant.BUSINESS_CATEGORY_ID, data.getId());

        setResult(RESULT_OK);
        finish();
    }

    private void showDialog(CategoryItem data) {

        dialog = new Dialog(MyBusinessCategoryActivity.this);
        this.dialog.requestWindowFeature(1);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView dialogTitleTextView = dialog.findViewById(R.id.dialogTitleTextView);
        TextView dialogMessageTextView = dialog.findViewById(R.id.dialogMessageTextView);

        dialogTitleTextView.setText("Change Business Category?");
        dialogMessageTextView.setText("You're about to change business\nCategory to : " + data.name);

        Button dialogOkButton = dialog.findViewById(R.id.dialogOkButton);

        Button dialogCancelButton = dialog.findViewById(R.id.dialogCancelButton);

        dialogOkButton.setOnClickListener(view -> {

            changeCategory(data);
            dialog.dismiss();

        });

        dialogCancelButton.setOnClickListener(view -> {

            setResult(RESULT_CANCELED);
            finish();
            dialog.dismiss();

        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void shimmer(int gone) {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(gone);
    }

}