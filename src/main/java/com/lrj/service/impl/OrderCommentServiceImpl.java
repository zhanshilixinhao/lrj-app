package com.lrj.service.impl;

import com.lrj.VO.OrderCommentVo;
import com.lrj.mapper.IOrderCommentMapper;
import com.lrj.service.IOrderCommentService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 评论 服务实现层
 * @date : 2020-5-21
 */
@Component
@Service
@Transactional
public class OrderCommentServiceImpl implements IOrderCommentService {

    @Resource
    private IOrderCommentMapper orderCommentMapper;

    public List<OrderCommentVo> listLatestOrderComment() {
        return orderCommentMapper.listLatestOrderComment();
    }

    public List<OrderCommentVo> getMyReservationComment(Integer staffId) {
        return orderCommentMapper.getMyReservationComment(staffId);
    }

    @Override
    public Integer addReservationComment(OrderCommentVo orderCommentVo) {
        return orderCommentMapper.addReservationComment(orderCommentVo);
    }

}
