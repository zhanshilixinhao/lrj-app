package com.lrj.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserLevelVo {
	private Integer userId;
	private Integer levelId; //用户等级Id
	private String levelName; //等级名称
	private String description; //等级说明
	private BigDecimal discount; // 等级折扣
	private BigDecimal distributionRatio;//分销比例
	private String remark; //备注
	private String privilegeDescription; //会员权限说明
	private Integer inviteNum;  //邀请人数
	private Integer upPeopleNum; // 升级所差人数
}
