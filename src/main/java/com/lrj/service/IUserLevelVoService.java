package com.lrj.service;

import com.lrj.VO.FormerResult;
import com.lrj.VO.UserLevelVo;

/**
 * @author Lxh
 * @date 2020/4/2 15:04
 */
public interface IUserLevelVoService {

    FormerResult findCurrentUserLevelInfo(Integer userId, FormerResult result);
}
