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
     *  员工注册
     */
    @Override
    public Integer staffRegister(Map<String, Object> params) {
        return staffMapper.staffRegister(params);
    }


    @Override
    public void updateStaffInfoAfterEnd(StaffInfoVo staffInfoVo) {
        staffMapper.updateStaffInfoAfterEnd(staffInfoVo);
    }

    /**
     * 添加员工支付宝账号
     * @param staffInfoVo
     */
    @Override
    public Integer updateStaffInfoAliAccount(StaffInfoVo staffInfoVo) {
        return staffMapper.updateStaffInfoAliAccount(staffInfoVo);
    }

    /**
     * 员工提现申请
     * @param staffRebateVo
     * @return
     */
    @Override
    public Integer staffWithdraw(StaffRebateVo staffRebateVo) {
        return staffMapper.staffWithdraw(staffRebateVo);
    }

    /**
     * 清空该员工的收益，记录信息等
     * @param staffId
     */
    @Override
    public void removeStaffInfoRebateInfo(Integer staffId) {
        staffMapper.removeStaffInfoRebateInfo(staffId);
    }

    /**
     *根据 手机号查询员工信息
     * @param staffPhone
     * @return
     */
    @Override
    public StaffInfoVo findStaffInfoByPhone(String staffPhone) {
        return staffMapper.findStaffInfoByPhone(staffPhone);
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
            if(staffInfoVo.getStaffPassword().equals(parMap.get("staffPassword"))){
                return staffInfoVo;
            }else {
                return null;
            }
        }
    }
}
