package com.spiderdt.mars.swagger.configuration

import com.mangofactory.swagger.configuration.SpringSwaggerConfig
import com.mangofactory.swagger.models.dto.ApiInfo
import com.mangofactory.swagger.plugin.EnableSwagger
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
/**
 * @Title:
 * @Package com.spiderdt.jupiter.swagger.configuration
 * @Description:
 * @author ranran
 * @date 2017/5/10 11:24
 * @version V1.0
 */
@Configuration
//NOTE: Only needed in a non-springboot application
@EnableSwagger
//@ComponentScan("com.spiderdt.jupiter.controller")
class SwaggerConfig {
    private SpringSwaggerConfig springSwaggerConfig

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig
    }
    /**
     * 自定义实现 customImplementation
     * @return
     */
    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(apiInfo())
                .includePatterns(".*?")
    }
    /**
     * title
     description
     terms of serviceUrl
     contact email
     license type
     license url
     * @return
     */
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Jupiter-api-v2 接口",
                "Jupiter-api-v2 接口，主要用于方便前后端测试",
                "termsOfServiceUrl",
                "联系邮箱 contact email",
                "许可证的类型 license type",
                "许可证的链接 license url"
        )
        return apiInfo
    }
}
