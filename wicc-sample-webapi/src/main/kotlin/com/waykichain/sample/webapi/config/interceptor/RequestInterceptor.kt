package com.waykichain.rcenter.webapi.config.interceptor
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


open class RequestInterceptor : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest? , response: HttpServletResponse? , handler: Any?): Boolean {
        LOGGER.trace(" requestUri[${request?.requestURI}], clientIp[${request?.getHeader("X-Real-IP")}], handler[$handler]")
        val requestURI = request?.requestURI
        //swagger api文档 start
        if ((requestURI!!.contains("swagger")
                        || requestURI.contains("api-docs")
                        || requestURI.contains("/error")
                        || requestURI.contains("ops")
                        || requestURI.contains("/admin/log")
                        || requestURI.contains("/login"))) {
            return true
        }

//        val token = request.getHeader("atoken")
//        LOGGER.info("login token: $token")
//        if (StringUtils.isEmpty(token)) throw BizException(ErrorCode.USER_NOT_LOGIN)
//        val uid = adminOauthRepository.getUidByAccessToken(token,Oauth2TokenPrefixType.ADMIN_USER) ?:
//                throw BizException(ErrorCode.ACCESS_TOKEN_EXPIRED)
//        LOGGER.info("login userid: $uid")
//        request.setAttribute("sysuser_id" , uid)
//        request.setAttribute("token" , token)
        return true
    }

    private val LOGGER = LoggerFactory.getLogger(javaClass)

}