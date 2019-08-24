package com.waykichain.sample.task.config

import com.waykichain.sample.commons.biz.env.task.Environment
import com.xxl.job.core.executor.XxlJobExecutor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("com.waykichain.sample.task")
open class TaskExecutorConfig {

    @Bean(initMethod = "start", destroyMethod = "destroy")
    open fun xxlJobExecutor(): XxlJobExecutor {
        return init()
    }

    private fun init(): XxlJobExecutor {
        val xxlJobExecutor = XxlJobExecutor()
        xxlJobExecutor.setIp(Environment.TASK_EXECUTOR_IP)
        xxlJobExecutor.setPort(Integer.parseInt(Environment.TASK_EXECUTOR_PORT))
        xxlJobExecutor.setAppName(Environment.TASK_APP_NAME)
        xxlJobExecutor.setAdminAddresses(Environment.TASK_JOB_ADMIN_URL)
        xxlJobExecutor.setLogPath(Environment.TASK_LOG_PATH)
        return xxlJobExecutor
    }
}

