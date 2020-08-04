package com.lrj.mapper;

import com.lrj.pojo.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : 活动接口层
 * @date : 2020-7-28
 */
@Repository
public interface IActivityMapper {

    List<Activity> getActivityList();
}
