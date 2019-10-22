package com.waykichain.sample.commons.biz.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.async.DeferredResult
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
open class SwaggerConfig {

    @Bean
    open fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .enable(true)
            .groupName("Waykichain loan")
            .genericModelSubstitutes(DeferredResult::class.java)
            .useDefaultResponseMessages(false)
            .forCodeGeneration(false)
            .pathMapping("/")   // base，最终调用接口后会和paths拼接在一起
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.waykichain"))
            .build()
            .apiInfo(loanApiInfo())

    private fun loanApiInfo(): ApiInfo {


        return ApiInfoBuilder()
                .title("Waykichain Platform API")//大标题
                .description("Waykichain Platform's REST API, all the applications could assess the Object model data via JSON.")//详细描述
                .version("1.0")//版本
                .termsOfServiceUrl("NO terms of service")
                .contact(Contact("Richard Chen", "http://www.waykichain.net", "tigerich at 163 dot com"))//作者
                .license("Commercial License, Version 1.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build()

    }
}