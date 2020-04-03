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
}
