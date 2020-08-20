package com.lrj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author 战士李昕昊
 */
@SpringBootApplication
@MapperScan("com.lrj.mapper")
public class LrjAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(LrjAppApplication.class, args);
    }
}
