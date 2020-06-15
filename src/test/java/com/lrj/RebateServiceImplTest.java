package com.lrj;

import com.github.pagehelper.PageInfo;
import com.lrj.VO.OrderVo;
import com.lrj.VO.UserInfoVo;
import com.lrj.VO.UserLevelVo;
import com.lrj.dto.RequestDTO;
import com.lrj.pojo.Rebate;
import com.lrj.pojo.User;
import com.lrj.pojo.UserLevel;
import com.lrj.service.IUserService;
import com.lrj.service.RebateService;
import com.lrj.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Autowired
    private IUserService userService;

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
        User user = new User();
        user.setAppUserId(4);
        User user1 = userService.getAppUserByParam(user);
        if (user1!= null && user1.getSuperId() !=null) {
            user.setAppUserId(user1.getSuperId());
            User superUser =  userService.getAppUserByParam(user);
            if (superUser!= null) {
                UserLevelVo level = userService.findUserLevelInfo(user1.getAppUserId());
                if (level !=null) {
                }
            }

        }
        /*UserLevelVo userLevelVo = userService.findUserLevelInfo(orderVo.getUserId());
        double feeBackMoney = userLevelVo.getDistributionRatio().doubleValue()*2;
        UserInfoVo userInfoVoFather = userService.findUserByInviteCode(userInfoVo.getInvitedCode());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userInfoVoFather.getAppUserId());
        params.put("invitedId", userInfoVo.getAppUserId());
        params.put("backMoney", feeBackMoney);
        params.put("createTime", DateUtils.getNowDateTime());
        params.put("status", "1");*/
    }
}