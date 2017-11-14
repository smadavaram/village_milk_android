
package com.villagemilk.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarReportModel {

    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("sub_total")
    @Expose
    private Double subTotal;
    @SerializedName("charges")
    @Expose
    private Double charges;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
