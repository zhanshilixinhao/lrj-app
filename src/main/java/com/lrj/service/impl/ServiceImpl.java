package com.lrj.service.impl;

import com.lrj.mapper.Mapper;
import com.lrj.service.IService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lxh
 * @date 2020/3/30 12:00
 */
@Service
public class ServiceImpl implements IService {
    @Resource
    private Mapper mapper;
}
