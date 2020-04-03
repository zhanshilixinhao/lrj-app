package com.lrj.controller;

import com.lrj.VO.BannerVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IBannerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public ResultVo getBannerList(){
        List<BannerVo> bannerVoList = bannerService.getBannerList();
        return new ResultVo("success",0,"查询成功",bannerVoList);
    }
}
