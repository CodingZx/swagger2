package lol.cicco.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("swagger")
public class SwaggerProperties {
    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 项目描述
     */
    private String description = "";
    /**
     * 项目版本
     */
    private String version = "";
    /**
     * 联系邮箱
     */
    private String email = "";
    /**
     * 项目负责人
     */
    private String contactName = "";
    /**
     * 相关网站url
     */
    private String url = "";
    /**
     * 扫描路径
     */
    private String basePackage = "";

}
