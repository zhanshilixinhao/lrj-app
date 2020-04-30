package com.lrj.service.impl;

import com.lrj.mapper.TeamLaundryMapper;
import com.lrj.pojo.TeamLaundry;
import com.lrj.service.TeamLaundryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lxh
 * @date 2020/4/28 18:29
 */
@Service
public class TeamLaundryServiceImpl implements TeamLaundryService {
    @Resource
    private TeamLaundryMapper teamLaundryMapper;
    @Override
    public TeamLaundry findTeamLundryByUserId(Integer userId) {
        return teamLaundryMapper.selectByPrimaryKey(userId);
    }
}
