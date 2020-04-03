package com.lrj.controller;

import com.lrj.VO.ConsigneeVo;
import com.lrj.service.IUserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.lrj.VO.ResultVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 用户控制层
 * @date : 2020-3-30
 */
@RestController
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 接收极光推送的token
     */
    @RequestMapping(value = "/toOauthLogin",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String,Object> oauthLogin(HttpServletRequest request){
        String token = request.getParameter("token");
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result", "success");
        return resultMap;
    }

    /**
     * 获取用户的地址
     */
    @RequestMapping(value = "/getUserAddressList",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultVo getUserAddressList(Integer userId){
        /** 校验必须参数 **/
        if (userId == null) {
            return new ResultVo("success", 1, "参数有误,请检查参数",null);
        }
        List<ConsigneeVo> consigneeVoList= userService.findUserAddressByUserId(userId);
        return new ResultVo("success",0,"查询成功",consigneeVoList);
    }
}
