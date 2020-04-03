package com.lrj.VO;

import java.math.BigDecimal;
import java.util.List;

public class AppShoppingVo {

    private Integer shoppingId;
    private Integer itemId;
    private Integer itemCategoryId;
    private Integer quantity;
    private String picture;
    private BigDecimal price;
    private String itemName;
    private Integer supportValue;
    private Integer supportScope;
    private BigDecimal supportMoney;
    private String itemUnit;
    private Integer isPouch;
    private Integer first;

    /** 增值服务 **/
    private List<ValueAddedServicesVo> valueAddedServicesVos;

    public AppShoppingVo() {
    }

    public AppShoppingVo(Integer shoppingId, Integer itemId, Integer itemCategoryId, Integer quantity, String picture, BigDecimal price, String itemName, Integer supportValue, Integer supportScope, BigDecimal supportMoney, String itemUnit, Integer isPouch, Integer first) {
        this.shoppingId = shoppingId;
        this.itemId = itemId;
        this.itemCategoryId = itemCategoryId;
        this.quantity = quantity;
        this.picture = picture;
        this.price = price;
        this.itemName = itemName;
        this.supportValue = supportValue;
        this.supportScope = supportScope;
        this.supportMoney = supportMoney;
        this.itemUnit = itemUnit;
        this.isPouch = isPouch;
        this.first = first;
    }

    public Integer getShoppingId() {
        return shoppingId;
    }

    public void setShoppingId(Integer shoppingId) {
        this.shoppingId = shoppingId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Integer itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public Integer getSupportValue() {
        return supportValue;
    }

    public void setSupportValue(Integer supportValue) {
        this.supportValue = supportValue;
    }

    public Integer getSupportScope() {
        return supportScope;
    }

    public void setSupportScope(Integer supportScope) {
        this.supportScope = supportScope;
    }

    public BigDecimal getSupportMoney() {
        return supportMoney;
    }

    public void setSupportMoney(BigDecimal supportMoney) {
        this.supportMoney = supportMoney;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public Integer getIsPouch() {
        return isPouch;
    }

    public void setIsPouch(Integer isPouch) {
        this.isPouch = isPouch;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    /**
     * valueAddedServicesVos
     *
     * @return the valueAddedServicesVos
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    public List<ValueAddedServicesVo> getValueAddedServicesVos() {

        return valueAddedServicesVos;
    }
    /**
     * @param valueAddedServicesVos
     *            the valueAddedServicesVos to set
     */
    public void setValueAddedServicesVos(List<ValueAddedServicesVo> valueAddedServicesVos) {

        this.valueAddedServicesVos = valueAddedServicesVos;
    }
}
