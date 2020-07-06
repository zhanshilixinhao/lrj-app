package com.lrj;



import com.lrj.VO.UserMonthCardVo;
import com.lrj.VO.WxUserInfo;
import com.lrj.mapper.*;

import com.lrj.pojo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

import static com.lrj.pojo.Order.ORDER_NUMBER;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LrjAppApplicationTests {

    @Resource
    private ConsigneeMapper consigneeMapper;
    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private TeamLaundryMapper teamLaundryMapper;
    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private UserCouponMapper userCouponMapper;
    @Resource
    private WxUserInfoMapper wxUserInfoMapper;
    @Resource
    private MemberServiceUserRelationMapper memberServiceUserRelationMapper;
    @Resource
    private UserLevelMapper userLevelMapper;
    @Resource
    private IOrderMapper orderMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PayOperationMapper payOperationMapper;
    @Test
    public void contextLoads() {
        Order reorder = new Order();
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo(ORDER_NUMBER,"20200618389571");
        List<Order> orders = orderMapper.selectByExample(example);
        for (Order order : orders) {
            reorder=order;
        }
        System.out.println(reorder.toString());
        /*WxUserInfo wxUserInfo = new WxUserInfo();
        wxUserInfo.setCity("螺蛳湾").setSex(2);
        int i = wxUserInfoMapper.insertSelective(wxUserInfo);
        System.out.println(i);*/
        /*UserMonthCardVo userMonthCardVo = userMonthCardMapper.selectUserMonthCard(36891);
        System.out.println(userMonthCardVo.getIsAuto());*/
       /* Order order = cardOrderMapper.selectByPrimaryKey(14);
        Example example = new Example(Consignee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appConsigneeId",order.getTakeAddress());
        List<Consignee> consignees = consigneeMapper.selectByExample(example);
        for (Consignee consignee : consignees) {
            System.out.println(consignee.getName());
        }*/
       /* Order order = new Order();
        long l = RandomUtil.generateOrderId();
        String s = Long.toString(l);
        order.setOrderType(2).setOrderNumber(s).setCreateTime(com.lrj.util.DateUtils.formatDate(new Date()));
        cardOrderMapper.insertSelective(order);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNumber",s);
        List<Order> orders = cardOrderMapper.selectByExample(example);
        for (Order order1 : orders) {
            String orderNumber = order1.getOrderNumber();
            System.out.println(orderNumber);
        }*/


        /*Order order = new Order();
        long l = RandomUtil.generateOrderId();
        String s = Long.toString(l);
        order.setOrderType(2).setOrderNumber(s).setCreateTime(com.lrj.util.DateUtils.formatDate(new Date()));
        cardOrderMapper.insert(order);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNumber", s);
        List<Order> orders = cardOrderMapper.selectByExample(example);
        Long id = null;
        String orderNumber = null;
        for (Order order1 : orders) {
            id = order1.getId();
            orderNumber = order1.getOrderNumber();
        }
        System.out.println(id + "______" + orderNumber);*/

    }
}





