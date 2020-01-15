package com.stylefeng.guns.rest.common.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.api.film.vo.FilmDetailVO;
import com.stylefeng.guns.rest.common.persistence.model.FilmT;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author greenArrow
 * @since 2020-01-12
 */
public interface FilmTMapper extends BaseMapper<FilmT> {
    FilmDetailVO getFilmDetailByName(@Param("filmName") String filmName);

    FilmDetailVO getFilmDetailById(@Param("uuid") String uuid);
}
