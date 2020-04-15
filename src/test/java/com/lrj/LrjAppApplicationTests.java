package com.lrj;

import com.alibaba.fastjson.JSON;
import com.lrj.mapper.ConsigneeMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.pojo.Consignee;
import com.lrj.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LrjAppApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ConsigneeMapper consigneeMapper;
    @Test
    public void contextLoads() {
        Example example = new Example(Consignee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",40).andEqualTo("active",1);
        List<Consignee> consignees = consigneeMapper.selectByExample(example);
        for (Consignee consignee : consignees) {
            Integer appConsigneeId = consignee.getAppConsigneeId();
            System.out.println(appConsigneeId);
        }


    }

}
