package com.example.a2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter2048 extends RecyclerView.Adapter<Adapter2048.Data2048ViewHolder>{

    class Data2048ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView dimension;
        TextView puntos;

        public Data2048ViewHolder(View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.User_2048);
            dimension=itemView.findViewById(R.id.dimension_2048);
            puntos=itemView.findViewById(R.id.puntos_2048);
        }
    }

    private final LayoutInflater mInflater;
    Context mContext;
    SqlData mDB;
    int dimension;

    public Adapter2048(Context context, SqlData db,int dimension) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
        this.dimension=dimension;
    }

    @NonNull
    @Override
    public Adapter2048.Data2048ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.ranking_item_a2048, parent, false);
        return new Data2048ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Data2048ViewHolder holder, int position) {
        Data2048 current = mDB.ranking2048(position,dimension);
        holder.userName.setText(current.getName());
        holder.dimension.setText(String.valueOf(current.getDimension()));
        holder.puntos.setText(String.valueOf(current.getPuntos()));
    }

    @Override
    public int getItemCount() {
        return (int) mDB.countdata2048(dimension);
    }
}
