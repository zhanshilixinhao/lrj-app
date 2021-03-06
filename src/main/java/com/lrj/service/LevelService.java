package com.lrj.service;

import com.lrj.VO.FormerResult;
import com.lrj.pojo.Level;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/5/26 11:56
 */
public interface LevelService {

    /**
     * @Description: 获取全部等级信息
     * @Author: LxH
     * @Date: 2020/5/26 12:06
     */
    FormerResult findAllLevel();

    /**
     * @Description: 获取用户等级信息
     * @Author: LxH
     * @Date: 2020/5/26 12:11
     */
    FormerResult findLevelByUserId(Integer userId);

    /**
     * 根据id查询登记信息
     * @return
     */
    Level findLevelByLeaveId(Integer levelId);
}
