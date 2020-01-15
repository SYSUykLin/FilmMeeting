package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.CinemaT;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/15 8:50 PM
 */
@Component
@Service(interfaceClass = CinemaServiceAPI.class)
public class DefaultCinemaServiceImpl implements CinemaServiceAPI {

    @Resource
    private CinemaTMapper cinemaTMapper;

    @Resource
    private AreaDictTMapper areaDictTMapper;

    @Resource
    private BrandDictTMapper brandDictTMapper;

    @Resource
    private HallDictTMapper hallDictTMapper;

    @Resource
    private HallFilmInfoTMapper hallFilmInfoTMapper;

    @Resource
    private FieldTMapper fieldTMapper;

    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {

        List<CinemaVO> cinemas = new ArrayList<>();

        Page<CinemaT> page = new Page<>(cinemaQueryVO.getNowPage(), cinemaQueryVO.getPageSize());
        EntityWrapper<CinemaT> entityWrapper = new EntityWrapper<>();
        if (cinemaQueryVO.getBrandId() != 99) {
            entityWrapper.eq("brand_id", cinemaQueryVO.getBrandId());
        }
        if (cinemaQueryVO.getDistrictId() != 99) {
            entityWrapper.eq("area_id", cinemaQueryVO.getDistrictId());
        }
        if (cinemaQueryVO.getHallId() != 99) {
            entityWrapper.like("hall_ids", "%#+" + cinemaQueryVO.getHallId() + "+#%");
        }
        List<CinemaT> cinemaTs = cinemaTMapper.selectPage(page, entityWrapper);
        for (CinemaT cinemaT : cinemaTs) {
            CinemaVO cinemaVO = new CinemaVO();

            cinemaVO.setUuid(cinemaT.getUuid() + "");
            cinemaVO.setMinimumPrice(cinemaT.getMinimumPrice() + "");
            cinemaVO.setAddress(cinemaT.getCinemaAddress());
            cinemaVO.setCinemaName(cinemaT.getCinemaName());
            cinemas.add(cinemaVO);
        }
        long counts = cinemaTMapper.selectCount(entityWrapper);
        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemas);
        result.setSize(cinemaQueryVO.getPageSize());
        result.setTotal(counts);
        return result;
    }

    @Override
    public List<BrandVO> getBrands(Integer brandId) {
        return null;
    }

    @Override
    public List<AreaVO> getAreas(Integer areaId) {
        return null;
    }

    @Override
    public List<HallTypeVO> getHallTypes(Integer hallType) {
        return null;
    }

    @Override
    public CinemaInfoVO getCinemaInfoById(Integer cinemaId) {
        return null;
    }

    @Override
    public FilmInfoVO getFilmInfoByCinemaId(Integer cinemaId) {
        return null;
    }

    @Override
    public FilmFieldVO getFilmFieldInfo(Integer fieldId) {
        return null;
    }

    @Override
    public FilmInfoVO getFilmInfoByFieldId(Integer fieldId) {
        return null;
    }
}
