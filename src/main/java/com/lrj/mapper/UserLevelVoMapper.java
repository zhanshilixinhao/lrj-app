package com.lrj.mapper;

import com.lrj.VO.UserLevelVo;
import org.springframework.stereotype.Repository;

/**
 * @author Lxh
 * @date 2020/4/2 14:59
 */
@Repository
public interface UserLevelVoMapper {

    UserLevelVo getCurrentUserLevelInfo(Integer userId);
}
