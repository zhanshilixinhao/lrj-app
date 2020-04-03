package com.lrj.mapper;

import com.lrj.VO.ConsigneeVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : 用户 有关接口
 * @date : 2020-4-2
 */
@Repository
public interface IUserMapper {
    /**
     * 通过用户ID查询用户拥有地址
     * @return
     */
    List<ConsigneeVo> findUserAddressByUserId(Integer userId);
}
