package com.lrj.VO;

/**
 * @author : cwj
 * @describe : apk 版本
 * @date : 2020-4-1
 */
public class AppApkVersionVo {
    private Integer apkId; // 主键ID
    private String apkMsg = ""; // apk版本描述
    private String upTime = ""; // 上传时间
    private String version = "";// 版本号
    private String url = "";//下载链接
    public AppApkVersionVo(){

    }

    public AppApkVersionVo(Integer apkId, String apkMsg, String upTime, String version, String url) {
        this.apkId = apkId;
        this.apkMsg = apkMsg;
        this.upTime = upTime;
        this.version = version;
        this.url = url;
    }

    public Integer getApkId() {
        return apkId;
    }

    public void setApkId(Integer apkId) {
        this.apkId = apkId;
    }

    public String getApkMsg() {
        return apkMsg;
    }

    public void setApkMsg(String apkMsg) {
        this.apkMsg = apkMsg;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
