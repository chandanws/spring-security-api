
package br.com.jonyfs.config;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@Import(SpringDataRestConfiguration.class) TODO WAIT FOR SPRING BOOT 2.0 COMPATIBILITY
@EnableSwagger2
@Slf4j
public class SwaggerConfig {

    @PostConstruct
    public void logMessage() {
        LOGGER.info("STARTED");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
