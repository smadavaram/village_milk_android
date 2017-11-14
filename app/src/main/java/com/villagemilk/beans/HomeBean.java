package com.villagemilk.beans;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 09-11-2015.
 */

public class HomeBean implements Serializable{

    @SerializedName("comingTomorrowList")
    @Expose
    private List<SubscriptionMasterSmall> comingTomorrowList = new ArrayList<>();
    @SerializedName("subscriptionsList")
    @Expose
    private List<NewSubscriptionMaster> subscriptionsList = new ArrayList<>();
    @SerializedName("billingMaster")
    @Expose
    private BillingMaster billingMaster;
    @SerializedName("promoConfig")
    @Expose
    private PromoConfig promoConfig;
    @SerializedName("welcomeMessage")
    @Expose
    private String welcomeMessage;
    @SerializedName("customerPromoMessage")
    @Expose
    private String customerPromoMessage;
    @SerializedName("popupProduct")
    @Expose
    private ProductMaster popupProduct;
    @SerializedName("popupProductHeading")
    @Expose
    private String popupProductHeading;
    @SerializedName("customerPromoMessageText")
    @Expose
    private String customerPromoMessageText;
    @SerializedName("flashProductList")
    @Expose
    private List<ProductMaster> flashProductList = new ArrayList<>();
    @SerializedName("productCategoryList")
    @Expose
    private List<ProductCategory> productCategoryList = new ArrayList<>();
    @SerializedName("userSubscriptionsList")
    @Expose
    private List<NewSubscriptionMaster> userSubscriptionsList = new ArrayList<>();
    @SerializedName("bannerList")
    @Expose
    private List<Banner> bannerList = new ArrayList<>();
    @SerializedName("installDays")
    @Expose
    private Integer installDays;
    @SerializedName("launchTimes")
    @Expose
    private Integer launchTimes;
    @SerializedName("remindInterval")
    @Expose
    private Integer remindInterval;
    @SerializedName("paytmRetailId")
    @Expose
    private String paytmRetailId;
    @SerializedName("appUpdatePromptStatus")
    @Expose
    private Integer appUpdatePromptStatus;
    @SerializedName("featuredProductBanner")
    @Expose
    private String featuredProductBanner;
    @SerializedName("pushTokenSet")
    @Expose
    private Boolean pushTokenSet;
    @SerializedName("subscriptionProductTypes")
    @Expose
    private List<Integer> subscriptionProductTypes;

    private boolean showReferalPage;

    public boolean isShowReferalPage() {
        return showReferalPage;
    }

    public void setShowReferalPage(boolean showReferralPage) {
        this.showReferalPage = showReferralPage;
    }

    public Integer getShowUserSalesOffer() {
        return showUserSalesOffer;
    }

    public void setShowUserSalesOffer(Integer showUserSalesOffer) {
        this.showUserSalesOffer = showUserSalesOffer;
    }

    private Integer showUserSalesOffer;

    public HomeBean() {

    }

    public List<SubscriptionMasterSmall> getComingTomorrowList() {
        return comingTomorrowList;
    }

    public void setComingTomorrowList(List<SubscriptionMasterSmall> comingTomorrowList) {
        this.comingTomorrowList = comingTomorrowList;
    }

    public List<NewSubscriptionMaster> getSubscriptionsList() {
        return subscriptionsList;
    }

    public void setSubscriptionsList(List<NewSubscriptionMaster> subscriptionsList) {
        this.subscriptionsList = subscriptionsList;
    }

    public BillingMaster getBillingMaster() {
        return billingMaster;
    }

    public void setBillingMaster(BillingMaster billingMaster) {
        this.billingMaster = billingMaster;
    }

    public PromoConfig getPromoConfig() {
        return promoConfig;
    }

    public void setPromoConfig(PromoConfig promoConfig) {
        this.promoConfig = promoConfig;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getCustomerPromoMessage() {
        return customerPromoMessage;
    }

    public void setCustomerPromoMessage(String customerPromoMessage) {
        this.customerPromoMessage = customerPromoMessage;
    }

    public ProductMaster getPopupProduct() {
        return popupProduct;
    }

    public void setPopupProduct(ProductMaster popupProduct) {
        this.popupProduct = popupProduct;
    }

    public String getPopupProductHeading() {
        return popupProductHeading;
    }

    public void setPopupProductHeading(String popupProductHeading) {
        this.popupProductHeading = popupProductHeading;
    }

    public String getCustomerPromoMessageText() {
        return customerPromoMessageText;
    }

    public void setCustomerPromoMessageText(String customerPromoMessageText) {
        this.customerPromoMessageText = customerPromoMessageText;
    }

    public List<ProductMaster> getFlashProductList() {
        return flashProductList;
    }

    public void setFlashProductList(List<ProductMaster> flashProductList) {
        this.flashProductList = flashProductList;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public List<NewSubscriptionMaster> getUserSubscriptionsList() {
        return userSubscriptionsList;
    }

    public void setUserSubscriptionsList(List<NewSubscriptionMaster> userSubscriptionsList) {
        this.userSubscriptionsList = userSubscriptionsList;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    public Integer getInstallDays() {
        return installDays;
    }

    public void setInstallDays(Integer installDays) {
        this.installDays = installDays;
    }

    public Integer getLaunchTimes() {
        return launchTimes;
    }

    public void setLaunchTimes(Integer launchTimes) {
        this.launchTimes = launchTimes;
    }

    public Integer getRemindInterval() {
        return remindInterval;
    }

    public void setRemindInterval(Integer remindInterval) {
        this.remindInterval = remindInterval;
    }

    public String getPaytmRetailId() {
        return paytmRetailId;
    }

    public void setPaytmRetailId(String paytmRetailId) {
        this.paytmRetailId = paytmRetailId;
    }

    public Integer getAppUpdatePromptStatus() {
        return appUpdatePromptStatus;
    }

    public void setAppUpdatePromptStatus(Integer appUpdatePromptStatus) {
        this.appUpdatePromptStatus = appUpdatePromptStatus;
    }

    public String getFeaturedProductBanner() {
        return featuredProductBanner;
    }

    public void setFeaturedProductBanner(String featuredProductBanner) {
        this.featuredProductBanner = featuredProductBanner;
    }

    public Boolean getPushTokenSet() {
        return pushTokenSet;
    }

    public void setPushTokenSet(Boolean pushTokenSet) {
        this.pushTokenSet = pushTokenSet;
    }

    public List<Integer> getSubscriptionProductTypes() {
        return subscriptionProductTypes;
    }

    public void setSubscriptionProductTypes(List<Integer> subscriptionProductTypes) {
        this.subscriptionProductTypes = subscriptionProductTypes;
    }
}
