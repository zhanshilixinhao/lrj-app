package com.lrj.service.impl;

import com.lrj.dto.Local;
import com.lrj.dto.ReturnUpLoad;
import com.lrj.mapper.IUserMapper;
import com.lrj.mapper.UserMapper;
import com.lrj.service.IUpLoadService;

import jdk.nashorn.internal.ir.IdentNode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/5/13 14:22
 */
@Service
public class UpLoadServiceImpl implements IUpLoadService {

    @Resource
    private ReturnUpLoad returnUpLoad;

    @Resource
    private UserMapper userMapper;
    /**
     * @param: uploadFile
     * @Description: 通用图片上传
     * @Author: LxH
     * @Date: 2020/5/13 14:26
     */
    @Override
    public ReturnUpLoad fileUpload(Map<String, MultipartFile> fileMap,Integer uploadType,HttpServletRequest request) {
        System.out.println("输出看看："+fileMap);
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            //1.判断文件是否为图片类型   abc.jpg
            String fileName = entity.getValue().getOriginalFilename();
            //将字符串转化为小写
            fileName = fileName.toLowerCase();
            if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
                //表示不满足规则
                return ReturnUpLoad.fail();
            }
            try {
                //2.判断是否为恶意程序 转化为图片对象
                BufferedImage bufferedImage =
                        ImageIO.read(entity.getValue().getInputStream());
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                if(width==0 || height==0) {
                    return ReturnUpLoad.fail();
                }


                //3.实现分文件存储  按照上传类型存储不同文件目录
                String dateDir = null;
                switch (uploadType){
                    case 1:  //头像上传
                        dateDir = "/usr/local/project/images/userHeadPhotos/";
                        break;
                    case 2: //评论图片上传
                        dateDir = "/usr/local/project/images/userCommentImages/";
                        break;
                    case 3: //自助理赔图片 上传
                        dateDir = "F:\\java\\QRCode\\merchantCode\\";
                        break;
                    case 4: //助手端订单衣服图片上传
                        break;
                    case 5: //活动图片上传
                        dateDir = "/usr/local/project/images/activityImages/";
                        break;
                }


                //4.生成文件名称防止重名  name.type
                int index = fileName.lastIndexOf(".");
                //.jpg
                String fileType = fileName.substring(index);
                String uuid = UUID.randomUUID().toString();
                //拼接文件名称
                String realFileName = uuid + fileType;

                //5.实现文件上传
                entity.getValue().transferTo
                        (new File(dateDir+realFileName));

                String photo = null;
                switch (uploadType){
                    case 1:  //头像上传
                        photo = "/userHeadPhotos/"+realFileName;
                        Integer userId = Integer.parseInt(request.getParameter("userId"));
                        Map<String, Object> params = new HashMap<>();
                        params.put("userId", userId);
                        params.put("photo", photo);
                        userMapper.uploadUserHeadPhotos(params);
                        break;
                    case 2: //评论图片上传
                        photo = "/userHeadPhotos/"+realFileName;
                        break;
                    case 3: //自助理赔图片 上传
                        photo = "/selfHelpClaims/"+realFileName;
                        break;
                    case 4: //助手端订单衣服图片上传
                        photo = "/userHeadPhotos/"+realFileName;
                        break;
                    case 5: //活动图片上传
                        photo = "/activityImages/"+realFileName;
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ReturnUpLoad.fail();
            }
        }


        return returnUpLoad;
    }
}
