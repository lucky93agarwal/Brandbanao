package com.astarconcepts.brandbanao.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.databinding.ItemServicesBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.ServiceItem;

import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {

    Activity context;
    ClickListener<ServiceItem> listener;
    List<ServiceItem> serviceItemList = new ArrayList<>();


    private PreferenceManager preferenceManager;


    public ServicesAdapter(Activity context, ClickListener<ServiceItem> listener) {
        this.context = context;
        this.listener = listener;

        preferenceManager = new PreferenceManager(context);
    }

    public void setServiceItemList(List<ServiceItem> subsPlanItemList) {
        this.serviceItemList = subsPlanItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServicesBinding binding = ItemServicesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setServiceData(serviceItemList.get(position));


        if (serviceItemList.get(position).pointItemList != null) {

            ServicePointAdapter adapter = new ServicePointAdapter(serviceItemList.get(position).pointItemList);

            holder.binding.lvPoints.setAdapter(adapter);
            holder.binding.lvPoints.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                }
            });

            holder.binding.lvPoints.setNestedScrollingEnabled(false);
        }


        holder.binding.btnService.setOnClickListener(v -> {

            if (serviceItemList.get(position).type.contains("contact")) {

                String url = "https://api.whatsapp.com/send?phone=" + preferenceManager.getString(Constant.WHATSAPP_NUMBER);
                try {
                    PackageManager pm = context.getPackageManager();
                    if (context.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.whatsapp") != null){

                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);

                    } else if (context.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b") != null) {

                        pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES);
                    }

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

            }
            }else {

                context.startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(serviceItemList.get(position).getUrl()), "text/plain").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }

        });
    }

    @Override
    public int getItemCount() {
        if (serviceItemList != null && serviceItemList.size() > 0) {
            return serviceItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemServicesBinding binding;

        public MyViewHolder(ItemServicesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
