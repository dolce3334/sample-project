package com.waykichain.rcenter.webapi.config.loader

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
 * 启动后自动执行的任务(注意修改名称)
 */
@Component
open class AuthLoader : CommandLineRunner{

    val logger = LoggerFactory.getLogger(javaClass)

    @Async
    override fun run(vararg args: String?) {
        logger.info("--------start to load role-resource--------------")
        logger.info("--------finished to load role-resource-----------")
    }
}