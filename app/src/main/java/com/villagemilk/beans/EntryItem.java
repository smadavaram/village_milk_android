package com.villagemilk.beans;

public class EntryItem implements Item {

    public final String title;
    public final Double price;
    public final Double strikePrice;
    public final String unit;
    public final String productId;
    public final String specialText;
    public final int productType;
    public final int status;
    public final String imageUrl;
    public final ProductMaster productMasterO;
    public int quantity;

    public EntryItem(String title, Double price, Double strikePrice, String unit, String imageUrl, String productId, ProductMaster productMaster, String specialText) {
        this.title = title;
        this.price = price;
        this.strikePrice = strikePrice;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.productId = productId;
        this.specialText = specialText;

        if(productMaster!=null) {
            this.productType = productMaster.getProductType()==null?0:productMaster.getProductType().getType();
            this.status = productMaster.getStatus();
            this.productMasterO = productMaster;
        } else {
            this.status = 1;
            this.productType=0;
            this.productMasterO = new ProductMaster();
            this.productMasterO.setSpecialText(specialText);
            this.productMasterO.setProductUnitPrice(price);
            this.productMasterO.setStrikePrice(strikePrice);
            this.productMasterO.setProductName(title);
            this.productMasterO.setProductUnitSize(unit);
//            this.productMasterO.setpr
        }

    }

    @Override
    public boolean isSection() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof EntryItem)){
            return false;
        } else {
            return ((EntryItem) o).productId.equals(productId);
        }
    }

    @Override
    public int hashCode() {
        return productMasterO!=null?productMasterO.getId().hashCode():super.hashCode();
    }
}
