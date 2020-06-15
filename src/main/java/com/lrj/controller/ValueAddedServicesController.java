package com.lrj.controller;

import com.lrj.VO.ResultVo;
import com.lrj.VO.ValueAddedServicesVo;
import com.lrj.service.IItemService;
import com.lrj.service.IValueAddedServicesService;
import com.lrj.util.SimpleCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <b>项目名称</b>：lanrenxiyi<br>
 * <b>类名称</b>：ValueAddedServicesController<br>
 * <b>类描述</b>：增值服务接口<br>
 * <b>创建人</b>：SAM QZL<br>
 * <b>创建时间</b>：2017-7-28 上午11:27:49<br>
 * <b>修改人</b>：SAM QZL<br>
 * <b>修改时间</b>：2017-7-28 上午11:27:49<br>
 * <b>修改备注</b>：<br>
 * @author SAM QZL<br>
 * @version
 * 
 */
@Controller
public class ValueAddedServicesController extends BaseController {

    @Resource
    private IItemService itemService;

    @Resource
    private IValueAddedServicesService valueAddedServicesService;

    /** 无增值服务商品ID集合 **/
    private static final List<Integer> NONE_VALUE_ADDED_SERVICES_ITEM_ID = Arrays.asList(11, 139);
    /** 无增值服务商品类别ID集合 **/
    private static final List<Integer> NONE_VALUE_ADDED_SERVICES_ITEM_CATEGORY_ID = Arrays.asList(5);

    /**
     * @功能说明:获取增值服务列表.
     * @param result
     * @return 结果数据
     * @返回类型:FormerResult
     * @方法名称:listValueAddedServices
     * @类名称:ValueAddedServicesController
     * @文件名称:ValueAddedServicesController.java
     * @所属包名:com.lanrenxiyi.app.valueAddedServices
     * @项目名称:lanrenxiyi
     * @创建时间:2017-7-28 上午11:39:34
     * @作者:SAM QZL
     * @版本:1.0
     */
    @RequestMapping("/listValueAddedServices")
    @ResponseBody
    public ResultVo listValueAddedServices(ResultVo result, Integer itemId) {

        /** 必须字段校验 **/
        if (itemId == null) {
            return FAIL2(result, "参数错误，itemId不能为空！", null);
        }
        /** 获取itemId的顶层类别ID **/
        Integer itemCategoryId =  itemService.getItemCategoryByItemId(itemId);
        /** 无增值服务 **/
        if (NONE_VALUE_ADDED_SERVICES_ITEM_ID.contains(itemId)
                || NONE_VALUE_ADDED_SERVICES_ITEM_CATEGORY_ID.contains(itemCategoryId)) {
            return SUCCESS2(result, "查询成功！", null);
        }
        /** 从缓存中获取 **/
        List<ValueAddedServicesVo> listValueAddedServices = (List<ValueAddedServicesVo>) SimpleCache.getInstance().get(SimpleCache.CacheType.VALUES_ADD_SERVICES);
        /** 如果缓存中有则直接返回 **/
        if (listValueAddedServices == null || listValueAddedServices.isEmpty()) {
            /** 从数据库中获取 **/
            listValueAddedServices = valueAddedServicesService.listValueAddedServices();
            /** 存入缓存 **/
            SimpleCache.getInstance().put(SimpleCache.CacheType.VALUES_ADD_SERVICES, listValueAddedServices);
        }
        /** 筛选 **/
        List<ValueAddedServicesVo> listValueAddedServices2 = new ArrayList<ValueAddedServicesVo>();
        /** 遍历 **/
        for (ValueAddedServicesVo vo : listValueAddedServices) {
            if (vo.getCategoryId() == null || vo.getCategoryId().equals(itemCategoryId)
                    || vo.getCategoryId() == 0) {
                listValueAddedServices2.add(vo);
            }
        }
        /** 返回结果 **/
        return new ResultVo("SUCCESS",0, "查询成功！", listValueAddedServices2);
    }
}
