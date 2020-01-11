package com.stylefeng.guns.api.user;

import java.io.Serializable;

/**
 * 这个userModel不会封装所有的用户信息，只会封装用户的注册信息而已
 *
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/9 6:20 PM
 */
public class UserModel implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
