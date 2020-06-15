package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author linqin
 * @date 2018/9/4
 */
@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "third_acc")
public class ThirdAcc extends Base{
    private static final long serialVersionUID = 7836674568401936452L;
    @Id
    private Integer id;
    private String phone;
    private String openId;
    private Byte accType;

    public static final String COLUMN_THIRDACC_OPEN_ID = "openId";
    public static final String COLUMN_THIRDACC_PHONE = "phone";
}
