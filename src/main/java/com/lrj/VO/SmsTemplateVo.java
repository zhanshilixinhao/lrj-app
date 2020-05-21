package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : cwj
 * @describe : 分拣员订单备注
 * @date :2020-5-20
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sms_template")
public class SmsTemplateVo {

    @Id
    private Integer smsTemplateId; //模板ID
    private long orderId; //订单编号
    private Integer isWearOut; //是否破损:1.有 0.无
    private Integer isDye; //是否染色:1.有 0.无
    private Integer isBallingUp; //是否起球:1.有 0.无
    private Integer isYellowWhite; //是否发黄发白:1.有 0.无
    private Integer isCrossColor; //是否串色:1.有 0.无
    private String other = ""; //其他
    private String createTime = ""; //创建时间
    private String orderNumber;  //订单号
}
