package com.example.a2048.RankingFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a2048.Adapter2048;
import com.example.a2048.AdapterLightout;
import com.example.a2048.R;
import com.example.a2048.SqlData;

public class LightoutRankingFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterLightout mAdapter;
    private SqlData sql;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_lightout_ranking_fragment, container, false);
        sql=new SqlData(this.getContext());
        recyclerView = view.findViewById(R.id.recyclerViewLightout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getItems(3);

        view.findViewById(R.id.lighout3x3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItems(3);
            }
        });
        view.findViewById(R.id.lighout4x4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItems(4);
            }
        });
        view.findViewById(R.id.lighout5x5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItems(5);
            }
        });
        return view;
    }
    public void getItems(int dimension) {
        mAdapter = new AdapterLightout(getContext(),sql,dimension);
        if (mAdapter.getItemCount() > 0) {
            view.findViewById(R.id.empty_view).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        }
        recyclerView.setAdapter(mAdapter);

    }
}