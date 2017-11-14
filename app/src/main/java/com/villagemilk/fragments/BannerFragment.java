package com.villagemilk.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.activities.PaymentsActivity;
import com.villagemilk.activities.ProductCategoryListActivity;
import com.villagemilk.beans.Banner;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.nostra13.universalimageloader.core.ImageLoader;
//import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by akash.mercer on 28-Jul-16.
 */
public class BannerFragment extends Fragment {
    private static final String TAG = "BannerFragment";

    private ImageView ivBanner;

    private TextView tvBannerTitle;

    ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int sectionNumber;

    public BannerFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static BannerFragment newInstance(int sectionNumber) {
        BannerFragment fragment = new BannerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.banner_item, container, false);

        ivBanner = (ImageView) rootView.findViewById(R.id.ivBanner);

//        tvBannerTitle = (TextView) rootView.findViewById(R.id.tvBannerTitle);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Banner> bannerList = ((HomeActivity) getActivity()).getBannerList();

        if (bannerList != null && !bannerList.isEmpty()){
            imageLoader.displayImage(bannerList.get(sectionNumber).getImageUrl(),ivBanner);
//            Picasso.with(getActivity()).load(bannerList.get(sectionNumber).getImageUrl()).into(ivBanner);
        }

    }
}
