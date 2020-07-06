package com.lrj.mapper;

import com.lrj.pojo.Balance;
import com.lrj.pojo.BalanceRecord;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.Map;


/**
 * @author Lxh
 * @date 2020/4/2 9:12
 */
@Repository
public interface BalanceMapper extends Mapper<Balance>, MySqlMapper<Balance> {
    void updateUserBalance(Map<String,Object> params);

    BalanceRecord findBalanceByRechargeOrderNumber(String rechargeOrderNumber);
}
