package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Lxh
 * @date 2020/4/15 14:33
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_consignee")
public class Consignee {
    @Id
    private Integer appConsigneeId; //
    private Integer userId; //
    private String consigneeName = ""; //
    private String consigneeMobile = ""; //
    private String name = "";
    private String address = ""; //
    private Integer isDefault; //
    private String createTime = ""; //
    private Integer active;
}
