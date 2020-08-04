package com.lrj.service.impl;

import com.lrj.VO.AppApkVersionVo;
import com.lrj.mapper.IActivityMapper;
import com.lrj.mapper.IAppApkVersionMapper;
import com.lrj.pojo.Activity;
import com.lrj.service.IActivityService;
import com.lrj.service.IAppApkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe : 活动实现类
 * @date : 2020-7-28
 */
@Service
@Transactional
public class IActivityServiceImpl implements IActivityService {

    @Resource
    private IActivityMapper activityMapper;

    /**
     * 查询用户可参数的活动
     */
    @Override
    public List<Activity> getActivityList() {
        return activityMapper.getActivityList();
    }
}
