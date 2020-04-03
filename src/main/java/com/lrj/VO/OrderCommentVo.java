package com.lrj.VO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : cwj
 * @describe : 评论
 * @date : 2020-4-1 14:19:17
 */
public class OrderCommentVo {
    private String commentContent; //评论内容
    private String createTime; //记录时间
    private BigDecimal star; //综合星级评分
    private String commentImage; //评论图片存储区 多张用,隔开
    private String headPhoto;
    private String nickname; //用户昵称
    private String userPhone; //用户手机号
    private List<String> commentImages; //评论图片存储区 多张用,隔开

    public OrderCommentVo(){

    }
    public OrderCommentVo(String commentContent, String createTime, BigDecimal star, String commentImage, String headPhoto, String nickname, String userPhone, List<String> commentImages) {
        this.commentContent = commentContent;
        this.createTime = createTime;
        this.star = star;
        this.commentImage = commentImage;
        this.headPhoto = headPhoto;
        this.nickname = nickname;
        this.userPhone = userPhone;
        this.commentImages = commentImages;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getStar() {
        return star;
    }

    public void setStar(BigDecimal star) {
        this.star = star;
    }

    public String getCommentImage() {
        return commentImage;
    }

    public void setCommentImage(String commentImage) {
        this.commentImage = commentImage;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public List<String> getCommentImages() {
        return commentImages;
    }

    public void setCommentImages(List<String> commentImages) {
        this.commentImages = commentImages;
    }
}
