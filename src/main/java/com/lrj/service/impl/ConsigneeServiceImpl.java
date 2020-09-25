package com.lrj.service.impl;

import com.lrj.VO.FormerResult;
import com.lrj.mapper.ConsigneeMapper;
import com.lrj.pojo.Consignee;
import com.lrj.service.IConsigneeService;
import com.lrj.util.CommonUtil;
import com.lrj.util.DateUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lxh
 * @date 2020/4/15 15:09
 */
@Service
public class ConsigneeServiceImpl implements IConsigneeService {
    @Resource
    private ConsigneeMapper consigneeMapper;

    public FormerResult countUserAddress(Consignee consignee) {
        FormerResult formerResult = new FormerResult();
        Example example = new Example(Consignee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",consignee.getUserId()).andEqualTo("active",1);
        /*查询是否为第一个地址 **/
        int count = consigneeMapper.selectCountByExample(example);
        /*第一次添加为默认地址**/
        if (count==0) {
            consignee.setIsDefault(1);
        }else {
            consignee.setIsDefault(0);
        }
        consignee.setCreateTime(DateUtils.getNowtime()).setActive(1);
        int insert = consigneeMapper.insert(consignee);
        if (insert>0) {
            return CommonUtil.SUCCESS(formerResult,"地址添加成功!",null);
        }
        return CommonUtil.FAIL(formerResult,"地址添加失败!",null);
    }

    public FormerResult getUserAddressList(Integer userId) {
        Example example = new Example(Consignee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId).andEqualTo("active",1);
        List<Consignee> consignees = consigneeMapper.selectByExample(example);
        return CommonUtil.SUCCESS(new FormerResult(),"用户地址查询成功",consignees);
    }

    public Consignee getConsigneeByConsigneeId(Integer consigneeId) {
        return consigneeMapper.getConsigneeByConsigneeId(consigneeId);
    }

    @Override
    public void removeConsignee(Integer appConsigneeId) {
         consigneeMapper.removeConsignee(appConsigneeId);
    }

    @Override
    public void updateConsigneeDefault(Integer userId, Integer appConsigneeId) {
        Example example = new Example(Consignee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId).andEqualTo("active",1);
        List<Consignee> consigneeList = consigneeMapper.selectByExample(example);
        for (Consignee consignee : consigneeList){
            if(consignee.getAppConsigneeId().equals(appConsigneeId)){
                Map<String, Object> params = new HashMap<>();
                params.put("appConsigneeId", appConsigneeId);
                params.put("isDefault", 1);
                consigneeMapper.updateConsigneeDefault(params);
            }else {
                Map<String, Object> params = new HashMap<>();
                params.put("appConsigneeId", consignee.getAppConsigneeId());
                params.put("isDefault", 0);
                consigneeMapper.updateConsigneeDefault(params);
            }
        }
    }
}
