package com.villagemilk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.activities.CartActivity;
import com.villagemilk.beans.Offer;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by akash.mercer on 30-Jul-16.
 */
public class OfferFragment extends Fragment {
    private static final String TAG = "OfferFragment";

    private ImageView ivOffer;

    private TextView tvOfferTitle;

    private TextView tvOfferDescription;

    private TextView tvAddProducts;

    private TextView tvNotNow;

    ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int sectionNumber;

    public OfferFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OfferFragment newInstance(int sectionNumber) {
        OfferFragment fragment = new OfferFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_offer, container, false);

        ivOffer = (ImageView) rootView.findViewById(R.id.ivOffer);

        tvOfferTitle = (TextView) rootView.findViewById(R.id.tvOfferTitle);

        tvOfferDescription = (TextView) rootView.findViewById(R.id.tvOfferDescription);

        tvAddProducts = (TextView) rootView.findViewById(R.id.tvAddProducts);

        tvNotNow = (TextView) rootView.findViewById(R.id.tvNotNow);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Offer offer = ((CartActivity) getActivity()).getOfferList().get(sectionNumber);

        imageLoader.displayImage(offer.getOfferImageUrl(),ivOffer);

        tvOfferTitle.setText(offer.getOfferTitle());

        tvOfferDescription.setText(offer.getOfferDescription());

        tvAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
