package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/20 8:40 PM
 */
@Data
public class AliPayResultVO implements Serializable {
    private String orderId;
    private Integer orderStatus;
    private String orderMsg;
}
