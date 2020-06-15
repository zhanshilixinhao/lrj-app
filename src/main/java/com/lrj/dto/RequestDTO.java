package com.lrj.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : fl
 * @describe : 分页查询参数类
 * @date : 2020-4-30
 */
@Getter
@Setter
@Accessors(chain = true)
public class RequestDTO<E> {
    /**
     *查询参数实体
     */
    E object;

    /**
     * 查询页
     */
    Integer page=0;

    /**
     * 条数
     */
    Integer size=15;

    /**
     * 开始时间
     */
    String beginTime;

    /**
     * 结束时间
     */
    String endTime;

    LocalDateTime startLocalDateTime;
    LocalDateTime endLocalDateTime;

}
