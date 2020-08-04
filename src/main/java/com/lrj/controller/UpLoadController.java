package com.lrj.controller;

import com.lrj.dto.ReturnUpLoad;
import com.lrj.service.IUpLoadService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/5/13 14:19
 */
@RestController
public class UpLoadController {

    @Resource
    private IUpLoadService upLoadService;

    /**
     * @Description: 通用图片上传
     * @Author: LxH
     * @Date: 2020/5/13 14:25
     */
    @RequestMapping(value = "/fileUpload",method = {RequestMethod.GET,RequestMethod.POST})
    public ReturnUpLoad fileUpload(Integer uploadType,HttpServletRequest request){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        Map<String, MultipartFile> fileMap = null;
        if (multipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            fileMap = multiRequest.getFileMap();
        }
        
        return upLoadService.fileUpload(fileMap,uploadType,request);
    }
}
