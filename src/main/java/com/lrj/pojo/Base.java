package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Lxh
 * @date 2020/4/16 11:07
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Base implements Serializable {
    private static final long serialVersionUID = -3668598027653287933L;
    private String createTime;
    @Transient
    private String updateTime;
}
