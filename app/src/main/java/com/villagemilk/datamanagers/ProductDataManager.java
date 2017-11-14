package com.villagemilk.datamanagers;

import com.villagemilk.beans.ProductMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 28-Jul-16.
 */
public class ProductDataManager {

    private List<ProductMaster> productMasterList = new ArrayList<>();

    public ProductDataManager(int size){
        for (int i = 0; i < size; i++) {
            productMasterList.add(buildProduct(String.valueOf(i)));
        }
    }

    public ProductMaster buildProduct(String id){
        ProductMaster productMaster = new ProductMaster();
        productMaster.setId(id);
        productMaster.setProductName("Nandini Milk");
        productMaster.setProductUnitPrice(100.0);
        productMaster.setProductImage("http://i.imgur.com/XbwYvAe.jpg");
        productMaster.setProductUnitSize("500ml");

        return productMaster;
    }

    public List<ProductMaster> getProductMasterList() {
        return productMasterList;
    }
}
