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
public interface IStaffService {

    StaffInfoVo getStaffInfoByStaffId(Integer staffId);

    /**
     * 接单人员修改密码
     * @param staffId
     * @param newPassWord
     */
    void updateStaffPassWord(Integer staffId, String newPassWord);

    /**
     * 员工登录查询并效验
     * @param parMap
     * @return
     */
    StaffInfoVo findStaffInfoByLoginInfo(Map<String, String> parMap);

    /**
     * 根据手机号 查询员工信息
     * @param staffPhone
     * @return
     */
    StaffInfoVo findStaffInfoByPhone(String staffPhone);

    /**
     * 员工注册
     * @param params
     * @return
     */
    Integer staffRegister(Map<String, Object> params);

    /**
     * 服务单完成后 算钱
     * @param staffInfoVo
     */
    void updateStaffInfoAfterEnd(StaffInfoVo staffInfoVo);
}
