package com.villagemilk.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.villagemilk.R;
import com.villagemilk.activities.CartActivity;
import com.villagemilk.adapters.OffersListPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by akash.mercer on 30-Jul-16.
 */
public class OfferListDialog extends DialogFragment {
    private static final String TAG = "OfferListDialog";

    private ViewPager viewPagerOffers;

    private CirclePageIndicator circlePageIndicator;

    private OffersListPagerAdapter offersListPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_offer_list, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(true);

        viewPagerOffers = (ViewPager) rootView.findViewById(R.id.viewPagerOffers);

        circlePageIndicator = (CirclePageIndicator) rootView.findViewById(R.id.circlePageIndicator);

        offersListPagerAdapter = new OffersListPagerAdapter(getChildFragmentManager(), getActivity());

        viewPagerOffers.setAdapter(offersListPagerAdapter);
        viewPagerOffers.setOffscreenPageLimit(((CartActivity) getActivity()).getOfferList().size());
        viewPagerOffers.setClipToPadding(false);
        viewPagerOffers.setPadding(64, 0, 64, 0);
        viewPagerOffers.setPageMargin(32);

        circlePageIndicator.setViewPager(viewPagerOffers);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
