package com.villagemilk.beans;

import java.io.Serializable;

public class PaymentType implements Serializable {

    private String id;

	private String name;

    private Integer type;

	public PaymentType() {

	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
