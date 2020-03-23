package com.stylefeng.guns.api.order;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.order.vo.OrderVO;
import org.mengyun.tcctransaction.api.Compensable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/17 5:56 PM
 */
public interface OrderServiceAPI {
    @Compensable
    boolean isTrueSeats(String fieldId, String seats);

    @Compensable
    boolean isNotSoldSeats(String fieldId, String seats);

    @Compensable
    OrderVO saveOrderInfo(Integer field, String soldSeats, String seatsName, Integer userId);

    Page<OrderVO> getOrderByUserId(Integer userId, Page<OrderVO> page);

    String getSoldSeatsByFieldId(Integer fieldId);

    OrderVO getOrderInfoById(String orderId);

    boolean paySuccess(String orderId);

    boolean payFail(String orderId);
}
