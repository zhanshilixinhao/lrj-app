package com.lrj.mapper;

import com.lrj.pojo.Balance;
import com.lrj.pojo.GivenBalance;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/2 10:18
 */
@Repository
public interface GivenBalanceMapper extends Mapper<GivenBalance>, MySqlMapper<GivenBalance> {
    List<GivenBalance> selectAllByUserId(Object userId);
}
