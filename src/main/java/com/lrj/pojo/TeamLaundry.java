package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Lxh
 * @date 2020/4/28 18:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "team_laundry")
public class TeamLaundry extends Base{
    private static final long serialVersionUID = -4621621942529670826L;
    @Id
    private Integer id;
    private Integer userId;
    private Integer leaderUserId;
    private String nickName;
    private String phoneNum;
    private String headPhoto;
    private BigDecimal countLimit;
    private BigDecimal useLimit;
}
