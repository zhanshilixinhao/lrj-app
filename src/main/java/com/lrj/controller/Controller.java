package com.lrj.controller;

import com.lrj.service.IService;

import javax.annotation.Resource;

/**
 * @author Lxh
 * @date 2020/3/30 11:59
 */
@org.springframework.stereotype.Controller
public class Controller {
    @Resource
    private IService service;
}
