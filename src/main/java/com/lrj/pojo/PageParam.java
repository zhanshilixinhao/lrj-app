package com.lrj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Lxh
 * @date 2020/4/16 17:52
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageParam implements Serializable {
    private static final long serialVersionUID = 213376033416827327L;
    protected Integer currentPage=1;// 当前页
    protected Integer pageSize=getPageSize();
    public Integer getPageSize() {
        if (pageSize == null) {
            setPageSize(10);
        }
        return pageSize;
    }
}
