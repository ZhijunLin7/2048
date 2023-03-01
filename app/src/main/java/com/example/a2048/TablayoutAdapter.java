package com.example.a2048;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.a2048.RankingFragment.LightoutRankingFragment;
import com.example.a2048.RankingFragment.a2048RankingFragment;

public class TablayoutAdapter extends FragmentStateAdapter {


    public TablayoutAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new a2048RankingFragment();
            case 1:
                return new LightoutRankingFragment();
            default:
                return new a2048RankingFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
