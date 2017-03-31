package com.pk.common;

/**
 * 返回结果定义
 * Created by pengkai
 * @date 2016/10/13 0009.
 * @email pengxiankaikai@163.com
 */
public interface RetResult {

    String SUCCESS = "SUCCESS";
    String FAIL = "FAIL";
    String NO_MONEY = "NO_MONEY"; //余额不足 特殊标识
    String REST_ING = "REST_ING"; //进入房间时正好是休息中 特殊标识

    String MSG_SUCCESS = "操作成功";
    String MSG_FAIL = "操作失败";
    String MSG_PARAM_ERROR = "参数错误";
    String MSG_NOT_FOUND_RECORD = "该记录不存在";
    String MSG_NOT_LOGIN = "MSG_NOT_LOGIN";
    String MSG_RECORD_REPEAT = "记录重复";
    String MSG_FREQUENTLY = "频繁请求";
    String MSG_REQ_TIMEOUT = "请求超时";
}
