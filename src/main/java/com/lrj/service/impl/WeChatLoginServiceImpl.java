package com.lrj.service.impl;

import com.alibaba.fastjson.JSON;
import com.lrj.VO.WeChatUserInfo;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.User;
import com.lrj.service.IWeChatLoginService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Lxh
 * @date 2020/4/14 17:27
 */
@Service
public class WeChatLoginServiceImpl implements IWeChatLoginService {
    @Resource
    private UserMapper userMapper;
    @Override
    public void findUserByOpenId(String openId,String userInfo) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openId",openId);
        List<User> users = userMapper.selectByExample(example);
        if (users==null||users.size()==0) {
            WeChatUserInfo weChatUserInfo = JSON.parseObject(userInfo, WeChatUserInfo.class);
            User user = new User().setNickName(weChatUserInfo.getNickname()).setOpenId(weChatUserInfo.getOpenid()).setActive(1).setIsCheck(1).setHeadPhoto(weChatUserInfo.getHeadimgurl()).setCreateTime(new Date());
            userMapper.insert(user);
        }
    }
}
