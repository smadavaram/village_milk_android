package com.villagemilk.beans;

/**
 * Created by akash.mercer on 23-02-2016.
 */
public class CartItem {

    private String subscriptionId;

    private String productMasterId;

    private int quantity;

    public CartItem(){

    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getProductMasterId() {
        return productMasterId;
    }

    public void setProductMasterId(String productMasterId) {
        this.productMasterId = productMasterId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
