package com.lrj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author Lxh
 * @Date 2020/5/12 14:39
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Component
@PropertySource("classpath:/properties/messages.properties")
public class ReturnUpLoad {
    /**判断是否有误*/
    private Integer error=0;
    /**图片存储地址*/
    private String url;
    /**图片宽度*/
    private Integer width;
    /**图片高度*/
    private Integer height;
    @Value("${img.localDir}")
    private String localDir;
    @Value("${img.localDirUrl}")
    private String localDirUrl;

    /**简化操作,可以提供静态方法*/
    public static ReturnUpLoad fail() {
        return new ReturnUpLoad(1, null, null, null,null,null);
    }
}
