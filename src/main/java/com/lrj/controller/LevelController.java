package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.ReturnResult;
import com.lrj.service.LevelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/5/26 11:51
 */
@RestController
public class LevelController {

    @Resource
    private LevelService levelService;

    /**
     * @Description: 获取全部等级信息
     * @Author: LxH
     * @Date: 2020/5/26 12:05
     */
    @RequestMapping("findAllLevel")
    public FormerResult findAllLevel(){
        return levelService.findAllLevel();
    }

    /**
     * @Description: 获取用户等级信息
     * @Author: LxH
     * @Date: 2020/5/26 12:10
     */
    @RequestMapping("findLevelByUserId")
    public FormerResult findLevelByUserId(Integer userId){
        return levelService.findLevelByUserId(userId);
    }
}
