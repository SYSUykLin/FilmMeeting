package com.stylefeng.guns.api.order;

import com.stylefeng.guns.api.order.vo.OrderVO;
import java.util.*;
/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/17 5:56 PM
 */
public interface OrderServiceAPI {
    boolean isTrueSeats(String fieldId, String seats);

    boolean isNotSoldSeats(String fieldId, String seats);

    OrderVO saveOrderInfo(Integer field, String soldSeats, String seatsName, Integer userId);

    List<OrderVO> getOrderByUserId(Integer userId);

    String getSoldSeatsByFieldId(Integer fieldId);
}
