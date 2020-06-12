package com.lrj.service.impl;

import com.lrj.VO.FormerResult;
import com.lrj.mapper.LevelMapper;
import com.lrj.mapper.UserLevelMapper;
import com.lrj.pojo.UserLevel;
import com.lrj.service.LevelService;
import com.lrj.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/5/26 11:57
 */
@Service
public class LevelServiceImpl implements LevelService {

    @Resource
    private LevelMapper levelMapper;

    @Resource
    private UserLevelMapper userLevelMapper;

    /**
     * @Description: 获取全部等级信息
     * @Author: LxH
     * @Date: 2020/5/26 12:06
     */
    @Override
    public FormerResult findAllLevel() {
        return CommonUtil.SUCCESS(new FormerResult(),null,levelMapper.selectAll());
    }

    /**
     * @param: userId
     * @Description: 获取用户等级信息
     * @Author: LxH
     * @Date: 2020/5/26 12:11
     */
    @Override
    public FormerResult findLevelByUserId(Integer userId) {
       /* UserLevel userLevel = userLevelMapper.selectByPrimaryKey(userId);
        Integer inviteNum = userLevel.getInviteNum();
        Integer levelId = userLevel.getLevelId();
        if (levelId==1) {

        }
*/
        return null;
    }
}
