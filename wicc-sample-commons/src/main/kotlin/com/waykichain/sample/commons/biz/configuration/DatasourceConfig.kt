package com.waykichain.sample.commons.biz.configuration

import com.alibaba.druid.pool.DruidDataSource
import com.waykichain.sample.commons.biz.env.Environment
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.SQLException
import javax.persistence.EntityManager
import javax.sql.DataSource

/**
 * @ClassName: DatasourceConfig
 * @Description: 数据库配置
 * @Date: 2019/8/9 15:04
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef="entityManagerFactory",
    transactionManagerRef="transactionManager",
    basePackages= arrayOf("com.waykichain.sample.repository"))
@Primary
open class DatasourceConfig {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 添加mysql 拦截器
     */
    @Bean
    @Primary
    open fun configureCustomer(): ConfigurationCustomizer {

        return ConfigurationCustomizer { ShowSqlInterceptor() }
    }

    /**
     * 配置druid 链接池
     *
     */
    @Bean
    @Primary
    open fun dataSource(): DataSource {

        val datasource = DruidDataSource()

        datasource.url = Environment.MYSQL_URL
        datasource.username = Environment.MYSQL_USERNAME
        datasource.password = Environment.MYSQL_PASSWORD
        datasource.driverClassName = Environment.MYSQL_DRIVER

        //configuration
        datasource.initialSize = Environment.MYSQL_INITIALSIZE
        datasource.minIdle = Environment.MYSQL_MIN_IDLE
        datasource.maxActive = Environment.MYSQL_MAX_ACTIVE
        datasource.maxWait = 5000
        datasource.timeBetweenEvictionRunsMillis = 10000
        datasource.minEvictableIdleTimeMillis = 30000
        datasource.isTestWhileIdle = true
        datasource.isTestOnBorrow = false
        datasource.isTestOnReturn = false
        datasource.isPoolPreparedStatements = true
        datasource.maxPoolPreparedStatementPerConnectionSize = 20
        datasource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000")
        datasource.validationQuery = "select now()"

        try {
            datasource.setFilters("stat,wall,log4j")
        } catch (e: SQLException) {
            logger.error("druid configuration initialization filter", e)
        }

        return datasource
    }

    @Bean
    @Primary
    open fun entityManager(builder: EntityManagerFactoryBuilder): EntityManager {
        return entityManagerFactory(builder).getObject().createEntityManager()
    }

    @Bean
    @Primary
    open fun entityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource())
            .properties(getVendorProperties(dataSource()))
            .packages("com.waykichain.sample.entity.domain") //设置实体类所在位置
            .persistenceUnit("PersistenceUnit")
            .build()
    }

    @Primary
    @Bean("entityManagerFactoryBetSql")
    open fun entityManagerFactoryBetSql(): SqlSessionFactory {
        val factoryBean = SqlSessionFactoryBean()
        factoryBean.setDataSource(dataSource())
        val resolver = PathMatchingResourcePatternResolver()
        factoryBean.setMapperLocations(resolver.getResources("classpath*:com/waykichain/sample/mapper/*.xml"))
        return factoryBean.getObject()
    }

    @Autowired
    private val jpaProperties: JpaProperties? = null

    private fun getVendorProperties(dataSource: DataSource): Map<String, String> {
        if(jpaProperties == null)
            return HashMap<String,String>()

        return jpaProperties.getHibernateProperties(dataSource)
    }

    @Bean
    @Primary
    open fun transactionManager(builder: EntityManagerFactoryBuilder): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory(builder).getObject())
    }

}