package com.lrj.service.impl;

import com.lrj.mapper.MemberServiceUserRelationMapper;
import com.lrj.service.IMemberServiceUserRelationService;
import com.lrj.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lxh
 * @date 2020/4/3 9:30
 */
@Service
public class MemberServiceUserRelationServiceImpl implements IMemberServiceUserRelationService {
    @Resource
    private MemberServiceUserRelationMapper memberServiceUserRelationMapper;

    @Override
    public boolean judgeTheUserIsMemberService(Integer userId) {
        /** 判断是否存在会员结束日期及是否过期 **/
        String memberServiceEndTimestr = memberServiceUserRelationMapper.getMemberServiceEndTimeByUserId(userId);
        /** 如果会员日期为null则为从来购买过会员服务 **/
        if (memberServiceEndTimestr == null) {
            return false;
        }
        /** 转换为日期对象 **/
        Date memberServiceEndTime = DateUtils.formatStringToDate(memberServiceEndTimestr);
        /** 获取当前日期 **/
        Date currentDate = new Date();
        /** 比较如果当前日期大于了截止日期则不是会员 **/
        assert memberServiceEndTime != null;
        if (currentDate.compareTo(memberServiceEndTime) > 0) {
            return false;
        }
        return true;

    }

    @Override
    public BigDecimal findResidueLimitByUserId(Integer userId) {
        return memberServiceUserRelationMapper.getResidueLimitByUserId(userId);
    }
}
