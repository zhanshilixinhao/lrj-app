package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
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
    @Id
    private Integer commentId; //主键
    private String commentContent; //评论内容
    private String createTime; //记录时间
    private double star; //综合星级评分
    private String commentImage; //评论图片存储区 多张用,隔开
    private String headPhoto;
    private String nickname; //用户昵称
    private String userPhone; //用户手机号
    private List<String> commentImages; //评论图片存储区 多张用,隔开
    private Integer userId;
    private Integer reservationId;//服务ID
    private String remark; //备注
    private Integer staffId;//服务员工ID

}
