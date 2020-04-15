package com.lrj.mapper;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author Lxh
 * @date 2020/4/3 9:28
 */
@Repository
public interface MemberServiceUserRelationMapper {
    String getMemberServiceEndTimeByUserId(Integer userId);

    BigDecimal getResidueLimitByUserId(Integer userId);
}
