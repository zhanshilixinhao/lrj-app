package com.lrj.VO;

import com.lrj.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lxh
 * @date 2020/4/24 15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAppointVo extends Order {
    private static final long serialVersionUID = 1008841595335226850L;
    private String name;
    private String address;
    private String consigneeMobile;
    private String consigneeName;
    private Integer isDefault;

}
