
package com.villagemilk.beans;

import java.util.HashMap;
import java.util.Map;


public class UserExpenditure {

    private Double expense;
    private String categoryName;

    public float getFraction() {
        return fraction;
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
    }

    private float fraction;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The expense
     */
    public Double getExpense() {
        return expense;
    }

    /**
     * 
     * @param expense
     *     The expense
     */
    public void setExpense(Double expense) {
        this.expense = expense;
    }

    /**
     * 
     * @return
     *     The categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 
     * @param categoryName
     *     The categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
