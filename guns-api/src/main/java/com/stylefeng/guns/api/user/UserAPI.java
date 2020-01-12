package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/8 6:21 PM
 */
public interface UserAPI {
    /**
     * return user uid
     * @param userName
     * @param password
     * @return
     */
    int login(String userName, String password);

    boolean register(UserModel userModel);

    boolean checkUsername(String username);

    UserInfoModel getUserInfo(int uuid);

    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);

}
