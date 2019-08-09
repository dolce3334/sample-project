package com.waykichain.demo.commons.biz.env

import com.waykichain.commons.util.BaseEnv

object Environment {

    @JvmField
    val MYSQL_URL = BaseEnv.env("MYSQL_URL", "jdbc:mysql://dev-mysql.wic.me:3306/wicc-bet?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false")

    @JvmField
    val MYSQL_DRIVER = BaseEnv.env("MYSQL_DRIVER", "com.mysql.jdbc.Driver")

    @JvmField
    val MYSQL_USERNAME = BaseEnv.env("MYSQL_USERNAME", "wicc-rw")

    @JvmField
    val MYSQL_PASSWORD = BaseEnv.env("MYSQL_PASSWORD", "wicc@123456")

    @JvmField
    val MYSQL_INITIALSIZE = BaseEnv.env("MYSQL_INITIALSIZE", 10)
    @JvmField
    val MYSQL_MIN_IDLE = BaseEnv.env("MYSQL_MIN_IDLE", 5)
    @JvmField
    val MYSQL_MAX_ACTIVE = BaseEnv.env("MYSQL_MAX_ACTIVE", 20)
}