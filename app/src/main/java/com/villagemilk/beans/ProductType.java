package com.villagemilk.beans;

import java.io.Serializable;

public class ProductType implements Serializable {

    private String id;

	private String name;

    private Integer type;

	private String productTypeImage;

	private String bannerImageString;

	public ProductType() {

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

    public String getProductTypeImage() {
        return productTypeImage;
    }

    public void setProductTypeImage(String productTypeImage) {
        this.productTypeImage = productTypeImage;
    }

    public String getBannerImageString() {
        return bannerImageString;
    }

    public void setBannerImageString(String bannerImageString) {
        this.bannerImageString = bannerImageString;
    }
}
