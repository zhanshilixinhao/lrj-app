package com.lrj.mapper;

import com.lrj.pojo.Activity;
import com.lrj.pojo.ActivityJSON;
import com.lrj.pojo.ItemJSON;
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

    Activity getActivityById(Integer merchantActivityId);

    /**
     * 查询活动包含的 商品列表
     * @return
     * @param activityId
     */
    List<ActivityJSON> getActivityItemList(Integer activityId);
}
