package com.lrj.mapper;

import com.lrj.VO.AppApkVersionVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : cwj
 * @describe : apk 版本
 * @date : 2020-4-1
 */
@Repository
public interface IAppApkVersionMapper {

    /**
     * 查询apk 版本
     * @return
     */
    List<AppApkVersionVo> getApkVersion();
}
