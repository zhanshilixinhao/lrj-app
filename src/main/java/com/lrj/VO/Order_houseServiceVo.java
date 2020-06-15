package com.lrj.VO;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : cwj
 * @describe : 家政服务订单
 * @date : 2020-4-16
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order_houseServiceVo  extends OrderVo{
    private Integer id; //主键
    private String orderNumber;//  订单号
    private Integer active;  //状态
    private Integer isLock; //是否被抢
    private Integer takeConsigneeId;//服务地址
    private String houseServiceJson; //家政服务Id
    private String createTime; //创建时间
    private HouseServiceVo houseServiceVo;  //家政服务

}
