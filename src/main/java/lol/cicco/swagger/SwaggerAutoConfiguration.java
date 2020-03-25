package lol.cicco.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@Configuration
@ConditionalOnProperty(value = "swagger.enable", havingValue = "true")
public class SwaggerAutoConfiguration {

    @Bean
    public Docket docket(SwaggerProperties swaggerProperty, @Value("${spring.application.name:Application}") String applicationName) {
        log.debug("加载swagger...");

        Predicate<RequestHandler> predicate = null;
        String[] packages = swaggerProperty.getBasePackage().split(",");
        for(String s : packages) {
            if(Strings.isNullOrEmpty(s)) {
                continue;
            }
            if(predicate == null) {
                predicate = RequestHandlerSelectors.basePackage(s);
            } else {
                predicate = Predicates.or(predicate, RequestHandlerSelectors.basePackage(s));
            }
        }

        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerProperty, applicationName))
                .select()
                .paths(PathSelectors.any());

        if(predicate != null) {
            builder.apis(predicate);
        }

        return builder.build();
    }

    private ApiInfo apiInfo(SwaggerProperties swaggerProperty, String applicationName) {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(swaggerProperty.getDescription())
                .contact(new Contact(
                            swaggerProperty.getContactName(),
                            swaggerProperty.getUrl(),
                            swaggerProperty.getEmail())
                        )
                .version(swaggerProperty.getVersion())
                .build();
    }
}
