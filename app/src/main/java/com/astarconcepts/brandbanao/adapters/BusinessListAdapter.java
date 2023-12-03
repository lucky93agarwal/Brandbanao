package com.astarconcepts.brandbanao.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.api.ApiClient;
import com.astarconcepts.brandbanao.custom.poster.adapter.RecyclerItemClickListener;
import com.astarconcepts.brandbanao.model.BusinessItem;
import com.astarconcepts.brandbanao.ui.activities.AddBusinessActivity;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.MyUtils;
import com.astarconcepts.brandbanao.utils.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.ViewHolder> {

    ArrayList<BusinessItem> businessItems;
    Activity context;

    RecyclerItemClickListener.OnItemClickListener onItemClickListener;
    PreferenceManager preferenceManager;

    public BusinessListAdapter(Activity context, ArrayList<BusinessItem> businessItems, RecyclerItemClickListener.OnItemClickListener onItemClickListener) {

        this.businessItems = businessItems;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        preferenceManager = new PreferenceManager(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);

        String str = businessItems.get(position).logo;


        Glide.with(context).load(str)
                .skipMemoryCache(false)
                .placeholder(R.drawable.ic_profile)
                .into(holder.circularImageView);

        if (businessItems.get(position).isDefault) {

            holder.rawLayout.setBackgroundResource(R.drawable.raw_business_defaolt_bg);

        } else {

            holder.rawLayout.setBackgroundResource(R.drawable.edit_bg);
        }

        holder.tvBusinessName.setText(businessItems.get(position).name);
        holder.tvBusinessNumber.setText(businessItems.get(position).businesscategory);


        holder.cvBusinessEdit.setOnClickListener(view -> {

            Constant.businessItem = businessItems.get(position);

            Intent intent = new Intent(context, AddBusinessActivity.class);
            intent.putExtra("Action", "Update");
            context.startActivity(intent);
        });


        holder.rawLayout.setOnClickListener(v -> {

            Constant.businessItem = businessItems.get(position);

            setupDefaultBusiness(holder, position);


        });
    }


    private void setupDefaultBusiness(@NonNull ViewHolder holder, int position) {

        UniversalDialog dialog = new UniversalDialog(context, false);

        dialog.showLoadingDialog(context,context.getString(R.string.setting_default));

        ApiClient.getApiDataService().setDefaultBusiness("default", preferenceManager.getString(Constant.USER_ID), businessItems.get(position).businessid).
                enqueue(new Callback<BusinessItem>() {
                    @Override
                    public void onResponse(Call<BusinessItem> call, Response<BusinessItem> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            BusinessItem businessItem = response.body();


                            if (businessItems.get(position).isDefault) {

                                holder.rawLayout.setBackgroundResource(R.drawable.raw_business_defaolt_bg);

                            } else {

                                holder.rawLayout.setBackgroundResource(R.drawable.edit_bg);
                            }

                            onItemClickListener.onItemClick(holder.itemView, position);


                            Constant.setDefaultBusiness(context, businessItem);

                            dialog.dissmissLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<BusinessItem> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return businessItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBusinessName;
        public TextView tvBusinessNumber;
        public TextView tvEdit;
        private CircularImageView circularImageView;
        private LinearLayout rawLayout;
        private LinearLayout cvBusinessEdit;

        public ViewHolder(View v) {

            super(v);

            circularImageView = v.findViewById(R.id.iv_business);
            tvBusinessName = v.findViewById(R.id.tv_business_name);
            tvBusinessNumber = v.findViewById(R.id.tv_business_number);
            cvBusinessEdit = v.findViewById(R.id.iv_edit);
            tvEdit = v.findViewById(R.id.tv_edit);
            rawLayout = v.findViewById(R.id.raw_layout);

        }
    }


}
