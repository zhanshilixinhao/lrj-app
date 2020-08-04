package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author : cwj
 * @describe : 活动实体
 * @date : 2020-7-28
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
    private Date beginDate;
    private Date endDate;
    private Integer peroidValidity;
    private String photo;
    private Integer active;
    private Date createTime;
    private Integer createUser;

}
