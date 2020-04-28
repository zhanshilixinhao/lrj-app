package com.lrj.VO;

import com.mysql.cj.xdevapi.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.sf.json.JSONArray;

/**
 * @author : cwj
 * @describe : 单项洗衣
 * @date : 2020-4-17
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order_washingVo extends OrderVo{
        private Integer takeConsigneeId; // 取件地址
    private Integer sendConsigneeId; // 送件地址
    private Integer isLock; //  订单是否已抢
    private String takeTime = ""; // 取件时间
    private Integer isUrgent; //是否加急
    private String shoppingJSON; //购物车信息
}
