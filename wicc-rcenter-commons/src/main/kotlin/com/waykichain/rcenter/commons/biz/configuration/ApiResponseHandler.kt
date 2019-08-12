package com.waykichain.rcenter.commons.biz.configuration


import com.alibaba.fastjson.JSONObject
import com.waykichain.commons.base.BizResponse
import com.waykichain.rcenter.commons.biz.dict.RequestType
import com.waykichain.rcenter.commons.biz.exception.BizException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.util.StringUtils
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.util.*
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
open class ApiResponseHandler : ResponseBodyAdvice<Any> {

    override fun beforeBodyWrite(body: Any, returnType: MethodParameter?, selectedContentType: MediaType?,
                                 selectedConverterType: Class<out HttpMessageConverter<*>>?,
                                 request: ServerHttpRequest?, response: ServerHttpResponse?): Any? {
        if (!request!!.uri.toString().contains("swagger") && request.uri.toString().contains("api-docs")) {
            logger.trace(" uri[${request.uri}], and result[$body]")
            return body
        }

        val timeZone = parseTimezone(request)
        jsonConverter.objectMapper.setTimeZone(timeZone)

        val result = jsonConverter.objectMapper.writeValueAsString(body)

        val httpLogMessage = constructHttpLogMessage((request as ServletServerHttpRequest).servletRequest, result)
//        httpLogMessage?.let {
//            val httpRequestLog = constructHttpRequestLog(it)
//            sysAdminRequestLogService.save(httpRequestLog)
//        }
        //TODO 此处需记录系统请求日志，表需要对应生成

        return JSONObject.parseObject(result, body.javaClass)

    }


    override fun supports(returnType: MethodParameter?, converterType: Class<out HttpMessageConverter<*>>?): Boolean {
        return true
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    @Throws(Exception::class)
    fun parameterInvalidException(e: MethodArgumentNotValidException, request: HttpServletRequest): BizResponse<Any> {
        logger.error(" parameterInvalidException :", e)
        val response = BizResponse<Any>(9000, "年轻人再等等，总会出来的")

        try {
            val propertyBindResult: BeanPropertyBindingResult = e.bindingResult as BeanPropertyBindingResult
            if (propertyBindResult.hasErrors()) {
                val sbf = StringBuffer()
                propertyBindResult.allErrors.forEach {
                    sbf.append(it.defaultMessage).append(";")
                }
                response.msg = sbf.toString()
            }
            return response
        } catch (e: Exception) {
            request.setAttribute(EXCEPTION_TAG, getStackTrace(e))
            request.setAttribute(ERROR_CODE_TAG, response.code)
            logger.error(" parameterInvalidException url[${request.requestURL}]", e)
            e.printStackTrace()
            return response
        }
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    @Throws(Exception::class)
    fun sysException(e: Exception, request: HttpServletRequest): BizResponse<Any> {
        val response = BizResponse<Any>(9000, "年轻人再等等，总会出来的")
        request.setAttribute(EXCEPTION_TAG, getStackTrace(e))
        request.setAttribute(ERROR_CODE_TAG, response.code)

        logger.error("Exception requestUrl[${request.requestURL}] ", e)
        e.printStackTrace()
        return response
    }

    @ExceptionHandler(BizException::class)
    @ResponseBody
    @Throws(Exception::class)
    fun bizException(e: BizException, request: HttpServletRequest): BizResponse<Any> {
        val response = BizResponse<Any>(e.code, e.msg)
        request.setAttribute(ERROR_CODE_TAG, response.code)
        request.setAttribute(EXCEPTION_TAG, getStackTrace(e))
        logger.error("BizException requestUrl[${request.requestURL}] ${e.code}:${e.msg}")
        return response
    }

    private fun parseTimezone(request: ServerHttpRequest): TimeZone {

        var zone = (request as ServletServerHttpRequest).servletRequest.getHeader(TIME_ZONE)
        var timeZone = TimeZone.getDefault()

        try {
            val hour = zone.toInt()
            if (hour >= 0) {
                zone = "GMT+$hour"
            } else {
                zone = "GMT$hour"
            }

            timeZone = StringUtils.parseTimeZoneString(zone)
        } catch (e: Exception) {
            logger.error(" parseTimezone ${request.servletRequest.requestURI} parse[$zone] to timezone failed, use default timezone[$timeZone]")
        }

        return timeZone
    }

    private fun getStackTrace(e: Exception): String {
        val stringBuffer = StringBuffer(e.toString() + "\n")
        val messages = e.stackTrace
        messages.forEach {
            stringBuffer.append("\t $it \n")
        }
        return stringBuffer.toString()
    }

    private fun constructHttpLogMessage(request: HttpServletRequest, response: String?): HttpLogMessage? {

        val requestURI = request.requestURL.toString()
        logger.trace(" requestURI:[$requestURI]")

        val httpLogMessage = HttpLogMessage()
        httpLogMessage.customerId = getCustomerId(request)
        httpLogMessage.errorCode = getErrorCode(request)
        httpLogMessage.method = request.method
        httpLogMessage.deviceUuid = getDeviceUuid(request)
        httpLogMessage.requestIp = getRealRemoteIpAddress(request)
        httpLogMessage.module = serverProperties.displayName
        httpLogMessage.requestParmas = getRequestBody(request)
        httpLogMessage.requestUri = request.requestURI
        httpLogMessage.requestUrl = request.requestURL.toString()
        httpLogMessage.requestUuid = getRequestUuid(request)
        httpLogMessage.response = response
        httpLogMessage.stackTrace = getStackTrace(request)
        httpLogMessage.timestamp = getTimestamp(request)
        httpLogMessage.responseTime = (System.currentTimeMillis() - httpLogMessage.timestamp!!)

        return httpLogMessage
    }

    private fun getCustomerId(request: HttpServletRequest): Long? {
        val customerIdStr = request.getAttribute(RequestType.REQUEST_CUSTOMER_ID)?.toString()
        if (null == customerIdStr || "" == customerIdStr) {
            val token = request.getHeader("access_token")
            if (null == token || "" == token) {
                return null
            }
        }
        return customerIdStr?.toLong()
    }

    private fun getTimestamp(request: HttpServletRequest): Long? {
        return request.getAttribute(RequestType.REQUEST_TIMESTAMP_TAG)!!.toString().toLong()
    }

    private fun getRequestUuid(request: HttpServletRequest): String? {
        return request.getHeader(RequestType.REQUEST_UUID_TAG)
    }

    private fun getErrorCode(request: HttpServletRequest): Int {
        return request.getAttribute(ERROR_CODE_TAG)?.toString()?.toInt() ?: 0
    }

    private fun getDeviceUuid(request: HttpServletRequest): String? {
        return request.getHeader(RequestType.DEVICE_UUID_TAG)?.toString()
    }

    private fun getRealRemoteIpAddress(request: HttpServletRequest): String {

        var clientIp = request.getHeader("X-Forwarded-For")
        if (null != clientIp) {
            val arr = clientIp.replace("[", "").replace("]", "").split(",")
            if (!arr.isEmpty()) {
                clientIp = arr.first()
            }
            return clientIp
        } else {
            clientIp = request.getHeader("X-Real-IP")
        }

        if (null == clientIp) {
            clientIp = request.remoteAddr
        }

        return clientIp
    }

    private fun getRequestBody(request: HttpServletRequest): String {
        val stringBuffer = StringBuffer()
        try {
            val buf: ByteArray = kotlin.ByteArray(1024 * 100)
            val inputStream = request.inputStream
            var len = 0
            while (inputStream.read(buf).apply { len = this } != -1) {
                stringBuffer.append(String(buf, 0, len))
            }
            if (stringBuffer.isEmpty() || stringBuffer.toString() == "") {
                return ""
            }
            val json = JSONObject.parseObject(stringBuffer.toString())
            if (json.containsKey("password")) {
                json["password"] = "*******"
            }
            return json.toString()

        } catch (e: Exception) {

            logger.error(" getRequestBody exception", e)
        }

        return stringBuffer.toString()
    }

    private fun getStackTrace(request: HttpServletRequest): String? {
        return request.getAttribute(EXCEPTION_TAG)?.toString()
    }

    private val EXCEPTION_TAG = "exception"
    private val ERROR_CODE_TAG = "error_code"
    private val TIME_ZONE = "user_timezone"

    @Autowired
    private lateinit var serverProperties: ServerProperties

    @Autowired
    private lateinit var jsonConverter: MappingJackson2HttpMessageConverter

    private val logger = LoggerFactory.getLogger(javaClass)

}