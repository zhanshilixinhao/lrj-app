package com.lrj.VO;

/**
 * @author : cwj
 * @describe : 首页轮播图
 * @date : 2020-4-1
 */
public class BannerVo {
    private String bannerName;
    private String bannerDetails;
    private Integer bannerType; //1.富文本 2.外链 3.跳转充值 4.跳转包年 5.跳转活动 6.跳转问卷
    private String bannerImg;
    private String url;

    public BannerVo(){

    }

    public BannerVo(String bannerName, String bannerDetails, Integer bannerType, String bannerImg, String url) {
        this.bannerName = bannerName;
        this.bannerDetails = bannerDetails;
        this.bannerType = bannerType;
        this.bannerImg = bannerImg;
        this.url = url;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerDetails() {
        return bannerDetails;
    }

    public void setBannerDetails(String bannerDetails) {
        this.bannerDetails = bannerDetails;
    }

    public Integer getBannerType() {
        return bannerType;
    }

    public void setBannerType(Integer bannerType) {
        this.bannerType = bannerType;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
