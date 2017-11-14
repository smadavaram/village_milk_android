package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Building implements Serializable{

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("buildingName")
	@Expose
	private String buildingName;
	@SerializedName("address")
	@Expose
	private String address;
	@SerializedName("vendorName")
	@Expose
	private String vendorName;
	@SerializedName("vendorPhone")
	@Expose
	private String vendorPhone;

	public Building() {

	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    @Override
	public Building clone() throws CloneNotSupportedException {
		return (Building) super.clone();
	}
}
