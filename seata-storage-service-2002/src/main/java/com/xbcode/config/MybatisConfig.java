package com.xbcode.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.xbcode.dao"})
public class MybatisConfig {
}
