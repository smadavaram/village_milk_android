package com.villagemilk.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.villagemilk.activities.ProductCategoryListActivity;

/**
 * Created by android on 29/12/16.
 */

public class SubscriptionListViewModel {

    Context mContext;

    public SubscriptionListViewModel(Context mContext) {
        this.mContext = mContext;
    }

    public View.OnClickListener getAddNewSubscriptions(){

        return new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ProductCategoryListActivity.class);
                mContext.startActivity(intent);

            }
        };



    }



}
