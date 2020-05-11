package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : cwj
 * @describe : 懒人家助手 员工
 * @date : 2020-5-8
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StaffInfoVo {

    private Integer sysAdminId; // id
    private String adminName = ""; // 名字
    private String adminPassword = ""; // 密码
    private Integer active; //  状态：启动，禁用
    private Integer sysAdminRoleType; // 角色类别
    private String sysAdminRoles; // 用户具有的角色串
    private String createTime;    //创建时间
    private Integer sysType; //
    private String invitationCode ="";//商家邀请码
    private String businessName = "";//商家名称
    private String businessAddress = ""; //商家地址
    private String businessPhone = ""; //商家联系电话
    private String businessContactPerson = ""; //商铺联系人
    private String businessInfo = ""; //商铺信息
    private Double businessDistributionRatio = 0.00; //分销比例
}
