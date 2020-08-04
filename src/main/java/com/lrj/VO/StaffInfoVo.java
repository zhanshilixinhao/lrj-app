package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

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

    private Integer appStaffId;
    private String staffName = "";
    private String staffPassword = "";
    private String realName; //
    private String telePhone;
    private String headPhoto;
    private Date registerTime;
    private Integer active;
    private String address;
    private Integer type;

}
