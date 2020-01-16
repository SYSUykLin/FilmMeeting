package com.stylefeng.guns.rest.common.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.rest.common.persistence.model.FieldT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 放映场次表 Mapper 接口
 * </p>
 *
 * @author greenArrow
 * @since 2020-01-15
 */
public interface FieldTMapper extends BaseMapper<FieldT> {
    List<FilmInfoVO> getFilmInfos(@Param("cinemaId") Integer cinemaId);

    HallInfoVO getHallInfo(@Param("fieldId") Integer fieldId);

    FilmInfoVO getFilmInfoById(@Param("fieldId") Integer fieldId);
}
