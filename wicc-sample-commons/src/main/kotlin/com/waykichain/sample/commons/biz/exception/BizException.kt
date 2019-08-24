package com.waykichain.sample.commons.biz.exception

import com.waykichain.sample.commons.biz.dict.ErrorCode

/**
 * @ClassName: BizException
 * @Description: 异常
 * @Date: 2019/4/9 15:00
 */
class BizException(val code: Int, val msg: String) : RuntimeException(msg) {
    constructor(errorCode: ErrorCode) : this(errorCode.code, errorCode.msg)
}