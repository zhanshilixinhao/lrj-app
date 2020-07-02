package com.lrj.service.impl;

import com.lrj.dto.Local;
import com.lrj.dto.ReturnUpLoad;
import com.lrj.service.IUpLoadService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    /**
     * @param: uploadFile
     * @Description: 通用图片上传
     * @Author: LxH
     * @Date: 2020/5/13 14:26
     */
    @Override
    public ReturnUpLoad fileUpload(MultipartFile uploadFile) {
        String localDir = returnUpLoad.getLocalDir();
        String localDirUrl = returnUpLoad.getLocalDirUrl();
        //1.判断文件是否为图片类型   abc.jpg
        String fileName = uploadFile.getOriginalFilename();
        //将字符串转化为小写
        fileName = fileName.toLowerCase();
        if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
            //表示不满足规则
            return ReturnUpLoad.fail();

        }

        try {
            //2.判断是否为恶意程序 转化为图片对象
            BufferedImage bufferedImage =
                    ImageIO.read(uploadFile.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if(width==0 || height==0) {
                return ReturnUpLoad.fail();
            }


            //3.实现分文件存储  按照yyyy/MM/dd/
            String dateDir =
                    new SimpleDateFormat("yyyy/MM/")
                            .format(new Date());
            //生成文件目录    D://image/yyyy/MM/dd
            String fileDirPath = localDir + dateDir;
            File dirFile = new File(fileDirPath);
            //如果没有目录,则创建目录
            if(!dirFile.exists()) {
                dirFile.mkdirs();
            }


            //4.生成文件名称防止重名  name.type
            int index = fileName.lastIndexOf(".");
            //.jpg
            String fileType = fileName.substring(index);
            String uuid = UUID.randomUUID().toString();
            //拼接文件名称
            String realFileName = uuid + fileType;

            //5.实现文件上传
            uploadFile.transferTo
                    (new File(fileDirPath+realFileName));


            //定义url虚拟地址
            String url = localDirUrl +dateDir + realFileName;


            //暂时使用网络地址代替真是url地址.
            returnUpLoad.setWidth(width)
                    .setHeight(height)
                    .setUrl(url);



        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUpLoad.fail();
        }

        return returnUpLoad;
    }
}
