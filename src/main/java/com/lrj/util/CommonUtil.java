package com.lrj.util;

import com.lrj.VO.FormerResult;
import com.lrj.common.Constant;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Lxh
 * @date 2020/4/2 14:15
 */
public class CommonUtil {
    /**
     * <b>BeanToRemoveNull</b>：(接口实体去除类型null的字段,注意只支持最简单的实体不能有实体嵌套)<br>
     * @param bean
     *            支持普通bean<br>
     * @return Object<br>
     * @Exception<br>
     * @author SAM QZL
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")

    public static void beanToRemoveNull(Object bean) {

        if (bean != null) {
            /** bean 为普通对象的情况 **/
            Class<? extends Object> beanClass = bean.getClass();
            /** 获取所有声明的实体 **/
            Field fileds[] = beanClass.getDeclaredFields();
            if (fileds != null) {
                /** 设置私有变量访问 **/
                Field.setAccessible(fileds, true);
                for (Field field : fileds) {
                    /** 如果有空值就替换 **/
                    try {
                        if (field.get(bean) == null) {
                            /** 如果是字符串类型转换为空字符串 **/
                            if (field.getType() == String.class) {
                                field.set(bean, "");
                            }
                            else if (field.getType() == Integer.class) {
                                /** 如果为数字类型替换为0 **/
                                field.set(bean, 0);
                            }
                            else if (field.getType() == Long.class) {
                                field.set(bean, 0l);
                            }
                            else if (field.getType() == BigDecimal.class) {
                                field.set(bean, new BigDecimal("0.00"));
                            }
                            else if (field.getType() == List.class) {
                                field.set(bean, new ArrayList());
                            }
                        }
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    /**
     * <b>SUCCESS</b>：(二期接口使用的接口成功方法)<br>
     * @return FormerResult<br>
     * @Exception<br>
     * @author SAM QZL
     */

    public static void objectToRemoveNull2(Object data) {

        /** 先判断是否是MAP--非MAP 处理 **/
        if (data instanceof Map != true) {
            /** LIST-处理 **/
            if (data instanceof List) {
                /** 调用list处理方法 **/
                listToRemoveNull2((List) data);
            }
            else {
                /** 非list处理 **/
                /** 不是原生类型则作为javaBean 处理 **/
                if (!isOrginObject(data)) {
                    beanToRemoveNull2(data);
                }
                /** 原生类型直接替换赋值 **/
                else {
                    if (data instanceof String) {
                        data = "";
                    }
                    else if (data instanceof Integer) {
                        data = 0;
                    }
                    else if (data instanceof Long) {
                        data = 0l;
                    }
                    else if (data instanceof BigDecimal) {
                        data = new BigDecimal("0.00");
                    }
                }
            }
        }
        else {
            /** MAP处理 **/
            /** 强制转换为Map **/
            Map map = (Map) data;
            /** 迭代处理 **/
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
                if (entry.getValue() == null) {
                    /** 设置为空字符串 **/
                    entry.setValue("");
                }
                else {
                    /** 非空处理 **/
                    if (entry.getValue() instanceof List) {
                        /** list处理 **/
                        listToRemoveNull2((List) entry.getValue());
                    }
                    else if (entry.getValue() instanceof Map) {
                        /** 继续递归处理 **/
                        objectToRemoveNull2(entry.getValue());
                    }
                    else {
                        /** 如果不是原生类型 **/
                        if (!isOrginObject(entry.getValue())) {
                            /** 作为javabean处理 **/
                            beanToRemoveNull2(entry.getValue());
                        }
                    }
                }
            }
        }
    }
    /**
     * <b>BeanToRemoveNull2</b>：(JavaBean过滤null)<br>
     *
     * @param bean
     *            支持普通bean<br>
     * @return Object<br>
     * @Exception<br>
     * @author SAM QZL
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void beanToRemoveNull2(Object bean) {

        if (bean != null) {
            /** bean 为普通对象的情况 **/
            Class<? extends Object> beanClass = bean.getClass();
            /** 获取所有声明的实体 **/
            Field fileds[] = beanClass.getDeclaredFields();
            if (fileds != null&&!beanClass.isEnum()) {
                /** 设置私有变量访问 **/
                Field.setAccessible(fileds, true);
                /** 遍历每个字段 **/
                for (Field field : fileds) {
                    try {
                        /** 为null 替换 **/
                        if (field.get(bean) == null) {
                            /** 如果是字符串类型转换为空字符串 **/
                            if (field.getType() == String.class) {
                                field.set(bean, "");
                            }
                            else if (field.getType() == Integer.class) {
                                field.set(bean, 0);
                            }
                            else if (field.getType() == Long.class) {
                                field.set(bean, 0l);
                            }
                            else if (field.getType() == BigDecimal.class) {
                                field.set(bean, new BigDecimal("0.00"));
                            }
                            else if (field.getType() == Boolean.class) {
                                field.set(bean, false);
                            }
                            else if (field.getType() == List.class) {
                                field.set(bean, new ArrayList());
                            }
                            else if (field.getType() == Map.class) {
                                field.set(bean, new HashMap());
                            }
                        }
                        /** 不为null处理 **/
                        else {
                            /** 如果bean中有List或Map **/
                            if (field.getType() == List.class) {
                                listToRemoveNull2((List) field.get(bean));
                            }
                            else if (field.getType() == Map.class) {
                                objectToRemoveNull2(field.get(bean));
                            }
                            /** 原生数据或javabean处理 **/
                            else {
                                if (!isOrginObject(field.get(bean))) {
                                    beanToRemoveNull2(field.get(bean));
                                }
                                /** String类型的日期处理 **/
                                if (field.getType() == String.class) {
                                    String s = (String) field.get(bean);
                                    if (s.contains(".0") && s.contains("-") && s.contains(":")) {
                                        s = s.substring(0, s.lastIndexOf("."));
                                        field.set(bean, s);
                                    }
                                }
                            }
                        }
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    /**
     * <b>ListToRemoveNull</b>：(list类型null过滤)<br>
     *

     *            <T> list list对象 T为实际装的对象<br>
     * @return void<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public static <T> void listToRemoveNull2(List<T> list) {

        if (list != null && list.size() > 0) {
            /** 遍历每一个对象去空 **/
            for (T t : list) {
                /** 不是原生类型 **/
                if (!isOrginObject(t)) {
                    /** 作为javabean去空 **/
                    beanToRemoveNull2(t);
                }
            }
        }
    }
    /**
     * <b>isOrginObject</b>：(判断是否是8种原生数据类型)<br>
     * @return boolean true 是 false 不是 <br>
     * @Exception<br>
     * @author SAM QZL
     */
    public static boolean isOrginObject(Object data) {

        if (data instanceof String != true && data instanceof Integer != true
                && data instanceof Long != true && data instanceof BigDecimal != true
                && data instanceof Boolean != true) {
            return false;
        }
        else {
            return true;
        }
    }
    public static FormerResult SUCCESS(FormerResult result, String tip, Object data) {


        objectToRemoveNull2(data);
        result.setRequestStatus(Constant.SUCCESS);
        result.setErrorCode(Constant.YES);
        result.setErrorTip("获取成功!");
        result.setData(data);
        if (tip != null && !"".equals(tip.trim())) {
            result.setErrorTip(tip);
        }
        return result;
    }
    /**
     * <b>success</b>：(接口执行失败!)<br>
     *
     * @param<br>
     * @param<br>
     * @return FormerResult<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public static FormerResult FAIL(FormerResult result, String tip, Object data) {

        result.setRequestStatus(Constant.SUCCESS);
        result.setErrorCode(Constant.NO);
        result.setErrorTip("程序异常!");
        result.setData(data);
        if (tip != null && !"".equals(tip.trim())) {
            result.setErrorTip(tip);
        }
        return result;
    }

}
