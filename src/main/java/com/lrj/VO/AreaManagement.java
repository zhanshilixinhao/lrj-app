package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/5/27 15:15
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "area_management")
public class AreaManagement {
    @Id
    private Integer areaManagementId;
    private Integer itemId;
    private String type;
    private BigDecimal price;
}
