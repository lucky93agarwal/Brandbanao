package com.astarconcepts.brandbanao.custom.animated_video.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.astarconcepts.brandbanao.R;
import com.astarconcepts.brandbanao.custom.animated_video.model.ModelAudio;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.viewHolder> {

    public OnItemClickListener onItemClickListener;
    Context context;
    ArrayList<ModelAudio> audioArrayList;


    public AudioAdapter(Context context, ArrayList<ModelAudio> audioArrayList) {
        this.context = context;
        this.audioArrayList = audioArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_list_item, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {


        holder.btnUse.setOnClickListener(view -> holder.itemView.performClick());

        holder.itemView.setOnClickListener(view -> {

            if (onItemClickListener != null) {

                onItemClickListener.onItemClick(position, audioArrayList);

            }


        });

        try{


        holder.title.setText(audioArrayList.get(position).getaudioTitle());

        holder.title.setSelected(true);


        }catch (Exception e){
            e.printStackTrace();

        }

    }

    @Override
    public int getItemCount() {
        return audioArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateList(ArrayList<ModelAudio> temp) {
        this.audioArrayList = temp;
        notifyDataSetChanged();

    }

    public void updateItems(ModelAudio audio, int pos) {

        audioArrayList.set(pos, audio);
        notifyItemChanged(pos);

    }

    public interface OnItemClickListener {
        void onItemClick(int pos, ArrayList<ModelAudio> audioArrayList);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout layout_parent;
        TextView btnUse;

        public viewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            layout_parent = itemView.findViewById(R.id.layout_parent);
            btnUse = itemView.findViewById(R.id.btn_use);

        }
    }
}