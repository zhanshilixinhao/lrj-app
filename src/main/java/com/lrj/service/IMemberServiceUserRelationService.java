package com.lrj.service;


import java.math.BigDecimal;

/**
 * @author Lxh
 * @date 2020/4/3 9:29
 */
public interface IMemberServiceUserRelationService {

    boolean judgeTheUserIsMemberService(Integer userId);

    BigDecimal findResidueLimitByUserId(Integer userId);
}
