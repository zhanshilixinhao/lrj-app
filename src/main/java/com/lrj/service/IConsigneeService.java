package com.lrj.service;

import com.lrj.VO.FormerResult;
import com.lrj.pojo.Consignee;

/**
 * @author Lxh
 * @date 2020/4/15 15:09
 */
public interface IConsigneeService {
    FormerResult countUserAddress(Consignee consignee);

    FormerResult getUserAddressList(Integer userId);
}
