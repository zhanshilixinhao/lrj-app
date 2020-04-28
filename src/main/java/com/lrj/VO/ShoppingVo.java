package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingVo {

    private Integer shoppingId;
    private Integer userId;
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
