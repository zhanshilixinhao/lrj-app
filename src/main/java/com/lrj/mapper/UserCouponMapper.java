package com.lrj.mapper;

import org.springframework.stereotype.Repository;

/**
 * @author Lxh
 * @date 2020/4/2 12:40
 */
@Repository
public interface UserCouponMapper {
    Integer selectQuantityByUserId(Object userId);
}
