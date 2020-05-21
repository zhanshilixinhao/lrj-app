package com.lrj.service.impl;

import com.lrj.VO.*;
import com.lrj.mapper.IStaffMapper;
import com.lrj.service.IStaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 懒人家助手 员工服务层
 * @date : 2020-5-8
 */
@Service
@Transactional
public class IStaffServiceImpl implements IStaffService {

    @Resource
    private IStaffMapper staffMapper;

    public StaffInfoVo getStaffInfoByStaffId(Integer staffId) {
        return staffMapper.getStaffInfoByStaffId(staffId);
    }

    public void updateStaffPassWord(Integer staffId, String newPassWord) {
        //封装不同类型参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staffId", staffId);
        params.put("newPassWord", newPassWord);
        staffMapper.updateStaffPassWord(params);
    }

    /**
     * 员工登录查询并效验
     * @param parMap
     * @return
     */
    public StaffInfoVo findStaffInfoByLoginInfo(Map<String, String> parMap) {
        StaffInfoVo staffInfoVo = staffMapper.findStaffInfoByLoginInfo(parMap.get("loginName"));
        if(staffInfoVo == null){
            return null;
        }else {
            //效验密码
            if(staffInfoVo.getAdminPassword().equals(parMap.get("staffPassword"))){
                return staffInfoVo;
            }else {
                return null;
            }
        }
    }
}
