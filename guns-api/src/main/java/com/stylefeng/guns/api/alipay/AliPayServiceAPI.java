package com.stylefeng.guns.api.alipay;

import com.stylefeng.guns.api.alipay.vo.AliPayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AliPayResultVO;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/20 8:38 PM
 */
public interface AliPayServiceAPI{
    AliPayInfoVO getQRCode(String orderId);
    AliPayResultVO getOrderStatus(String orderId);

}
