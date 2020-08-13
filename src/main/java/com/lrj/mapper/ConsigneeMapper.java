package com.lrj.mapper;

import com.lrj.VO.Order_washingVo;
import com.lrj.pojo.Consignee;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/15 14:56
 */
@Repository
public interface ConsigneeMapper extends Mapper<Consignee>, MySqlMapper<Consignee> {
    void getUserAddress(Order_washingVo washingOrder);

    /**
     * 通过ID  查询地址信息
     * @param consigneeId
     * @return
     */
    Consignee getConsigneeByConsigneeId(Integer consigneeId);

    /**
     * 通过Id 删除用户地址
     * @param appConsigneeId
     * @return
     */
    void removeConsignee(Integer appConsigneeId);

    /**
     * 设置默认地址
     * @param params
     */
    void updateConsigneeDefault(Map<String,Object> params);
}
