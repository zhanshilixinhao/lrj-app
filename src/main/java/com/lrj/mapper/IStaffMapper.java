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

    void updateStaffPassWord(Integer staffId, String newPassWord);

    Integer updateOrderDetailStatus(Integer staffId, String orderNumber);
}
