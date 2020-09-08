package com.lrj.controller;


import com.lrj.VO.ResultVo;

import com.lrj.mapper.IMigrateDataMapper;
import com.lrj.pojo.UserFromOldDataBase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe : 数据迁移或者数据库操作的控制类
 * @date : 2020-8-24 17:34:32
 */
@RestController
public class MigrateDataController {

    @Resource
    private IMigrateDataMapper migrateDataMapper;
    /**
     * 通过该接口将老项目数据库导入新项目数据库
     */
    @RequestMapping(value = "/migrateData",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo migrateData(){
        List<UserFromOldDataBase> userList = migrateDataMapper.UserFromOldDataBase();
        return new ResultVo("SUCCESS", 0, "数据导入成功！", userList);
    }
}
