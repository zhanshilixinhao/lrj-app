package com.lrj.mapper;

import com.lrj.VO.Order_washingVo;
import com.lrj.pojo.Consignee;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Lxh
 * @date 2020/4/15 14:56
 */
@Repository
public interface ConsigneeMapper extends Mapper<Consignee>, MySqlMapper<Consignee> {
    void getUserAddress(Order_washingVo washingOrder);
}
