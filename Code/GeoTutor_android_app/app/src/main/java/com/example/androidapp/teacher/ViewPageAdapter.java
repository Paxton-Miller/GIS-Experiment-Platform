package com.example.androidapp.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;

    public ViewPageAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList == null ? null : fragmentList.get(position); //获取fragment
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size(); //获取fragment数量
    }
}
