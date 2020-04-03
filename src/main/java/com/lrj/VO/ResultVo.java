package com.lrj.VO;

import java.util.List;

/**
 * @author : cwj
 * @describe : 消息封装返回类
 * @date : 时间
 */
public class ResultVo {
    private String requestStatus;
    private Integer errorCode;
    private String errorTip;
    private List data;


    public ResultVo() {

    }
    public ResultVo(String requestStatus, Integer errorCode, String errorTip, List data) {
        this.requestStatus = requestStatus;
        this.errorCode = errorCode;
        this.errorTip = errorTip;
        this.data = data;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorTip() {
        return errorTip;
    }

    public void setErrorTip(String errorTip) {
        this.errorTip = errorTip;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
