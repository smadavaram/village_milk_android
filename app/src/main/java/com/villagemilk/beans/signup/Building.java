
package com.villagemilk.beans.signup;

public class Building {

    private String id;
    private String buildingName;
    private String address;
    private String vendorName;
    private String vendorPhone;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * 
     * @param buildingName
     *     The buildingName
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return
     *     The vendorName
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * 
     * @param vendorName
     *     The vendorName
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * 
     * @return
     *     The vendorPhone
     */
    public String getVendorPhone() {
        return vendorPhone;
    }

    /**
     * 
     * @param vendorPhone
     *     The vendorPhone
     */
    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

}
