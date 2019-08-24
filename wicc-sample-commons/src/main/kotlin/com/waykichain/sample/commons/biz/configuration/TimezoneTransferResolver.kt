package com.waykichain.sample.commons.biz.configuration

import com.alibaba.fastjson.JSONObject
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.waykichain.commons.util.DateUtils
import com.waykichain.sample.commons.biz.annotations.TimezoneTransfer
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
open class TimezoneTransferResolver : HandlerMethodArgumentResolver {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val TIME_ZONE = "user_timezone"

    private var jsonConverter: MappingJackson2HttpMessageConverter? = null

    init {
        jsonConverter = MappingJackson2HttpMessageConverter()

        jsonConverter!!.objectMapper = ObjectMapper()
                .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setDateFormat(SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {

        return  parameter.hasMethodAnnotation(TimezoneTransfer::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {

        val timeZone = parseTimezone(webRequest)
        jsonConverter!!.objectMapper.setTimeZone(timeZone)

        if (parameter.parameterType.name == "java.util.Date") {

            var dateStr = webRequest.getParameter(parameter.parameterName)
            return parseDateStrToDefaultZone(dateStr, timeZone)
        }

        val json = getRequestBody((webRequest as ServletWebRequest).request)

        val po = JSONObject.parseObject(json, parameter.parameterType)
        val formatStr = jsonConverter!!.objectMapper.writeValueAsString(po)

        return JSONObject.parseObject(formatStr, parameter.parameterType)
    }

    //将date字符串转为GMT+0的时间
    private fun parseDateStrToDefaultZone(dateStr: String, zone: TimeZone): Date {

        val date = DateUtils.getDate(dateStr, DateUtils.DEFAULTDATEPATTERN)

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.MILLISECOND, zone.rawOffset)
        calendar.timeZone = TimeZone.getDefault()

        return calendar.time
    }

    private fun parseTimezone(webRequest: WebRequest): TimeZone {

        var zone = webRequest.getHeader(TIME_ZONE) ?: let {
            return TimeZone.getDefault()
        }

        var default = TimeZone.getDefault()
        var requestTimeZone = TimeZone.getDefault()

        try {
            val hour = zone.toInt()
            if (hour >= 0) {
                zone = "GMT+$hour"
            } else {
                zone = "GMT$hour"
            }

            requestTimeZone = StringUtils.parseTimeZoneString(zone)
        } catch (e: Exception) {
            logger.error(" resolveArgument parse url[${(webRequest as ServletWebRequest).request.requestURI}] to timezone failed, use default timezone", e)
        }

        val offsetHours = (default.rawOffset - requestTimeZone.rawOffset) / (60 * 60 * 1000)
        if (offsetHours >= 0) {
            zone = "GMT+$offsetHours"
        } else {
            zone = "GMT$offsetHours"
        }

        return StringUtils.parseTimeZoneString(zone)
    }


    private fun getRequestBody(request: HttpServletRequest): String? {

        try {
            val stringBuffer = StringBuffer()

            val buf: ByteArray = kotlin.ByteArray(1024 * 100)

            val inputStream = request.inputStream
            var len = 0

            while (inputStream.read(buf).apply { len = this } != -1) {
                stringBuffer.append(String(buf, 0, len))
            }

            if (stringBuffer.isEmpty()) {
                logger.error(" getRequestBody stringBuffer is null")
                return null
            }

            return stringBuffer.toString()
        } catch (e: Exception) {
            logger.error(" getRequestBody exception", e)
            return null
        }
    }

}
