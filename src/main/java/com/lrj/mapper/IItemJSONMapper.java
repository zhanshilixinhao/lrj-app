package com.lrj.mapper;

import com.lrj.pojo.Balance;
import com.lrj.pojo.BalanceRecord;
import com.lrj.pojo.ItemJSON;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author cwj
 * @date 2020-8-19
 */
@Repository
public interface IItemJSONMapper extends Mapper<Balance>, MySqlMapper<Balance> {

    /**
     * 保存单项洗衣和单项家政到order_json_only表
     * @return
     * @param itemJSON
     */
   Integer addOrderJSONOnly(ItemJSON itemJSON);

    /**
     * 保存单项洗衣和单项家政到order_json_many表
     * @return
     * @param itemJSON
     */
   Integer addOrderJSONMany(ItemJSON itemJSON);

    /**
     * 通过服务单Id 查询该服务单的商品列表
     */
    List<ItemJSON> getItemJSONByReservationId(Integer reservationId);

    /**
     * 通过订单号查询  月卡或定制家政的剩余可用商品列表
     * @param orderNumber
     * @return
     */
    List<ItemJSON> getItemJSONByOrderNumber(String orderNumber);

    /**
     * 通过订单号 和 itemId 更新月卡或定制家政商品使用次数
     * @param params
     */
    void updateJSONManyByOrderNumberAndItemId(Map<String, Object> params);
}
