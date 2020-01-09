package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.user.UserAPI;
import org.springframework.stereotype.Component;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/8 6:22 PM
 */
@Component
@Service(interfaceClass = UserAPI.class)
public class UserImpl implements UserAPI {
    @Override
    public boolean login(String userName, String password) {
        System.out.println("this is user service!" + userName + " " + password);
        return false;
    }
}
