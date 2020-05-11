package com.lrj.service;

import com.lrj.VO.*;
import com.lrj.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/7 14:30
 */
@Repository
public interface IStaffService {

    StaffInfoVo getStaffInfoByStaffId(Integer staffId);

    /**
     * 接单人员修改密码
     * @param staffId
     * @param newPassWord
     */
    void updateStaffPassWord(Integer staffId, String newPassWord);

}