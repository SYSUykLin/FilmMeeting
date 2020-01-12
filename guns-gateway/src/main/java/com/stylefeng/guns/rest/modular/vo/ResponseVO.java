package com.stylefeng.guns.rest.modular.vo;

import lombok.Data;

/**
 * 与前端交互的视图
 * status，error message，data
 * [0-success] [1-fail] [999-error from system]
 *
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/10 9:15 AM
 */
@Data
public class ResponseVO<M> {
    private int status;
    private String msg;
    private M data;
    private String imgPre;

    private ResponseVO() {
    }

    public static <M> ResponseVO success(M m) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);
        return responseVO;
    }

    public static <M> ResponseVO success(String imgPre, M m) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        return responseVO;
    }

    public static <M> ResponseVO success(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);
        return responseVO;
    }


    public static <M> ResponseVO serviceFail(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setMsg(msg);
        return responseVO;
    }

    public static <M> ResponseVO appFail(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(999);
        responseVO.setMsg(msg);
        return responseVO;
    }
}
