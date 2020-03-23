package com.stylefeng.guns.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/30 4:18 PM
 */
@Configuration
@ImportResource(locations = {"classpath:tcc-transaction.xml","classpath:tcc-transaction-dubbo.xml"})
public class TCCConfig {

}
