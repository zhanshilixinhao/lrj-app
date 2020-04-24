package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Lxh
 * @date 2020/4/23 16:34
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class Reservation extends Base{
    private static final long serialVersionUID = 5087055302184564912L;
    @Id
    private Integer reservationId;
    private Byte status;
    private Byte trackingStatus;
    private Integer orderType;
    private Integer grabOrderId;
    private String address;
    private Integer userId;
    private String orderNumber;
    private String longitude;
    private String latitude;
}
