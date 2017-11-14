package com.villagemilk.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.villagemilk.R;
import com.villagemilk.activities.ActivityProductList;
import com.villagemilk.activities.HomeActivity;
import com.villagemilk.adapters.AdapterProductList;
import com.villagemilk.beans.HomeBean;
import com.villagemilk.beans.ProductCategory;

import java.util.List;

/**
 * Created by android on 3/1/17.
 */

public class FragmentHomeCategories extends Fragment {

    Context mContext;

    TabLayout tlCategories;

    RecyclerView rvProducts;
    RecyclerView rvCategories;

    HomeBean homeBean;

    AdapterProductList adapter;

    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_categories,container,false);

        tlCategories = (TabLayout)view.findViewById(R.id.tlCategories);
        rvProducts = (RecyclerView)view.findViewById(R.id.rvProducts);
        rvCategories = (RecyclerView)view.findViewById(R.id.rvCategories);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ActivityProductList.class);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        homeBean = (HomeBean) getArguments().getSerializable("homeBean");


/*
        for(final ProductCategory productCategory : homeBean.getProductCategoryList()){

            TabLayout.Tab tab = tlCategories.newTab();

            View v = LayoutInflater.from(mContext).inflate(R.layout.category_tab,null);
            ImageView imageView = (ImageView) v.findViewById(R.id.ivCategoryImage);
            TextView textView = (TextView) v.findViewById(R.id.tvProductCategory);


            imageLoader.displayImage(productCategory.getProductCategoryImage(),imageView);

            textView.setText(productCategory.getProductCategoryName());

//            tab.setText(productCategory.getProductCategoryName());
            tab.setCustomView(v);

            tlCategories.addTab(tab);


        }

        if(tlCategories.getTabCount() <=1)
            tlCategories.setVisibility(View.GONE);



        tlCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                adapter.setProductList(homeBean.getProductCategoryList().get(tab.getPosition()).getProductList());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
*/


    }



    public void setHomeBean(HomeBean homeBean){

        this.homeBean = homeBean;

        AdapterCategories adapterCategories = new AdapterCategories(mContext,homeBean.getProductCategoryList());
        rvCategories.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        rvCategories.setAdapter(adapterCategories);

        adapter = new AdapterProductList(mContext,homeBean.getProductCategoryList().get(0).getProductList());

        rvProducts.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        rvProducts.setAdapter(adapter);



    }



    public class AdapterCategories extends RecyclerView.Adapter<FragmentHomeCategories.ViewModel>{

        Context context;
        List<ProductCategory> productCategories;

        public AdapterCategories(Context context, List<ProductCategory> productCategories) {
            this.context = context;
            this.productCategories = productCategories;
        }


        @Override
        public FragmentHomeCategories.ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.category_tab,parent,false);

            return new ViewModel(view);
        }

        @Override
        public void onBindViewHolder(FragmentHomeCategories.ViewModel holder, int position) {

            ProductCategory productCategory = productCategories.get(position);

            imageLoader.displayImage(productCategory.getProductCategoryImage(),holder.imageView);

            holder.textView.setText(productCategory.getProductCategoryName());


        }

        @Override
        public int getItemCount() {
            return productCategories.size();
        }
    }


    class ViewModel extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView textView;

        ViewModel(View v) {
            super(v);

            imageView = (ImageView) v.findViewById(R.id.ivCategoryImage);
            textView = (TextView) v.findViewById(R.id.tvProductCategory);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ActivityProductList.class);
                    intent.putExtra("id",homeBean.getProductCategoryList().get(getAdapterPosition()).getProductCategoryId());
                    startActivity(intent);

                }
            });

        }
    }
}
