package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.rest.common.persistence.model.ActorT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author greenArrow
 * @since 2020-01-12
 */
public interface ActorTMapper extends BaseMapper<ActorT> {
    List<ActorVO> getActors(@Param("filmId") String filmId);
}
