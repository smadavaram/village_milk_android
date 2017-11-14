
package com.villagemilk.model.calendar;

import java.util.ArrayList;
import java.util.List;

public class CalendarResponse {

    private String date;
    private List<Subscription> subscriptions = new ArrayList<Subscription>();

    /**
     * 
     * @return
     *     The date
     */
    public String getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * @return
     *     The subscriptions
     */
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    /**
     * 
     * @param subscriptions
     *     The subscriptions
     */
    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

}
