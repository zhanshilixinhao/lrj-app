package com.lrj.mapper;

import com.lrj.VO.UserLevelVo;

/**
 * @author Lxh
 * @date 2020/4/2 14:59
 */
public interface UserLevelVoMapper {

    UserLevelVo getCurrentUserLevelInfo(Integer userId);
}
