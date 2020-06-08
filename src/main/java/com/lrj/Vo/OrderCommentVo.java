package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : cwj
 * @describe : 评论
 * @date : 2020-4-1 14:19:17
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderCommentVo {
    private String commentContent; //评论内容
    private String createTime; //记录时间
    private BigDecimal star; //综合星级评分
    private String commentImage; //评论图片存储区 多张用,隔开
    private String headPhoto;
    private String nickname; //用户昵称
    private String userPhone; //用户手机号
    private List<String> commentImages; //评论图片存储区 多张用,隔开
    private Integer userId;
    private String orderNumber; //订单号
    private Integer reservationId;//服务ID

}
