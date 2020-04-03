package com.lrj.controller;

import com.lrj.VO.OrderCommentVo;
import com.lrj.VO.ResultVo;
import com.lrj.service.IOrderCommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
}
