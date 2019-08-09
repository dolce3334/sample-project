package com.waykichain

import com.waykichain.commons.util.BaseEnv
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @ClassName: Application
 * @Description: 启动服务
 * @Date: 2019/8/9 14:27
 */

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    BaseEnv.init()
    SpringApplication.run(Application::class.java, *args)
}