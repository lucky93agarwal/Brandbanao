package com.astarconcepts.brandbanao.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.databinding.SongListItemBinding;
import com.astarconcepts.brandbanao.model.MusicModel;

import java.util.ArrayList;
import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<MusicModel> list;
    private MusicPagerAdapter.OnMusicSelect listener;
    Activity context;
    int selectedPosition = 500;

    public MusicAdapter(Activity context, List<MusicModel> list, MusicPagerAdapter.OnMusicSelect listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SongListItemBinding binding = SongListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicModel model = list.get(position);
        holder.binding.setMusic(model);

        if (this.selectedPosition == position) {
            holder.binding.playBtn.setImageDrawable(context.getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_pause));
        } else {
            holder.binding.playBtn.setImageDrawable(context.getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_play));
        }
        holder.binding.playBtn.setOnClickListener(view -> {
            if (selectedPosition == position) {
                selectedPosition = 500;
                listener.onStop();
                holder.binding.playBtn.setBackground(context.getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_play));
            } else {
                listener.onPlay(model.audio_url);
                MusicAdapter recyclerOverLayAdapter = MusicAdapter.this;
                recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
                MusicAdapter recyclerOverLayAdapter2 = MusicAdapter.this;
                selectedPosition = position;
                recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
            }
        });
        holder.binding.title.setText(model.title);

        holder.binding.btnUse.setOnClickListener(view -> listener.onSelect(model.audio_url));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<MusicModel> temp) {
        this.list = temp;
        notifyDataSetChanged();


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        SongListItemBinding binding;

        ViewHolder(@NonNull SongListItemBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;

        }
    }

}