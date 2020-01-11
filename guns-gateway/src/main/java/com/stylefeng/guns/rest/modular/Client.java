package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/9 2:04 PM
 */
@Component
public class Client {
    @Reference(interfaceClass = UserAPI.class, timeout = 30000)
    private UserAPI userAPI;

    public void run(){
        userAPI.login("admin", "admin");
    }

}
