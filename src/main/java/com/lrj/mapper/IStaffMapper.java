package com.lrj.mapper;

import com.lrj.VO.*;
import com.lrj.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

/**
 * @author cwj
 * @date 2020-5-8
 */
@Repository
public interface IStaffMapper extends Mapper<User>, MySqlMapper<User> {

    StaffInfoVo getStaffInfoByStaffId(Integer staffId);

    void updateStaffPassWord(Map<String,Object> params);

    /**
     * 通过员工登录名查询 员工信息
     * @param loginName
     * @return
     */
    StaffInfoVo findStaffInfoByLoginInfo(String loginName);

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
     * 服务单完成后  算钱
     * @param staffInfoVo
     */
    void updateStaffInfoAfterEnd(StaffInfoVo staffInfoVo);
}
