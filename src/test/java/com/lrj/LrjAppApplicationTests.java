package com.lrj;



import com.lrj.VO.UserMonthCardVo;
import com.lrj.mapper.*;

import com.lrj.pojo.Consignee;
import com.lrj.pojo.Order;
import com.lrj.pojo.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;


import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LrjAppApplicationTests {


    @Resource
    private CardCatMapper cardCatMapper;
    @Resource
    private UserMonthCardMapper userMonthCardMapper;
    @Resource
    private CardOrderMapper cardOrderMapper;
    @Resource
    private UserMonthCardOrderMapper userMonthCardOrderMapper;
    @Resource
    private ConsigneeMapper consigneeMapper;
    @Resource
    private ReservationMapper reservationMapper;
    @Test
    public void contextLoads() {
        UserMonthCardVo userMonthCardVo = userMonthCardMapper.selectUserMonthCard(36891);
        System.out.println(userMonthCardVo.getIsAuto());
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





