package com.villagemilk.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 03-06-2015.
 */
public class ItemNew {
    String title;
    String body;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;
    public List<ProductMaster> productMasterList = new ArrayList<>();

    public ItemNew(String title, String body, String imageUrl) {
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
    }

    public String getTitle(){
        return this.title;
    }

    public String getBody() {
        return body;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
