package com.waykichain.rcenter.commons.biz.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.text.SimpleDateFormat
import javax.servlet.Filter

@Configuration
open class CommonMvcConfig : WebMvcConfigurerAdapter() {

    /**扩展JSON输出*/
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(customJackson2HttpMessageConverter())
    }

    /**JSON输出格式*/
    @Bean
    open fun customJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {

        val jsonConverter = MappingJackson2HttpMessageConverter()

        jsonConverter.objectMapper = ObjectMapper()
                .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setDateFormat(SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))

        return jsonConverter
    }

    open fun timezoneTransfer(): TimezoneTransferResolver {
        return TimezoneTransferResolver()
    }


    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        super.addArgumentResolvers(argumentResolvers)
        argumentResolvers.add(timezoneTransfer())
    }

}