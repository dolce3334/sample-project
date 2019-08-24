package com.waykichain

import com.waykichain.commons.util.BaseEnv
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @ClassName: Application
 * @Description: 启动服务
 * @Date: 2019/8/9 14:27
 */

@SpringBootApplication
@MapperScan("com/waykichin/sample/mapper")
open class Application

fun main(args: Array<String>) {
    BaseEnv.init()
    SpringApplication.run(Application::class.java, *args)
}