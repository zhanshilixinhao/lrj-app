package com.lrj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/9/8 17:49
 */
@Controller
public class LrjController {
    @RequestMapping("")
    public String getHtml() {
        return "index";
    }
}
