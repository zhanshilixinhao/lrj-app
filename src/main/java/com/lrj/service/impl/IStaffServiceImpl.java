package com.lrj.service.impl;

import com.lrj.VO.*;
import com.lrj.mapper.IStaffMapper;
import com.lrj.service.IStaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
        staffMapper.updateStaffPassWord(staffId,newPassWord);
    }
}
