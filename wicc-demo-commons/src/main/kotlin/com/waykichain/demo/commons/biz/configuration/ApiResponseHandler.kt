package com.waykichain.demo.commons.biz.configuration


import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import javax.servlet.http.HttpServletRequest


@ControllerAdvice
open class ApiResponseHandler : ResponseBodyAdvice<Any> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun beforeBodyWrite(body: Any, returnType: MethodParameter?, selectedContentType: MediaType?,
                                 selectedConverterType: Class<out HttpMessageConverter<*>>?,
                                 request: ServerHttpRequest?, response: ServerHttpResponse?): Any? {
        return null

    }


    override fun supports(returnType: MethodParameter?, converterType: Class<out HttpMessageConverter<*>>?): Boolean {
        return true
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    @Throws(Exception::class)
    fun parameterInvalidException(e: MethodArgumentNotValidException, request: HttpServletRequest) {

    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    @Throws(Exception::class)
    fun sysException(e: Exception, request: HttpServletRequest) {

    }

}