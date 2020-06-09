package com.lrj.util;

import java.util.concurrent.TimeUnit;

public class UserCache extends BaseCache<String, String> {

    public UserCache(long duration, TimeUnit timeUtil) {
        super(duration, timeUtil);
    }

    @Override
    protected String loadData(String s) {
        return null;
    }

    @Override
    public String getCache(String param) {
//        if (param.equalsIgnoreCase("yichen")) {
//            RawData data = new RawData();
//            RawData.UserInfoBean userInfoBean = new RawData.UserInfoBean();
//            userInfoBean.setAvatarUrl("avatar.png");
//            userInfoBean.setNickName("yichen");
//            data.setOpenid("yichen");
//            data.setUserInfo(userInfoBean);
//            return JSON.toJSONString(data);
//        } else if (param.equalsIgnoreCase("18313747954")) {
//            return "123456";
//        } else if (param.equalsIgnoreCase("qwe")) {
//            return "yichen";
//        }
        return super.getCache(param);
    }
}
