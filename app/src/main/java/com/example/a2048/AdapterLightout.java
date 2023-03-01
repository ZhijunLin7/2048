package com.example.a2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterLightout extends RecyclerView.Adapter<AdapterLightout.DatalightoutViewHolder>{

    class DatalightoutViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView dimension;
        TextView pasos;
        TextView tiempo;

        public DatalightoutViewHolder(View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.User_light);
            dimension=itemView.findViewById(R.id.dimension_light);
            pasos=itemView.findViewById(R.id.pasos_restante);
            tiempo=itemView.findViewById(R.id.tiempo_restante);
        }
    }

    private final LayoutInflater mInflater;
    Context mContext;
    SqlData mDB;
    int dimension;

    public AdapterLightout(Context context, SqlData db,int dimension) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
        this.dimension=dimension;
    }
    @NonNull
    @Override

    public DatalightoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.ranking_item_lightout, parent, false);
        return new DatalightoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DatalightoutViewHolder holder, int position) {
        Datalightout current = mDB.rankinglightout(position,dimension);
        holder.userName.setText(current.getName());
        holder.dimension.setText(String.valueOf(current.getDimension()));
        holder.pasos.setText(String.valueOf(current.getPasos_restante()));
        holder.tiempo.setText(String.valueOf(current.getTiempo_restante()));
    }

    @Override
    public int getItemCount() {
        return (int) mDB.countdatalightout(dimension);
    }

}
