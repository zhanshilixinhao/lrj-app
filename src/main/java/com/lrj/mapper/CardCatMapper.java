package com.lrj.mapper;

import com.lrj.VO.UserMonthCardVo;
import com.lrj.pojo.CardCat;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * @author Lxh
 * @date 2020/4/16 11:21
 */
public interface CardCatMapper extends Mapper<CardCat>,MySqlMapper<CardCat> {
}
