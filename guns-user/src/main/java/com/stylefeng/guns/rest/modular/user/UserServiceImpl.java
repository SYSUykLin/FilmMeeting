package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.user.UserAPI;
import com.stylefeng.guns.api.user.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.UserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.UserT;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/8 6:22 PM
 */
@Component
@Service(interfaceClass = UserAPI.class, loadbalance = "roundrobin")
public class UserServiceImpl implements UserAPI {

    @Resource
    private UserTMapper userTMapper;

    @Override
    public int login(String userName, String password) {
        UserT userT = new UserT();
        userT.setUserName(userName);
        UserT result = userTMapper.selectOne(userT);
        if (result != null && result.getUuid() > 0) {
            String md5Password = MD5Util.encrypt(password);
            if (result.getUserPwd().equals(md5Password)) {
                return result.getUuid();
            }
        }
        return 0;
    }

    @Override
    public boolean register(UserModel userModel) {
        UserT userT = new UserT();
        userT.setUserName(userModel.getUsername());
        userT.setEmail(userModel.getEmail());
        userT.setAddress(userModel.getAddress());
        userT.setUserPhone(userModel.getPhone());
        String md5Password = MD5Util.encrypt(userModel.getPassword());
        userT.setUserPwd(md5Password);
        Integer integer = userTMapper.insert(userT);
        if (integer > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<UserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name", username);
        Integer result = userTMapper.selectCount(entityWrapper);
        if (result != null && result > 0) {
            return false;
        } else
            return true;
    }

    private UserInfoModel do2UserInfo(UserT userT) {
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUuid(userT.getUuid());
        userInfoModel.setHeadAddress(userT.getHeadUrl());
        userInfoModel.setPhone(userT.getUserPhone());
        userInfoModel.setUpdateTime(userT.getUpdateTime().getTime());
        userInfoModel.setEmail(userT.getEmail());
        userInfoModel.setUsername(userT.getUserName());
        userInfoModel.setNickname(userT.getNickName());
        userInfoModel.setLiftState("" + userT.getLifeState());
        userInfoModel.setBirthday(userT.getBirthday());
        userInfoModel.setAddress(userT.getAddress());
        userInfoModel.setSex(userT.getUserSex());
        userInfoModel.setBeginTime(userT.getBeginTime().getTime());
        userInfoModel.setBiography(userT.getBiography());
        return userInfoModel;
    }

    private Date time2Data(Long time) {
        Date date = new Date(time);
        return date;
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {
        UserT userT = userTMapper.selectById(uuid);
        UserInfoModel userInfoModel = do2UserInfo(userT);
        return userInfoModel;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        UserT userT = new UserT();
        userT.setUuid(userInfoModel.getUuid());
        userT.setUserName(userInfoModel.getUsername());
        userT.setLifeState(Integer.parseInt(userInfoModel.getLiftState()));
        userT.setBirthday(userInfoModel.getBirthday());
        userT.setBiography(userInfoModel.getBiography());
        userT.setBeginTime(null);
        userT.setHeadUrl(userInfoModel.getHeadAddress());
        userT.setEmail(userInfoModel.getEmail());
        userT.setAddress(userInfoModel.getAddress());
        userT.setUserPhone(userInfoModel.getPhone());
        userT.setUserSex(userInfoModel.getSex());
        userT.setUpdateTime(time2Data(System.currentTimeMillis()));

        Integer result = userTMapper.updateById(userT);
        if (result > 0) {
            return getUserInfo(userT.getUuid());
        } else {
            return userInfoModel;
        }
    }
}
