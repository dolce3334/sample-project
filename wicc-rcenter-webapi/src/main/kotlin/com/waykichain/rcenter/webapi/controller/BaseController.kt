package com.waykichain.rcenter.webapi.controller

import org.springframework.beans.factory.annotation.Autowired
import java.net.InetAddress
import javax.servlet.http.HttpServletRequest


open class BaseController {

    @Autowired
    protected lateinit var request: HttpServletRequest
//    @Autowired
//    protected lateinit var userService: SysUserService

    protected fun getUserId(): Long {
        val customerId: Long = request.getAttribute("sysuser_id").toString().toLong()
        return customerId
    }

    protected fun getToken() = request.getHeader("atoken")


//    protected fun getUser(): SysUser? {
//        val customerId: Long = request.getAttribute("sysuser_id").toString().toLong()
//        return userService.getById(customerId)
//            ?: throw BizException(ErrorCode.USER_NOT_EXIST)
//    }
//
//    protected fun getUserLang(): SystemLanguageEnum {
//        val lang = request.getHeader(RequestType.LANG_TAG) ?: "zh-CHS"
//        return SystemLanguageEnum.getByStrCode(lang.toString())
//    }

    protected fun getIpAddr(request: HttpServletRequest): String? {

        var ip: String? = request.getHeader("x-forwarded-for")
        if (ip == null || ip.length == 0 || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip == null || ip.length == 0 || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.length == 0 || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
            if (ip == "127.0.0.1") {
                //根据网卡取本机配置的IP
                var inet: InetAddress? = null
                try {
                    inet = InetAddress.getLocalHost()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                ip = inet!!.hostAddress
            }
        }

        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","))
            }
        }
        return ip
    }


}




