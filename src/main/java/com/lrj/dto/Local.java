package com.lrj.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/5/13 16:00
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
//@AllArgsConstructor
public class Local implements Serializable {
    private static final long serialVersionUID = 6810494732420784373L;
    private final String localDir="D:/wy/pic";
    private final String localDirUrl="http://51lrj.com/";
}
