package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.OrderQueryVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.core.util.UUIDUtil;
import com.stylefeng.guns.rest.common.persistence.dao.OrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.OrderT;
import com.stylefeng.guns.rest.common.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/17 7:00 PM
 */
@Slf4j
@Component
@Service(interfaceClass = OrderServiceAPI.class)
public class DefaultOrderServiceImpl implements OrderServiceAPI {
    @Resource
    private OrderTMapper orderTMapper;
    @Reference(interfaceClass = CinemaServiceAPI.class, check = false)
    private CinemaServiceAPI cinemaServiceAPI;
    @Autowired
    private FTPUtil ftpUtil;

    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
        String seatPath = orderTMapper.getSeatsByFieldId(fieldId);
        String fileStrByAddress = ftpUtil.getFileStrByAddress(seatPath);
        JSONObject jsonObject = JSONObject.parseObject(fileStrByAddress);
        String ids = jsonObject.get("ids").toString();
        String[] seatArr = seats.split(",");
        String[] idArr = ids.split(",");
        Integer isTrue = 0;
        for (String id : idArr) {
            for (String seat : seatArr) {
                if (seat.equalsIgnoreCase(id)) {
                    isTrue++;
                }
            }
        }
        return isTrue == seatArr.length;
    }

    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id", fieldId);
        List<OrderT> list = orderTMapper.selectList(entityWrapper);
        String[] seatArr = seats.split(",");
        for (OrderT orderT : list) {
            String[] ids = orderT.getSeatsIds().split(",");
            for (String id : ids) {
                for (String seat : seatArr) {
                    if (id.equalsIgnoreCase(seat)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        String uuid = UUIDUtil.genUuid();
        FilmInfoVO filmInfoVO = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
        int filmId = Integer.parseInt(filmInfoVO.getFilmId());
        OrderQueryVO orderQueryVO = cinemaServiceAPI.getOrderNeeds(fieldId);
        Integer cinemaId = Integer.parseInt(orderQueryVO.getCinemaId());
        double filmPrice = Double.parseDouble(orderQueryVO.getFilmPrice());
        System.out.println();
        int solds = soldSeats.split(",").length;
        double totalPrice = getTotalPrice(solds, filmPrice);
        OrderT orderT = new OrderT();
        orderT.setUuid(uuid);
        orderT.setSeatsName(seatsName);
        orderT.setSeatsIds(soldSeats);
        orderT.setOrderUser(userId);
        orderT.setOrderPrice(totalPrice);
        orderT.setFilmPrice(filmPrice);
        orderT.setFieldId(fieldId);
        orderT.setCinemaId(cinemaId);
        orderT.setFilmId(filmId);
        Integer insert = orderTMapper.insert(orderT);
        if (insert > 0) {
            OrderVO orderVO = orderTMapper.getOrderInfoById(uuid);
            if (orderVO == null || orderVO.getOrderId().trim().length() == 0) {
                log.error("订单信息为空，订单编号{}", uuid);
                return null;
            } else {
                return orderVO;
            }
        } else {
            log.error("插入失败");
            return null;
        }
    }

    private double getTotalPrice(int solds, double filmPrice) {
        BigDecimal soldsDecimal = new BigDecimal(solds);
        BigDecimal filmPriceDecimal = new BigDecimal(filmPrice);
        BigDecimal total = soldsDecimal.multiply(filmPriceDecimal);
        BigDecimal result = total.setScale(2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    @Override
    public Page<OrderVO> getOrderByUserId(Integer userId, Page<OrderVO> page) {
        Page<OrderVO> result = new Page<>();
        if (userId == null) {
            log.error("编号获取失败");
            return null;
        } else {
            List<OrderVO> orderInfoByUserId = orderTMapper.getOrderInfoByUserId(userId, page);
            if (orderInfoByUserId == null && orderInfoByUserId.size() == 0) {
                result.setTotal(0);
                result.setRecords(new ArrayList<>());
                return result;
            } else {
                EntityWrapper<OrderT> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("order_user", userId);
                Integer count = orderTMapper.selectCount(entityWrapper);
                result.setTotal(count);
                result.setRecords(orderInfoByUserId);
                return result;
            }
        }
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        if (fieldId == null) {
            log.error("场次编号错误");
            return "";
        } else {
            String soldSeatsByFieldId = orderTMapper.getSoldSeatsByFieldId(fieldId);
            return soldSeatsByFieldId;
        }
    }

    @Override
    public OrderVO getOrderInfoById(String orderId) {
        OrderVO orderInfoById = orderTMapper.getOrderInfoById(orderId);
        return orderInfoById;
    }

    @Override
    public boolean paySuccess(String orderId) {
        OrderT orderT = new OrderT();
        orderT.setUuid(orderId);
        orderT.setOrderStatus(1);
        Integer integer = orderTMapper.updateById(orderT);
        return integer >= 1;
    }

    @Override
    public boolean payFail(String orderId) {
        OrderT orderT = new OrderT();
        orderT.setUuid(orderId);
        orderT.setOrderStatus(2);
        Integer integer = orderTMapper.updateById(orderT);
        return integer >= 1;
    }
}
