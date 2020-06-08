package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/5/26 11:34
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "level")
public class Level extends Base{
    private static final long serialVersionUID = -9019255317955588624L;
    @Id
    private Integer levelId;
    private Byte digimarc;
    private String levelName;
    private String description;
    private BigDecimal discount;
    private BigDecimal distributionRatio;
    private String remark;
    private Integer createAdminId;
    private Integer updateAdminId;
    private String privilegeDescription;
}
