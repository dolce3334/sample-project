package com.waykichain.sample.webapi.config.mvc

import com.waykichain.sample.commons.biz.configuration.CommonMvcConfig
import com.waykichain.sample.webapi.config.interceptor.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

@Configuration
open class WebapiMvcConfig : CommonMvcConfig() {

    /**拦截用户请求*/
    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry!!.addInterceptor(requestInterceptor())
    }

    /** token校验*/
    @Bean
    open fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor()
    }
}