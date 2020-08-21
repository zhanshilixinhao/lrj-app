package com.lrj.VO;

import com.alibaba.fastjson.JSON;
import com.lrj.pojo.ItemJSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.sf.json.JSONArray;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author : cwj
 * @describe : 定制家政服务订单
 * @date : 2020-4-17
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order_custom_houseServiceVo extends OrderVo{
    private String openTime;//服务开始时间
    private String endTime; //服务结束时间
    private Integer workTime;//工作时间段 0:周末 1：工作日 2：有周末
    private String houseArea; // 住宅面积
    private Integer serviceCycle; //服务周期
    private Integer baseServiceCount; // 每月基础服务次数（剩余）
    private BigDecimal baseServicePrice;//基础服务价格
    private String individualServiceJson; //个性服务以及次数
    private List<ItemJSON> individualServiceJSONList; //将json转化为对象给前端
    private Integer active; //月卡是否还可以用
    private String orderNumber; //订单号

}
