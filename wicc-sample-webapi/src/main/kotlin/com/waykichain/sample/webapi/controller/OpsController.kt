package com.waykichain.rcenter.webapi.controller

import com.waykichain.commons.base.BizResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ops")
class OpsController: BaseController() {

    @RequestMapping("/ver")
    fun getVersion(): BizResponse<String> {
        return BizResponse("1.0.0")
    }
}