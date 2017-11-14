package com.villagemilk.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.villagemilk.activities.CartActivity;
import com.villagemilk.fragments.OfferFragment;

/**
 * Created by akash.mercer on 30-Jul-16.
 */
public class OffersListPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "OffersListPagerAdapter";

    private Context context;

    public OffersListPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return OfferFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return ((CartActivity) context).getOfferList().size();
    }
}
