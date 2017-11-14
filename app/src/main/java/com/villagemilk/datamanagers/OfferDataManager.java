package com.villagemilk.datamanagers;

import com.villagemilk.beans.Offer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 30-Jul-16.
 */
public class OfferDataManager {

    private List<Offer> offerList = new ArrayList<>();

    public OfferDataManager(int size){
        for (int i = 0; i < size; i++) {
            offerList.add(buildOffer(String.valueOf(i)));
        }
    }

    public Offer buildOffer(String id){
        Offer offer = new Offer();
        offer.setId(id);
        offer.setOfferImageUrl("http://i.imgur.com/XbwYvAe.jpg");
        offer.setOfferTitle("Cashback of ₹100");
        offer.setOfferDescription("Daily Ninja. Manage your daily needs all in one place.No hassle. No worries. No more rushing for curd early morning. No more carrying change for bread");

        return offer;
    }

    public List<Offer> getOfferList() {
        return offerList;
    }

}
