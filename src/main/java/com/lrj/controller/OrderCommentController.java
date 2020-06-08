package com.lrj.controller;

import com.lrj.VO.FormerResult;
import com.lrj.VO.OrderCommentVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IOrderCommentService;
import com.lrj.util.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 评论
 * @date : 2020-4-1
 */
@RestController
public class OrderCommentController {

    @Resource
    private IOrderCommentService commentService;

    /**
     * 获取评论列表数据
     * @return
     */
    @RequestMapping(value = "/listLatestOrderComment",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo listLatestOrderComment(){
        ResultVo resultVo = new ResultVo();
        resultVo.setRequestStatus("success");
        List<OrderCommentVo> orderCommentVoList= commentService.listLatestOrderComment();
        resultVo.setErrorCode(0);
        resultVo.setErrorTip("查询成功");
        resultVo.setData(orderCommentVoList);
        return resultVo;
    }

    /**
     * 评论服务订单
     */
    @RequestMapping(value = "/addReservationComment",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo addReservationComment(Integer userId,String orderNumber,String content,String commentImage,Integer reservationId){
        OrderCommentVo orderCommentVo = new OrderCommentVo();
        orderCommentVo.setCommentContent(content);
        orderCommentVo.setCreateTime(DateUtils.getNowTime("YYYY-MM-DD HH:mm:ss"));
        orderCommentVo.setCommentImage(commentImage);
        orderCommentVo.setUserId(userId);
        orderCommentVo.setOrderNumber(orderNumber);
        orderCommentVo.setReservationId(reservationId);
        return new ResultVo("SUCCESS", 0, "评论成功", null);
    }

    /**
     * 图片上传
     */
    @RequestMapping("/fileUploadController")
    public FormerResult fileUpload(MultipartFile filename){
        System.out.println(filename.getOriginalFilename());
        try {
            filename.transferTo(new File("F:\\java\\myproject\\"+filename.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FormerResult("SUCCESS", 0, "图片上传成功", null);
    }

}
