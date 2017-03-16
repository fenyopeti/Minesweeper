package com.example.fenyves.minesweeper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fenyves on 2016. 12. 11..
 */
public class GamerAdapter extends RecyclerView.Adapter<GamerAdapter.GamerViewHolder> {
    private final List<Gamer> gamers;

    GamerAdapter(ArrayList<Gamer> gamers){
        this.gamers = gamers;
    }

    @Override
    public GamerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamer_view, parent, false);
        GamerViewHolder viewHolder = new GamerViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GamerViewHolder holder, int position) {
        holder.nameLabel.setText(gamers.get(position).getName() + " - ");

        long sec = gamers.get(position).getTime();
        String time = (sec / 60) + ":" + (sec % 60);

        holder.timeLabel.setText(time);
        holder.numLabel.setText(String.valueOf(position + 1) + ".  ");
    }


    @Override
    public int getItemCount() {
        return gamers.size();
    }

    public class GamerViewHolder extends RecyclerView.ViewHolder{
        TextView numLabel;
        TextView nameLabel;
        TextView timeLabel;
        public GamerViewHolder(View itemView) {
            super(itemView);
            nameLabel = (TextView) itemView.findViewById(R.id.name_label);
            timeLabel = (TextView) itemView.findViewById(R.id.time_label);
            numLabel = (TextView) itemView.findViewById(R.id.num_label);
        }
    }
}
