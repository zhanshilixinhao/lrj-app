package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @author cwj
 * @descrip: 活动
 * @date 2020-9-2 18:05:36
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @Id
    private Integer activityId;
    private String name;
    private String content;
    private String beginDate;
    private String endDate;

    private String photo;
    private String outContentLink;

    /**
     * 活动状态 0 停用 1 启用
     */
    private Integer active;

    private String createUser;
    private String createTime;

    private BigDecimal price;
    private Integer appointCount;
    private Integer buyCount;
    private Integer sendCount;

    public static final String NAME_COLUMN = "name";
    public static final String DELETED_COLUMN ="deleted";
}
