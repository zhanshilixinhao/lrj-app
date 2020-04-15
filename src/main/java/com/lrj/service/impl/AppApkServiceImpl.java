package com.lrj.service.impl;

import com.lrj.VO.AppApkVersionVo;
import com.lrj.mapper.IAppApkVersionMapper;
import com.lrj.service.IAppApkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : cwj
 * @describe : apk 服务
 * @date : 2020-4-1
 */
@Service
@Transactional
public class AppApkServiceImpl implements IAppApkService {

    @Resource
    private IAppApkVersionMapper appApkVersionMapper;

    public List<AppApkVersionVo> getApkVersion() {
        return appApkVersionMapper.getApkVersion();
    }
}
