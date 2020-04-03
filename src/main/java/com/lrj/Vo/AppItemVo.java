package com.lrj.VO;

import java.math.BigDecimal;

public class AppItemVo {

    private Integer itemId;
    private String picture;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private String itemName;
    private String itemUnit;
    private String commodityExplain;
    private String duration;
    private Integer inShoppingCartCount;

    public AppItemVo(){

    }
    public AppItemVo(Integer itemId, String picture, BigDecimal originalPrice, BigDecimal price, String itemName, String itemUnit, String commodityExplain, String duration, Integer inShoppingCartCount) {
        this.itemId = itemId;
        this.picture = picture;
        this.originalPrice = originalPrice;
        this.price = price;
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.commodityExplain = commodityExplain;
        this.duration = duration;
        this.inShoppingCartCount = inShoppingCartCount;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getCommodityExplain() {
        return commodityExplain;
    }

    public void setCommodityExplain(String commodityExplain) {
        this.commodityExplain = commodityExplain;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getInShoppingCartCount() {
        return inShoppingCartCount;
    }

    public void setInShoppingCartCount(Integer inShoppingCartCount) {
        this.inShoppingCartCount = inShoppingCartCount;
    }
}
