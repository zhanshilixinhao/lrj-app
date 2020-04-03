package com.lrj.VO;

import java.math.BigDecimal;

/**
 * 
 * <b>项目名称</b>：lanrenxiyi<br>    
 * <b>类名称</b>：ValueAddedServicesVo<br>     
 * <b>类描述</b>：购物车，结算中心增值服务VO类<br>     
 * <b>创建人</b>：SAM QZL<br>    
 * <b>创建时间</b>：2017-8-4 下午1:54:01<br>     
 * <b>修改人</b>：SAM QZL<br>     
 * <b>修改时间</b>：2017-8-4 下午1:54:01<br>     
 * <b>修改备注</b>：<br>     
 * @author SAM QZL<br> 
 * @version     
 *
 */
public class ValueAddedServicesVo {

    private Integer valueAddedServicesId;
    private Integer categoryId;
    private BigDecimal servicePrice;
    private String serviceName;
    private String serviceDescription;

    /**
     * categoryId
     * 
     * @return the categoryId
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    public Integer getCategoryId() {

        return categoryId;
    }

    /**
     * @param categoryId
     *            the categoryId to set
     */
    public void setCategoryId(Integer categoryId) {

        this.categoryId = categoryId;
    }

    /**
     * serviceDescription
     * 
     * @return the serviceDescription
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    public String getServiceDescription() {

        return serviceDescription;
    }

    /**
     * @param serviceDescription
     *            the serviceDescription to set
     */
    public void setServiceDescription(String serviceDescription) {

        this.serviceDescription = serviceDescription;
    }

    /**
     * valueAddedServicesId
     * 
     * @return the valueAddedServicesId
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    public Integer getValueAddedServicesId() {

        return valueAddedServicesId;
    }

    /**
     * @param valueAddedServicesId
     *            the valueAddedServicesId to set
     */
    public void setValueAddedServicesId(Integer valueAddedServicesId) {

        this.valueAddedServicesId = valueAddedServicesId;
    }

    /**
     * servicePrice
     * 
     * @return the servicePrice
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    public BigDecimal getServicePrice() {

        return servicePrice;
    }

    /**
     * @param servicePrice
     *            the servicePrice to set
     */
    public void setServicePrice(BigDecimal servicePrice) {

        this.servicePrice = servicePrice;
    }

    /**
     * serviceName
     * 
     * @return the serviceName
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    public String getServiceName() {

        return serviceName;
    }

    /**
     * @param serviceName
     *            the serviceName to set
     */
    public void setServiceName(String serviceName) {

        this.serviceName = serviceName;
    }

    @Override
    public String toString() {

        return "ValueAddedServicesVo [valueAddedServicesId=" + valueAddedServicesId
                + ", categoryId=" + categoryId + ", servicePrice=" + servicePrice
                + ", serviceName=" + serviceName + ", serviceDescription=" + serviceDescription
                + "]";
    }
}
