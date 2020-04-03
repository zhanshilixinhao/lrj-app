package com.lrj.service.impl;

import com.lrj.VO.FormerResult;
import com.lrj.VO.UserLevelVo;
import com.lrj.mapper.LevelUserRelationMapper;
import com.lrj.mapper.UserLevelVoMapper;
import com.lrj.pojo.LevelUserRelation;
import com.lrj.service.IUserLevelVoService;
import com.lrj.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lxh
 * @date 2020/4/2 15:05
 */
@Service
public class UserLevelVoServiceImpl implements IUserLevelVoService {
    @Resource
    private UserLevelVoMapper userLevelVoMapper;

    @Resource
    private LevelUserRelationMapper levelUserRelationMapper;

    @Override
    public FormerResult findCurrentUserLevelInfo(Integer userId, FormerResult result) {
        UserLevelVo levelInfo = userLevelVoMapper.getCurrentUserLevelInfo(userId);
        /** 如果用户等级不存在则创建 **/
        if (levelInfo == null) {
            /** 创建初始等级 0积分 等级id为1的等级 **/
            levelUserRelationMapper.insert(new LevelUserRelation(userId,0,1));
            return CommonUtil.SUCCESS(result,"查询成功！",userLevelVoMapper.getCurrentUserLevelInfo(userId));
        }
        return CommonUtil.SUCCESS(result,"查询成功！",levelInfo);
    }
}
