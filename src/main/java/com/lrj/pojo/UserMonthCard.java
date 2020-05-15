package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Lxh
 * @date 2020/4/16 15:08
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserMonthCard{
    private static final long serialVersionUID = -1667954499663620799L;
    @Id
    private Integer id;
    private Integer usedCount;
    private Integer totalCount;
    private Integer mUsedCount;
    private Integer mTotalCount;
    private Integer userId;
    private Integer wxMonthCardId;
    private Integer isAuto;
    private Byte status;
    private Date startTime;
    private Date endTime;
    private Date lastAppointTime;
    private String optionTime;
    private Integer addressId;

}
