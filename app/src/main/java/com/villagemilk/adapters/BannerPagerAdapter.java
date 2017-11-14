package com.villagemilk.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.villagemilk.fragments.BannerFragment;
import com.villagemilk.beans.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 28-Jul-16.
 */
public class BannerPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "BannerPagerAdapter";

    private List<Banner> bannerList = new ArrayList<>();

    public BannerPagerAdapter(FragmentManager fm, List<Banner> bannerList) {
        super(fm);

        this.bannerList = bannerList;
    }

    @Override
    public Fragment getItem(int position) {
        return BannerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }
}
