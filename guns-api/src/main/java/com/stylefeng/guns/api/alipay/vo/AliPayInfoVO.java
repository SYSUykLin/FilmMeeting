package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/20 8:39 PM
 */
@Data
public class AliPayInfoVO implements Serializable {
    private String orderId;
    private String QRCodeAddress;
}
