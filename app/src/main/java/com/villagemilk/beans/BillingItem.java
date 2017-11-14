package com.villagemilk.beans;

public class BillingItem {
    public final String paid_date;
    public final String rupees;
    public final  String start_end_date;
    public final int imageid;
    public Double amount;
    public boolean isCurrent;

    public BillingItem(String paid_date, String rupees, String start_end_date, int imageid, Double amount, boolean isCurrent) {
        this.paid_date = paid_date;
        this.rupees=rupees;
        this.start_end_date=start_end_date;
        this.imageid=imageid;

        if(amount != null){
            this.amount = amount;
        }
        this.isCurrent = isCurrent;
    }
}
