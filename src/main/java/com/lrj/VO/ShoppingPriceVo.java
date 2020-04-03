package com.lrj.VO;

import java.math.BigDecimal;

public class ShoppingPriceVo {

    private BigDecimal totalPrice;
    private Integer quantity;

    public ShoppingPriceVo(BigDecimal totalPrice, Integer quantity) {

        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }

    public ShoppingPriceVo() {

    }

    /**
     * totalPrice
     * 
     * @return the totalPrice
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    public BigDecimal getTotalPrice() {

        return totalPrice;
    }

    /**
     * @param totalPrice
     *            the totalPrice to set
     */
    public void setTotalPrice(BigDecimal totalPrice) {

        this.totalPrice = totalPrice;
    }

    /**
     * quantity
     * 
     * @return the quantity
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    public Integer getQuantity() {

        return quantity;
    }

    /**
     * @param quantity
     *            the quantity to set
     */
    public void setQuantity(Integer quantity) {

        this.quantity = quantity;
    }
}
