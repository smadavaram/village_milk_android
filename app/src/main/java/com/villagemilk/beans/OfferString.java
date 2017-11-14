
package com.villagemilk.beans;

import java.util.HashMap;
import java.util.Map;

public class OfferString {

    private String offerString;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The offerString
     */
    public String getOfferString() {
        return offerString;
    }

    /**
     * 
     * @param offerString
     *     The offerString
     */
    public void setOfferString(String offerString) {
        this.offerString = offerString;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
