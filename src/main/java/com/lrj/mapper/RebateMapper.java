package com.lrj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lrj.pojo.Rebate;
import org.springframework.stereotype.Repository;

/**
 * @author : fl
 * @describe :返利的映射接口
 * @date : 2020-4-29
 */
@Repository
public interface RebateMapper extends BaseMapper<Rebate> {

    /**
     * 返利记录
     * @param rebate
     */
    void add(Rebate rebate);
}
