package com.lrj.mapper;

import com.lrj.VO.OrderCommentVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : 评论 mapper接口
 * @date : 2020-4-1
 */
@Repository
public interface IOrderCommentMapper {

    /**
     * 查询 显示的订单评论
     */
    List<OrderCommentVo> listLatestOrderComment();
}
