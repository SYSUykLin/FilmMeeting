package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.OrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author greenArrow
 * @since 2020-01-17
 */
public interface OrderTMapper extends BaseMapper<OrderT> {

    String getSeatsByFieldId(@Param("fieldId") String fieldId);

}
