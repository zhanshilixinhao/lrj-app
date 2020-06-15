package com.lrj.mapper;

import com.lrj.VO.WxUserInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author Lxh
 * @date 2020/5/6 14:17
 */
@Repository
public interface WxUserInfoMapper extends Mapper<WxUserInfo>, MySqlMapper<WxUserInfo> {
    List<WxUserInfo> findAll();
}
