package com.lrj.controller;

import com.lrj.VO.BannerVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IBannerService;
import com.lrj.util.MessagesUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : cwj
 * @describe : 轮播图
 * @date : 2020-4-1
 */
@RestController
public class BannerController {

    @Resource
    private IBannerService bannerService;

    @RequestMapping(value = "/getBannerList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getBannerList(HttpServletRequest request){
        List<BannerVo> bannerVoList = bannerService.getBannerList();
        /** 获取请求地址 **/
        StringBuffer url = request.getRequestURL();
        /** 拼接 **/
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        /** 拼接可访问图片地址 **/
        for (BannerVo bannerVo : bannerVoList) {
            /** 图片地址 **/
            bannerVo.setBannerImg(tempContextUrl + bannerVo.getBannerImg());
        }
        return new ResultVo("success",0,"查询成功",bannerVoList);
    }
}
