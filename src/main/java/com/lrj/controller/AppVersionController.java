package com.lrj.controller;

import com.lrj.VO.AppApkVersionVo;
import com.lrj.VO.BannerVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IAppApkService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : cwj
 * @describe : app 版本控制
 * @date : 2020-4-1
 */
@RestController
public class AppVersionController {

    @Resource
    private IAppApkService appApkService;

    @RequestMapping(value = "/getApkVersion",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getApkVersion(HttpServletRequest request){
        //由于返回结果只有一条，故建立新的集合存放
        List<AppApkVersionVo> apkList = new ArrayList<AppApkVersionVo>();

        ResultVo resultVo = new ResultVo();
        resultVo.setRequestStatus("success");

        List<AppApkVersionVo> appApkVersionVoList = appApkService.getApkVersion();
        AppApkVersionVo apk = appApkVersionVoList.get(0);
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + path + "/";
        String url = basePath + "file-download/apk";
        /**设置下载地址**/
        apk.setUrl(url);
        resultVo.setErrorCode(0);
        resultVo.setErrorTip("获取成功");

        apkList.add(apk);
        resultVo.setData(apkList);
        return resultVo;
    }
}
