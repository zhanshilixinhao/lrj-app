package com.lrj.service.impl;

import com.lrj.VO.FormerResult;
import com.lrj.mapper.BalanceMapper;
import com.lrj.mapper.IOrderMapper;
import com.lrj.pojo.Balance;
import com.lrj.pojo.Order;
import com.lrj.service.AppletsPayService;
import com.lrj.util.CommonUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.lrj.pojo.Order.ORDER_NUMBER;

/**
 * @Description:
 * @Author: Lxh
 * @Date: 2020/6/23 16:03
 */
@Service
public class AppletsPayServiceImpl implements AppletsPayService {

    @Resource
    private IOrderMapper orderMapper;

    @Resource
    private BalanceMapper balanceMapper;

    private FormerResult result;
    /**
     * @param: request
     * @Description: 微信第三方支付
     * @Author: LxH
     * @Date: 2020/6/23 16:05
     */
    @Override
    public FormerResult pay(HttpServletRequest request) {
        /**接收前端传回参数*/
        String orderNumber = request.getParameter("orderNumber");
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Integer isBalance = Integer.parseInt(request.getParameter("isBalance"));
        /**校验必须参数**/
        if (orderNumber == null || userId == 0 || isBalance == null) {
            return new FormerResult("success", 1, "参数有误,请检查参数", null);
        }
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo(ORDER_NUMBER,orderNumber);
        List<Order> orders = orderMapper.selectByExample(example);
        if (orders.size()==0) {
            return CommonUtil.FAIL(result,null,null);
        }
        for (Order order : orders) {
            Balance balance = balanceMapper.selectByPrimaryKey(order.getUserId());

        }
        return null;
    }
}
