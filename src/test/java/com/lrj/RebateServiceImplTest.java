package com.lrj;

import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageInfo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.StaffInfoVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.VO.UserLevelVo;
import com.lrj.constant.Constant;
import com.lrj.dto.RequestDTO;
import com.lrj.pojo.Rebate;
import com.lrj.pojo.User;
import com.lrj.pojo.UserLevel;
import com.lrj.service.IStaffService;
import com.lrj.service.IUserService;
import com.lrj.service.RebateService;
import com.lrj.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;


/**
 * @author fl
 * @descrip:
 * @date 2020/5/13 0013下午 3:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RebateServiceImplTest {

    @Autowired
    private RebateService rebateService;

    @Test
    public void getListByParam() {
        RequestDTO requestDTO = new RequestDTO();
      /*  AppFeedback appFeedback = new AppFeedback();
        appFeedback.setContact("489");
        requestDTO.setObject(appFeedback);*/
        PageInfo<Rebate> page = rebateService.getPageByParam(requestDTO);
        for (Object or : page.getList()) {
            System.out.println("数据:" + or.toString());
        }
    }

    @Test
    public void add() {
        OrderVo orderVo = new OrderVo();
        orderVo.setUserId(4);
        orderVo.setOrderType(3);
        orderVo.setTotalPrice(new BigDecimal(100));
        rebateService.rebate(orderVo);
    }
}
