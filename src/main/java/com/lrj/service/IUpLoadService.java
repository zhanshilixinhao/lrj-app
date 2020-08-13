package com.lrj.service;

import com.lrj.dto.ReturnUpLoad;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/5/13 14:22
 */
public interface IUpLoadService {

    /**
     * @Description: 通用图片上传
     * @Author: LxH
     * @Date: 2020/5/13 14:26
     */
    ReturnUpLoad fileUpload(MultipartFile files, Integer uploadType, HttpServletRequest request);
}
