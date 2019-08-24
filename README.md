# wicc-sample-svc

克隆项目后，改造成新项目，需依次执行如下操作（以wicc-sample-svc项目为例）：
1、右上角project structure里面修改project name 为 wicc-sample-svc
   或者打开.idea目录，修改.name文件内容

2、修改顶部名称为 wicc-sample-svc，修改 settings.gradle 文件中的 rootProject.name 为 wicc-sample-svc

3、rename module com.waykichain.wicc-demo-commons com.waykichain.wicc-sample-commons
   rename module com.waykichain.wicc-demo-entity com.waykichain.wicc-sample-entity
   rename module com.waykichain.wicc-demo-webapi com.waykichain.wicc-sample-webapi
   rename module com.waykichain.wicc-demo-xservice com.waykichain.wicc-sample-xservice

4、修改 settings.gradle 文件中的 include 'demo' 全部替换为 'sample'
   修改weiapi中的 build.gradle 中的依赖 wicc-demo-commons 为 wicc-sample-commons

5、关闭项目，修改四个目录名称，并重新打开项目

6、修改每个module下面的包名

7、对应每个module所需的修改
 webapi： 修改包名
          修改BaseController（按需修改）
          修改拦截器，主要包含token校验
          启动后执行的任务，如不需要则无需修改
          修改Application.kt中MapperScanner扫描Mybatis的Mapper路径
          修改application.yml中的Mybatis的xml文件路径

 entity: 主要是修改包名和 build.gradle 文件中的配置
            baseName = 'wicc-sample-entity'
            jdbcUrl
            domainPackageName
            repositoryPackageName
            serviceSrcPath
            servicePackageName

 xservice: 修改包名
            修改 build.gradle 配置

 commons: 修改包名
          修改build.gradle文件的的依赖和包名配置
          修改数据库配置
          修改Environment中的url等配置
          ApiResponseHandler中需手动修改日志记录
