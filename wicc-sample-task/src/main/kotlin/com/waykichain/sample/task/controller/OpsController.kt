package com.waykichain.sample.task.controller

import com.waykichain.commons.base.BizResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @ClassName: OpsController
 * @Date: 2019/5/10 10:12
 */
@RestController
@RequestMapping("/ops")
class OpsController {

    @RequestMapping("/ver")
    fun onVersion(): BizResponse<String> {
        return BizResponse("0.0.3")
    }

}