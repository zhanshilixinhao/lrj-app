package com.lrj.service;

import com.lrj.VO.OrderCommentVo;

import java.util.List;

/**
 * @author : cwj
 * @describe : 评论 服务
 * @date : 2020-4-1
 */
public interface IOrderCommentService {

    List<OrderCommentVo> listLatestOrderComment();


    /**
     * 根据员工Id 查询对应服务的评论
     * @param staffId
     * @return
     */
    List<OrderCommentVo> getMyReservationComment(Integer staffId);

    /**
     * 评论服务订单
     * @param orderCommentVo
     * @return
     */
    Integer addReservationComment(OrderCommentVo orderCommentVo);
}
