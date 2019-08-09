package com.waykichain.demo.commons.biz.dict

/**
 * @ClassName: ErrorCode
 * @Description: 错误码
 * @Date: 2019/4/9 14:48
 */
enum class ErrorCode(val code: Int, val msg: String) {

    /**通用错误码 10000以下*/
    SYSTEM_UNKNOWN_ERROR(      9000, "年轻人再等等"),
    REQUEST_PARAM_ERROR(       9001, "请求参数错误"),
    RECORD_NOT_EXIST(          9002, "记录不存在"),
    RECORD_PARAM_ERROR(        9003, "数据记录异常"),
    OPERATION_NOT_ALLOWED(     9004, "无法进行当前操作"),
    REPEAT_NO_EFFECT_ACTION(   9005, "无效的重复操作"),
    NO_EFFECT_ADDRESS(         9006, "无效的查询地址"),
    CALL_OPEN_API_FAILED(      9007, "调用远程API失败"),
    CALL_HESSIAN_API_FAILED(   9008, "调用hessianAPI失败"),
    LOCK_WAIT_TIMEOUT(         9009, "redis锁超时"),
    RECORD_ALREADY_EXIST(      9010, "记录已存在"),

}