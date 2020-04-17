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

    private Integer status;  //状态
    private String serviceAddress;//服务地址

}
