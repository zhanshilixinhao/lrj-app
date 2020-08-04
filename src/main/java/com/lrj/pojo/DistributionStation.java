package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author : cwj
 * @describe : 配送站实体类
 * @date : 2020-7-28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DistributionStation {
    @Id
    private Integer distributionStationId;
    private String distributionName;
    private String longitude;
    private String latitude;

}
