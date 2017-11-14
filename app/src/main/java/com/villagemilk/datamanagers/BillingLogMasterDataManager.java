package com.villagemilk.datamanagers;

import com.villagemilk.beans.BillingLogResponseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 16-Aug-16.
 */
public class BillingLogMasterDataManager {

    private List<BillingLogResponseBean> billingLogResponseBeanList = new ArrayList<>();

    public BillingLogMasterDataManager(int count){
        for (int i = 0; i < count; i++) {
            billingLogResponseBeanList.add(buildBillingLogMaster());
        }

    }

    public BillingLogResponseBean buildBillingLogMaster(){
        BillingLogResponseBean billingLogResponseBean = new BillingLogResponseBean();
        return billingLogResponseBean;
    }
}
