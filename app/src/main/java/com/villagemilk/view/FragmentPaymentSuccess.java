package com.villagemilk.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.villagemilk.R;
import com.villagemilk.databinding.FragmentPaymentSuccessBinding;
import com.villagemilk.model.BillingAddress;

/**
 * Created by dailyninja on 22/07/17.
 */

public class FragmentPaymentSuccess extends Fragment {

    Context mContext;

    BillingAddress billingAddress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    public static FragmentPaymentSuccess newInstance(String transactionId, Double amount, BillingAddress billingAddress) {
        
        Bundle args = new Bundle();
        args.putString("transactionId",transactionId);
        args.putDouble("amount",amount);
        args.putSerializable("bilingAddress",billingAddress);
        
        FragmentPaymentSuccess fragment = new FragmentPaymentSuccess();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentPaymentSuccessBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.fragment_payment_success,container,false);

        billingAddress = (BillingAddress)getArguments().getSerializable("billingAddress");

        binding.tvAddress.setText(billingAddress.getAddress1());

        String transactionId = getArguments().getString("transactionId");

        Double amount = getArguments().getDouble("amount");

        binding.tvAmount.setText("Total: $ "+ amount);

        binding.tvTransactionId.setText(transactionId);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
