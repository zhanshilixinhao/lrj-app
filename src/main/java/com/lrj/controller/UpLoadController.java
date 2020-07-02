package com.lrj.controller;

import com.lrj.dto.ReturnUpLoad;
import com.lrj.service.IUpLoadService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;

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
    @RequestMapping("fileUpload")
    public ReturnUpLoad fileUpload(MultipartFile uploadFile){
        return upLoadService.fileUpload(uploadFile);
    }
}
