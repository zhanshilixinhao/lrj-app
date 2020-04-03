package com.lrj.service;

import com.lrj.VO.AppApkVersionVo;

import java.util.List;

/**
 * @author : cwj
 * @describe : apk 服务
 * @date : 2020-4-1
 */
public interface IAppApkService {

    List<AppApkVersionVo> getApkVersion();
}
