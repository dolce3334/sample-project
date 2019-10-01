package com.waykichain.sample.commons.biz.env.task

import com.waykichain.commons.util.BaseEnv


object Environment {
    /**
     * 定时任务
     */
    @JvmField
    var TASK_JOB_ADMIN_URL= BaseEnv.env("TASK_JOB_ADMIN_URL", "http://10.0.0.7:8888")
    @JvmField
    var TASK_APP_NAME = BaseEnv.env("TASK_APP_NAME", "wicc-sample-executor")
    @JvmField
    var TASK_EXECUTOR_IP= BaseEnv.env("TASK_EXECUTOR_IP", "10.0.0.67")
    @JvmField
    var TASK_EXECUTOR_PORT = BaseEnv.env("TASK_EXECUTOR_PORT", "27003") //RPC Port
    @JvmField
    var TASK_LOG_PATH= BaseEnv.env("TASK_LOG_PATH", "~/wicc/logs")


}

