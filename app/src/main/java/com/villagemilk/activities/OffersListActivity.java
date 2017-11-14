package com.villagemilk.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.villagemilk.R;
import com.villagemilk.adapters.OffersListPagerAdapter;
import com.villagemilk.beans.Offer;
import com.villagemilk.datamanagers.OfferDataManager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class OffersListActivity extends BaseActivity {
    private static final String TAG = "OffersListActivity";

    private ViewPager viewPagerOffers;

    private CirclePageIndicator circlePageIndicator;

    private OffersListPagerAdapter offersListPagerAdapter;

    private List<Offer> offerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_list);

        viewPagerOffers = (ViewPager) findViewById(R.id.viewPagerOffers);

        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);

        initViews();
    }

    private void initViews(){
        offerList = new OfferDataManager(6).getOfferList();

        offersListPagerAdapter = new OffersListPagerAdapter(getSupportFragmentManager(), this);

        viewPagerOffers.setAdapter(offersListPagerAdapter);
        viewPagerOffers.setOffscreenPageLimit(offerList.size());
        viewPagerOffers.setClipToPadding(false);
        viewPagerOffers.setPadding(64, 0, 64, 0);
        viewPagerOffers.setPageMargin(16);

        circlePageIndicator.setViewPager(viewPagerOffers);
    }

    public List<Offer> getOfferList() {
        return offerList;
    }
}
