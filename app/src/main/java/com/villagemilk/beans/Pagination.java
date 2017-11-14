package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akash.mercer on 21-Aug-16.
 */

public class Pagination {

    @SerializedName("next")
    @Expose
    private String next;

    public Pagination(){

    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
