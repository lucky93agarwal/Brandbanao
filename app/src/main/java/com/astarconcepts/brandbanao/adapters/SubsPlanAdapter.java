package com.astarconcepts.brandbanao.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.databinding.ItemSubsPlanBinding;
import com.astarconcepts.brandbanao.listener.ClickListener;
import com.astarconcepts.brandbanao.model.SubsPlanItem;
import com.astarconcepts.brandbanao.ui.activities.PlanDetailActivity;
import com.astarconcepts.brandbanao.ui.dialog.UniversalDialog;
import com.astarconcepts.brandbanao.utils.Constant;
import com.astarconcepts.brandbanao.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class SubsPlanAdapter extends RecyclerView.Adapter<SubsPlanAdapter.MyViewHolder> {

    Activity context;
    ClickListener<SubsPlanItem> listener;
    List<SubsPlanItem> subsPlanItemList = new ArrayList<>();


    private PreferenceManager preferenceManager;


    public SubsPlanAdapter(Activity context, ClickListener<SubsPlanItem> listener) {
        this.context = context;
        this.listener = listener;

        preferenceManager = new PreferenceManager(context);
    }

    public void subsPlanItemList(List<SubsPlanItem> subsPlanItemList) {
        this.subsPlanItemList = subsPlanItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubsPlanBinding binding = ItemSubsPlanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setSubdata(subsPlanItemList.get(position));


        if (subsPlanItemList.get(position).pointItemList != null) {

            SubPointAdapter adapter = new SubPointAdapter(subsPlanItemList.get(position).pointItemList);

            holder.binding.lvPoints.setAdapter(adapter);

            if (preferenceManager.getBoolean(Constant.IS_SUBSCRIBE) && subsPlanItemList.get(position).id.equals(preferenceManager.getString(Constant.PLAN_ID))) {

                holder.binding.purchasedTag.setVisibility(View.VISIBLE);

            } else {
                holder.binding.purchasedTag.setVisibility(View.GONE);
            }

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



        if (preferenceManager.getString(Constant.CURRENCY).equals("INR")) {
            holder.binding.tvCurrency.setText("₹");
            holder.binding.tvCurrency2.setText("₹");

        }

        holder.binding.cvSubs.setOnClickListener(v -> {

            Constant.subsPlanItem = subsPlanItemList.get(position);
            if (preferenceManager.getBoolean(Constant.IS_SUBSCRIBE) && subsPlanItemList.get(position).id.equals(preferenceManager.getString(Constant.PLAN_ID))) {
                UniversalDialog dialog = new UniversalDialog(context, false);

                dialog.showWarningDialog(context.getString(R.string.premium_user), context.getString(R.string.premium_user_message), context.getString(R.string.continue_ok), false);

                dialog.okBtn.setOnClickListener(v2 -> {

                    Intent intent = new Intent(context, PlanDetailActivity.class);
                    context.startActivity(intent);
                    dialog.cancel();
                });
                dialog.cancelBtn.setOnClickListener(v1 -> dialog.cancel());

                dialog.show();

            } else {
                Intent intent = new Intent(context, PlanDetailActivity.class);
                context.startActivity(intent);

            }



        });
    }

    @Override
    public int getItemCount() {
        if (subsPlanItemList != null && subsPlanItemList.size() > 0) {
            return subsPlanItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSubsPlanBinding binding;

        public MyViewHolder(ItemSubsPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
