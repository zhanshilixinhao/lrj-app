package com.lrj.service;

import com.lrj.pojo.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : 活动控制类
 * @date : 2020-7-28
 */
public interface IActivityService {

    /**
     * 查询用户可参数的活动
     */
     List<Activity> getActivityList();
}
