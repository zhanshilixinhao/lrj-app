package com.lrj.service;

import com.lrj.pojo.TeamLaundry;

/**
 * @author Lxh
 * @date 2020/4/28 18:29
 */
public interface TeamLaundryService {
    TeamLaundry findTeamLundryByUserId(Integer userId);
}
