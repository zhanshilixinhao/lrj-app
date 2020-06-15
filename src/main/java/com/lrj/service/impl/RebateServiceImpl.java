package com.lrj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lrj.dto.RequestDTO;
import com.lrj.dto.ReturnData;
import com.lrj.mapper.RebateMapper;
import com.lrj.pojo.Rebate;
import com.lrj.service.RebateService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lrj.dto.ReturnData.Fail_CODE;
import static com.lrj.dto.ReturnData.SUCCESS_CODE;
import static com.lrj.pojo.Rebate.USER_ID_COLUMN;

/**
 * @author fl
 * @descrip:
 * @date 2020/6/12 0012下午 2:11
 */
@Service
public class RebateServiceImpl implements RebateService {
    private RebateMapper rebateMapper;
    private ObjectMapper objectMapper;

    public RebateServiceImpl(RebateMapper rebateMapper, ObjectMapper objectMapper) {
        this.rebateMapper = rebateMapper;
        this.objectMapper = objectMapper;
    }

    public PageInfo<Rebate> getPageByParam(RequestDTO requestDTO) {
        QueryWrapper<Rebate> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc(Rebate.CREATE_TIME);
        Rebate rebate = null;
        try {
            rebate = objectMapper.convertValue(requestDTO.getObject(), Rebate.class);
            if (rebate != null && rebate.getUserId() != null) {
                queryWrapper.eq(USER_ID_COLUMN, rebate.getUserId());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        PageHelper.startPage(requestDTO.getPage(),requestDTO.getSize());
        List<Rebate> list = rebateMapper.selectList(queryWrapper);
        return new PageInfo<Rebate>(list);
    }

    public ReturnData<Boolean> add(Rebate rebate) {
        try {
            if (rebateMapper.insert(rebate) > 0) {
                return new ReturnData(SUCCESS_CODE,"操作成功", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReturnData(Fail_CODE,"操作失败",false );
    }
}
