package com.lrj.mapper;

import com.lrj.VO.OrderCommentVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据员工ID 查询对应服务的评论
     * @param staffId
     * @return
     */
    List<OrderCommentVo> getMyReservationComment(Integer staffId);

    /**
     * 评论服务单
     * @param orderCommentVo
     * @return
     */
    Integer addReservationComment(OrderCommentVo orderCommentVo);

    /**
     * 添加评论
     * @param params
     */
    void uploaduserCommentImages(Map<String, Object> params);
}
